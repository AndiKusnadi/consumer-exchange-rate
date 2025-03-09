package bni.co.id.consumer.exchange_rate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ConsumerExchangeRateApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumerExchangeRateApplication.class, args);
	}

}
