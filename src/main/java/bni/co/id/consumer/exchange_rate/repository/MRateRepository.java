package bni.co.id.consumer.exchange_rate.repository;

import bni.co.id.consumer.exchange_rate.entity.MRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MRateRepository extends JpaRepository<MRate, String> {
}
