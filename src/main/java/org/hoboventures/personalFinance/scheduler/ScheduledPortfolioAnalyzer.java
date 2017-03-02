package org.hoboventures.personalFinance.scheduler;

import com.barchart.ondemand.api.QuoteRequest;
import org.hoboventures.personalFinance.service.GenericBarchartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Asha on 2/18/2017.
 */
@Component
public class ScheduledPortfolioAnalyzer {

    @Autowired private GenericBarchartService barchartService;

    @Value("${interval_cron}")
    public String interval;
    @Value("${end_of_day_cron}")
    public String endofday;

    @Scheduled(cron = "${interval_cron}")
    protected void checkPortfolioIntervals(){
        barchartService.getQuote(QuoteRequest.QuoteRequestMode.REAL_TIME);
    }

    @Scheduled(cron = "${end_of_day_cron}")
    protected void checkPortfolioEndOfDay(){
        barchartService.getQuote(QuoteRequest.QuoteRequestMode.END_OF_DAY);
    }
}
