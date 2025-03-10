package bni.co.id.consumer.exchange_rate.repository;

import bni.co.id.consumer.exchange_rate.entity.TransactionRateDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRateDetailRepository extends JpaRepository<TransactionRateDetail, Long> {
}
