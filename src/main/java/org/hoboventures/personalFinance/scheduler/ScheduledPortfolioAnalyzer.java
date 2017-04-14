package org.hoboventures.personalFinance.scheduler;

import com.barchart.ondemand.api.HistoryRequest;
import com.barchart.ondemand.api.QuoteRequest;
import org.hoboventures.personalFinance.service.GenericBarchartService;
import org.hoboventures.personalFinance.service.HistoryService;
import org.hoboventures.personalFinance.service.LeaderService;
import org.hoboventures.personalFinance.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Asha on 2/18/2017.
 */
@Component
public class ScheduledPortfolioAnalyzer {

    @Autowired private HistoryService historyService;
    @Autowired private LeaderService leaderService;
    @Autowired private QuoteService quoteService;

    @Value("${interval_cron}")
    public String interval;
    @Value("${end_of_day_cron}")
    public String endofday;

    @Scheduled(cron = "${interval_cron}")
    protected void quotePortfolioInterval(){
        quoteService.getQuote(QuoteRequest.QuoteRequestMode.REAL_TIME);
    }

    @Scheduled(cron = "${end_of_day_cron}")
    protected void quotePortfolioEoD(){
        quoteService.getQuote(QuoteRequest.QuoteRequestMode.END_OF_DAY);
    }

    @Scheduled(cron = "${end_of_day_cron}")
    protected void profileHistoryEoD(){
        historyService.profilePortfolio(HistoryRequest.HistoryRequestType.DAILY);
    }

    @Scheduled(cron = "${end_of_day_cron}")
    protected void checkLeadersEoD(){
        leaderService.leaders();
    }
}
