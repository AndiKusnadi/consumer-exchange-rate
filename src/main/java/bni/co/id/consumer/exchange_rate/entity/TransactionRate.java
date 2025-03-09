package bni.co.id.consumer.exchange_rate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRANSACTION_RATE", schema = "BACKOFFICE")
public class TransactionRate extends AbsBaseEntity implements Serializable {
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "TRX_EXCHANGE_RATE_SEQ" // Matches the generator name below
    )
    @SequenceGenerator(
            schema = "BACKOFFICE",
            name = "TRX_EXCHANGE_RATE_SEQ",      // Logical name for the generator
            sequenceName = "TRX_EXCHANGE_RATE_SEQ", // Actual database sequence name
            allocationSize = 1          // Matches INCREMENT BY in the sequence
    )
    @Id
    private Long id;

    @Column(name = "base")
    private String currencyBase;

    @Column(name = "trx_date")
    private String trxDate;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private MRate currencyId;

    @Column(name="rate", precision = 15, scale = 16)
    private BigDecimal rate;
}
