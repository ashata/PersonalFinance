package org.hoboventures.personalFinance.domain;

import com.barchart.ondemand.api.responses.Quote;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Asha on 4/13/2017.
 */
@Data
@NoArgsConstructor
@Document(indexName = "quote", type = "quote")
public class QuotesDTO extends Quote {

    @Id
    private String id;

    private String dayCode;
    private String fiftyTwoWkHighDate;
    private String fiftyTwoWkLowDate;
    private String flag;
    private String mode;
    private String name;
    private String serverTimestamp;
    private String symbol;
    private String tradeTimestamp;
    private String unitCode;

    private Double close;
    private Double dollarVolume;
    private Double fiftyTwoWkHigh;
    private Double fiftyTwoWkLow;
    private Double high;
    private Double last;
    private Double lastPrice;
    private Double low;
    private Double netChange;
    private Double open;
    private Double percentChange;
    private Double previousClose;
    private Double settlement;

    private Long numTrades;
    private Long previousVolume;
    private Long volume;

    @Field(type = FieldType.Object)
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
