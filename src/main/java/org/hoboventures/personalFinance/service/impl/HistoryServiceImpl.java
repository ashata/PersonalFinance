package org.hoboventures.personalFinance.service.impl;

import com.barchart.ondemand.BarchartOnDemandClient;
import com.barchart.ondemand.api.HistoryRequest;
import com.barchart.ondemand.api.OnDemandRequest;
import com.barchart.ondemand.api.QuoteRequest;
import com.barchart.ondemand.api.responses.*;
import org.apache.commons.lang.StringUtils;
import org.hoboventures.personalFinance.dao.HistoryRepository;
import org.hoboventures.personalFinance.domain.HistoryDTO;
import org.hoboventures.personalFinance.service.GenericBarchartService;
import org.hoboventures.personalFinance.service.HistoryService;
import org.hoboventures.personalFinance.util.EnvironmentUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asha on 2/15/2017.
 */
@Service
public class HistoryServiceImpl implements HistoryService {

    private static final Logger logger = LoggerFactory.getLogger(HistoryServiceImpl.class);

    @Qualifier("customAPIKey")
    @Autowired private BarchartOnDemandClient onDemandClient;
    @Autowired private EnvironmentUtil environmentUtil;
    @Autowired private HistoryRepository historyRepository;

    @Override
    public History history(String symbol){
        if(StringUtils.isBlank(symbol)){
            symbol = "GOOGL";
        }
        final OnDemandRequest historyRequest = new HistoryRequest.Builder().symbol(symbol.toUpperCase()).dividends(true).type(HistoryRequest.HistoryRequestType.DAILY)
                .volume(HistoryRequest.HistoryRequestVolume.TOTAL).splits(true).start(DateTime.parse("2013-01-01")).build();
        History history = new History();
        try {
            history = getResponse(historyRequest, History.class);
            int count = 0;
            for (HistoryBar historyBar : history.all()) {
                logger.debug(count + " " + historyBar.toString());

                elasticFlushHistory(historyBar);

                count++;
            }
        } catch (Exception e) {
            logger.error("Exception fetching history ", e);
        }
        return history;
    }

    @Override
    public List<History> profilePortfolio(HistoryRequest.HistoryRequestType historyRequestType){
        List<History> portfolioHistory = new ArrayList<History>();
        for (FundTypes fundType : FundTypes.values()) {
            String fundValue = environmentUtil.getValue(fundType.name());
            String[] fundTypeSymbols = StringUtils.split(fundValue, ",");
            for (String symbol : fundTypeSymbols) {
                final OnDemandRequest historyRequest = new HistoryRequest.Builder().symbol(symbol.toUpperCase()).dividends(true).type(historyRequestType)
                        .volume(HistoryRequest.HistoryRequestVolume.TOTAL).splits(true).start(DateTime.parse("2000-01-01")).build();
                try {
                    History history = getResponse(historyRequest, History.class);
                    int count = 0;
                    for (HistoryBar historyBar : history.all()) {
                        logger.debug(count + " " + historyBar.toString());

                        elasticFlushHistory(historyBar);

                        count++;
                    }
                    portfolioHistory.add(history);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Exception fetching history ", e);
                }
            }
        }
        return portfolioHistory;
    }

    private void elasticFlushHistory(HistoryBar historyBar) {
        HistoryDTO historyDTO = new HistoryDTO();
        BeanUtils.copyProperties(historyBar, historyDTO);
        historyRepository.save(historyDTO);
    }

    @Override
    public BarchartOnDemandClient getOnDemandClient() {
        return onDemandClient;
    }

    @Override
    public EnvironmentUtil getEnvironmentUtil() {
        return environmentUtil;
    }
}
