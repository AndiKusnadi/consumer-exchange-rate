package bni.co.id.consumer.exchange_rate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Version;

import java.time.LocalDateTime;

public abstract class AbsBaseEntity {
    @Version
    private Integer version;

    @Column(name = "created_time")
    private LocalDateTime createdTime;
}
