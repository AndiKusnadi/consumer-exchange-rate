package bni.co.id.consumer.exchange_rate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.objenesis.instantiator.SerializationInstantiatorHelper;

import java.io.Serializable;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MASTER_RATE", schema = "BACKOFFICE")
public class MRate extends AbsBaseEntity implements Serializable {
    @Column(name = "currency_id")
    @Id
    private String currencyId;
}
