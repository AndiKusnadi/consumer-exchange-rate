package bni.co.id.consumer.exchange_rate.service;

import bni.co.id.consumer.exchange_rate.entity.MRate;
import bni.co.id.consumer.exchange_rate.repository.MRateRepository;
import bni.co.id.consumer.exchange_rate.repository.TransactionRateRepository;
import bni.co.id.consumer.exchange_rate.vo.CurrencyVO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import static java.lang.Thread.sleep;

@Service
@Slf4j
public class ExchangeRateService {

    @Autowired
    private MRateRepository rateRepository;

    @Autowired
    private TransactionRateRepository transactionRateRepository;

    @Transactional
    public void saveExchangeRateData(CurrencyVO pValue)  {
        //save currency id first.
        Optional<MRate> rate = this.rateRepository.findById(pValue.getCurrencyBase());
        if (rate.isEmpty()) {
            MRate rateEntity = new MRate();
            rateEntity.setCurrencyId(pValue.getCurrencyBase());
            rateEntity.setCreatedTime(LocalDateTime.now());
            rateEntity.setVersion(0);
            log.info("save base currency {}", rateEntity.getCurrencyId());
            this.rateRepository.save(rateEntity);
            this.rateRepository.flush();
        }

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (String key : pValue.getRates().keySet()) {
            executor.submit( () -> {
                Optional<MRate> rateEntityTrx = this.rateRepository.findById(key);
                if (rateEntityTrx.isEmpty()) {
                    var rateTrx = addingRate(key);
                    try {
                        rateTrx.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }



    }

    Thread addingRate(String pName) {
        return virtualThread(
                pName,
                () -> {
                    MRate entity = new MRate();
                    entity.setCurrencyId(pName);
                    entity.setCreatedTime(LocalDateTime.now());
                    entity.setVersion(0);
                    log.info("Save currency id {}", entity.getCurrencyId());
                    this.rateRepository.save(entity);
                    this.rateRepository.flush();
                    log.info("Success save {}", entity.getCurrencyId());
                });
    }

    Thread virtualThread(String name, Runnable runnable) {
        return Thread.ofVirtual()
                .name(name)
                .start(runnable);
    }
}
