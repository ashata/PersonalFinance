package org.hoboventures.personalFinance.config;

import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Asha on 12/2/2016.
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@PropertySource("classpath:barchartFinance.properties")
@EnableScheduling
//@EnableTransactionManagement
//@EntityScan("org.hoboventures.org.hoboventures.personalFinance.domain")
@EnableElasticsearchRepositories("org.hoboventures.org.hoboventures.personalFinance.dao")
@ComponentScan({"org.hoboventures.personalFinance"})
@EnableMetrics
@EnableSwagger2
public class ApplicationConfig extends SpringBootServletInitializer {

    public static void main(String[] args){
        SpringApplication.run(ApplicationConfig.class, args);
    }

    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        //, BarchartFinanceAppConfig.class
        return builder.sources(ApplicationConfig.class, BarchartFinanceAppConfig.class).bannerMode(Banner.Mode.OFF);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        //, BarchartFinanceAppConfig.class
        setRegisterErrorPageFilter(false);
        return configureApplication(builder);
    }
}
