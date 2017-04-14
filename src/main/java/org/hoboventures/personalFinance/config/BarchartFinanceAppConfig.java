package org.hoboventures.personalFinance.config;

import com.barchart.ondemand.BarchartOnDemandClient;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.node.NodeBuilder;
import org.hoboventures.personalFinance.domain.HistoryDTO;
import org.hoboventures.personalFinance.domain.LeadersDTO;
import org.hoboventures.personalFinance.domain.QuotesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.UUID;

/**
 * Created by Asha on 12/2/2016.
 */
@Configuration
public class BarchartFinanceAppConfig {

    private static final Logger logger = LoggerFactory.getLogger(BarchartFinanceAppConfig.class);
    private static final String BARCHART_API_KEY = "b8276c101658e7a275b6b8456a6d085c";
    private static final String BARCHART_MARKETDATA_URL = "http://marketdata.websol.barchart.com/" ;

    @Bean
    public JavaMailSender mailSender(){
        return new JavaMailSenderImpl();
    }

    @Bean(name = "customAPIKey")
    //@Qualifier(value = "customAPIKey")
    public BarchartOnDemandClient barchartOnDemandClient(){
        /* create the onDemand client with your API key */
        return new BarchartOnDemandClient.Builder().debug(true)
                .baseUrl(BARCHART_MARKETDATA_URL).apiKey(BARCHART_API_KEY).build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.hoboventures.personalFinance"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    private static NodeClient getNodeClient() {
        return (NodeClient) nodeBuilder().clusterName(UUID.randomUUID().toString()).local(true).node()
                .client();
    }

    private static NodeBuilder nodeBuilder() {
        return new NodeBuilder();
    }

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate() {
        ElasticsearchTemplate elasticsearchTemplate = new ElasticsearchTemplate(getNodeClient());
        elasticsearchTemplate.createIndex(HistoryDTO.class);
        elasticsearchTemplate.createIndex(LeadersDTO.class);
        elasticsearchTemplate.createIndex(QuotesDTO.class);
        return elasticsearchTemplate;
    }

}
