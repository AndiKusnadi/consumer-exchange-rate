package bni.co.id.consumer.exchange_rate.service;

import bni.co.id.consumer.exchange_rate.config.ApplicationProperties;
import bni.co.id.consumer.exchange_rate.util.GeneralFacility;
import bni.co.id.consumer.exchange_rate.vo.CurrencyVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class ConsumerDataService {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private GeneralFacility generalFacility;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @KafkaListener(topics = "${topic.exchange.rate}", groupId = "${topic.exchange.rate.group.id}")
    public void listen(ConsumerRecord<String, String> record) {
        String consumeStr = record.value();
        try {
            CurrencyVO currencyVO = this.generalFacility.getObjectMapper().readValue(consumeStr, CurrencyVO.class);
            log.info("base " + currencyVO.getCurrencyBase());
            this.exchangeRateService.saveExchangeRateData(currencyVO);
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
