package bni.co.id.consumer.exchange_rate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRANSACTION_RATE_DETAIL", schema = "BACKOFFICE")
public class TransactionRateDetail {
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "TRX_EXCHANGE_RATE_DETAIL_SEQ" // Matches the generator name below
    )
    @SequenceGenerator(
            schema = "BACKOFFICE",
            name = "TRX_EXCHANGE_RATE_DETAIL_SEQ",      // Logical name for the generator
            sequenceName = "TRX_EXCHANGE_RATE_DETAIL_SEQ", // Actual database sequence name
            allocationSize = 1          // Matches INCREMENT BY in the sequence
    )
    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "currency_id")
    private MRate currencyId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_header_id")
    private TransactionRateHeader transactionRateHeaderId;

    @Column(name = "rate", precision = 17, scale = 4)
    private BigDecimal rate;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "created_time")
    private LocalDateTime createdTime;
}
