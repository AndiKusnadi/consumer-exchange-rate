package bni.co.id.consumer.exchange_rate.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class CurrencyVO implements Serializable {
    @JsonProperty("base")
    private String currencyBase;
    @JsonProperty("date")
    private String transactionDate;
    @JsonProperty("rates")
    private Map<String, String> rates = new HashMap<>();
}
