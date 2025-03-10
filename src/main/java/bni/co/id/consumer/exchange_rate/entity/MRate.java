package bni.co.id.consumer.exchange_rate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MASTER_RATE", schema = "BACKOFFICE")
public class MRate implements Serializable {
    @Column(name = "currency_id")
    @Id
    private String currencyId;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "created_time")
    private LocalDateTime createdTime;
}
