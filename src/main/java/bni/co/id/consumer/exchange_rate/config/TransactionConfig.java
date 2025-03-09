package bni.co.id.consumer.exchange_rate.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "bni.co.id.consumer.exchange_rate.config.repository")
@EnableTransactionManagement
@Slf4j
@Profile("consumer")
public class TransactionConfig {
    public TransactionConfig() {
        log.info("** >> Transaction Config << **");
    }
}
