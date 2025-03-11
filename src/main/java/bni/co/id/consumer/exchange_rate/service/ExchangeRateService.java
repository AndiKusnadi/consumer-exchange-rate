package bni.co.id.consumer.exchange_rate.service;

import bni.co.id.consumer.exchange_rate.entity.MRate;
import bni.co.id.consumer.exchange_rate.entity.TransactionRateDetail;
import bni.co.id.consumer.exchange_rate.entity.TransactionRateHeader;
import bni.co.id.consumer.exchange_rate.repository.MRateRepository;
import bni.co.id.consumer.exchange_rate.repository.TransactionRateDetailRepository;
import bni.co.id.consumer.exchange_rate.repository.TransactionRateHeaderRepository;
import bni.co.id.consumer.exchange_rate.vo.CurrencyVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.retry.annotation.Retryable;
import jakarta.persistence.OptimisticLockException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class ExchangeRateService {

    @Autowired
    private MRateRepository rateRepository;

    @Autowired
    private TransactionRateHeaderRepository transactionRateHeaderRepository;

    @Autowired
    private TransactionRateDetailRepository transactionRateDetailRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional()
    @Retryable(value = OptimisticLockException.class, maxAttempts = 3)
    public void saveExchangeRateData(CurrencyVO pValue) throws ExecutionException, InterruptedException {
        //save currency id first.
        Optional<MRate> rate = this.rateRepository.findById(pValue.getCurrencyBase());
        final TransactionRateHeader transactionRateHeader = new TransactionRateHeader();

        if (rate.isEmpty()) {
            MRate rateEntity = new MRate();
            rateEntity.setCurrencyId(pValue.getCurrencyBase());
            rateEntity.setCreatedTime(LocalDateTime.now());
//            rateEntity.setVersion(0);
            log.info("Currency cccc " + pValue.getCurrencyBase());
            rateEntity = this.rateRepository.saveAndFlush(rateEntity);
//            this.rateRepository.flush();
//            this.entityManager.persist(rateEntity);

            transactionRateHeader.setCreatedTime(LocalDateTime.now());
            transactionRateHeader.setTrxDate(pValue.getTransactionDate());
            transactionRateHeader.setCurrencyBase(rateEntity);
//            entityManager.persist(transactionRateHeader);

            TransactionRateHeader header = transactionRateHeaderRepository.saveAndFlush(transactionRateHeader);
            transactionRateHeader.setId(header.getId());

            log.info("save base currency {}", rateEntity.getCurrencyId());

        } else {
            //save to the main table
            transactionRateHeader.setCreatedTime(LocalDateTime.now());
            transactionRateHeader.setTrxDate(pValue.getTransactionDate());
            transactionRateHeader.setCurrencyBase(rate.get());
//            entityManager.persist(transactionRateHeader);
            TransactionRateHeader header = transactionRateHeaderRepository.saveAndFlush(transactionRateHeader);
            transactionRateHeader.setId(header.getId());
        }

//        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        for (String key : pValue.getRates().keySet()) {
//            var threadRunning = executor.submit(() -> {
                Optional<MRate> rateEntityTrx = this.rateRepository.findById(key);
                String rateValue = pValue.getRates().get(key);
//                log.info("Rate value111 " + rateValue);
//                String testValue = rateValue.substring(rateValue.indexOf("."), rateValue.length());
//                if (testValue.length() > 2)
//                    rateValue = rateValue.substring(0, rateValue.indexOf(".") + 2);
//                else
//                    rateValue = rateValue.substring(0, testValue.length());

//                if(key.equals("IDR")){
//                    log.info("Stopeed");
//                }
//                log.info("Rate value222 " + rateValue);
                if (rateEntityTrx.isEmpty()) {
                    MRate entity = new MRate();
                    entity.setCurrencyId(key);
                    entity.setCreatedTime(LocalDateTime.now());
                    log.info("Save currency id {}", entity.getCurrencyId());
                    entity = this.rateRepository.saveAndFlush(entity);

                    TransactionRateDetail detail = new TransactionRateDetail();
                    detail.setCreatedTime(LocalDateTime.now());
                    detail.setTransactionRateHeaderId(transactionRateHeader);
                    detail.setCurrencyId(entity);
                    detail.setRate(new BigDecimal(rateValue));
                    detail.setVersion(1);
                    transactionRateDetailRepository.saveAndFlush(detail);
                    log.info("Success save {}", entity.getCurrencyId());

//                    var rateTrx = addingRate(key, transactionRateHeader, new BigDecimal(rateValue));
//                    try {
//                        rateTrx.join();
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
                } else {
//                    try {
                    TransactionRateDetail detail = new TransactionRateDetail();
                    detail.setCreatedTime(LocalDateTime.now());
                    detail.setTransactionRateHeaderId(transactionRateHeader);
                    detail.setCurrencyId(rateEntityTrx.get());
                    detail.setRate(new BigDecimal(rateValue));
                    detail.setVersion(1);
//                        entityManager.persist(detail);
//                        entityManager.flush();
                    transactionRateDetailRepository.saveAndFlush(detail);
                    log.info("Save rate");
//                        transactionRateDetailRepository.flush();
//                    }catch(Exception ex){
//                        log.error("aaaaa " + rateEntityTrx.get().getCurrencyId());
//                        log.error(ex.getMessage());
//                    }
                }
//            });
//            threadRunning.get();
        }
    }

//    @Transactional
//    @Retryable(value = StaleObjectStateException.class, maxAttempts = 3)
    Thread addingRate(String pName, TransactionRateHeader pHeader, BigDecimal pRate) {
        return virtualThread(
                pName,
                () -> {
                    MRate entity = new MRate();
                    entity.setCurrencyId(pName);
                    entity.setCreatedTime(LocalDateTime.now());
                    entity.setVersion(1);
                    log.info("Save currency id {}", entity.getCurrencyId());
                    entity = this.rateRepository.saveAndFlush(entity);
//                    this.rateRepository.flush();

                    try {
                        TransactionRateDetail detail = new TransactionRateDetail();
                        detail.setCreatedTime(LocalDateTime.now());
                        detail.setTransactionRateHeaderId(pHeader);
                        detail.setCurrencyId(entity);
                        detail.setRate(pRate);
                        transactionRateDetailRepository.saveAndFlush(detail);
//                        transactionRateDetailRepository.flush();
                        log.info("Success save {}", entity.getCurrencyId());
                    }catch(Exception ex){
                        ex.printStackTrace();
                        log.error("RRRRRR 111 " + pRate.toString());
                    }
                });
    }

//    @Transactional
//    @Retryable(value = StaleObjectStateException.class, maxAttempts = 3)
    Thread virtualThread(String name, Runnable runnable) {
        return Thread.ofVirtual()
                .name(name)
                .start(runnable);
    }
}
