package org.hoboventures.personalFinance.domain;

import com.barchart.ondemand.api.responses.HistoryBar;
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
@Document(indexName = "history", type = "history")
@Data
@NoArgsConstructor
public class HistoryDTO extends HistoryBar {

    @Id
    private Long id;

    private String symbol;
    private String timestamp;
    private String tradingDay;

    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Integer volume;

    @Field(type = FieldType.Object)
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
