package bni.co.id.consumer.exchange_rate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRANSACTION_RATE_HEADER", schema = "BACKOFFICE")
public class TransactionRateHeader implements Serializable {
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "TRX_EXCHANGE_RATE_HEADER_SEQ" // Matches the generator name below
    )
    @SequenceGenerator(
            schema = "BACKOFFICE",
            name = "TRX_EXCHANGE_RATE_HEADER_SEQ",      // Logical name for the generator
            sequenceName = "TRX_EXCHANGE_RATE_HEADER_SEQ", // Actual database sequence name
            allocationSize = 1          // Matches INCREMENT BY in the sequence
    )
    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "currency_id")
    private MRate currencyBase;

    @Column(name = "trx_date")
    private String trxDate;

    @Version
    @Column(name = "version")
    private Integer version = 0;

    @Column(name = "created_time")
    private LocalDateTime createdTime;
}
