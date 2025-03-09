package bni.co.id.consumer.exchange_rate.repository;

import bni.co.id.consumer.exchange_rate.entity.TransactionRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRateRepository extends JpaRepository<TransactionRate, Long> {
}
