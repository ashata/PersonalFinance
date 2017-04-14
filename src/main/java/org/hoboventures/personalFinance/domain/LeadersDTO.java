package org.hoboventures.personalFinance.domain;

import com.barchart.ondemand.api.responses.Leader;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Document(indexName = "Leader", type = "leader")
@Data
@NoArgsConstructor
public class LeadersDTO {

    @Id
    private Long id;

    private String symbol;
    private String symbolName;
    private String exchange;
    private String country;
    private String sicSector;
    private String industry;
    private String subIndustry;
    private String timestamp;

    private Double lastPrice;
    private Double priceNetChange;
    private Double pricePercentChange;
    private Double previousClose;
    private Double standardDeviation;
    private Long volume;
    private Long previousVolume;

    @Field(type = FieldType.Object)
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
