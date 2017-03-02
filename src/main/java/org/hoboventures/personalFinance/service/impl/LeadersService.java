package org.hoboventures.personalFinance.service.impl;

import com.barchart.ondemand.BarchartOnDemandClient;
import com.barchart.ondemand.api.HistoryRequest;
import com.barchart.ondemand.api.LeadersRequest;
import com.barchart.ondemand.api.OnDemandRequest;
import com.barchart.ondemand.api.QuoteRequest;
import com.barchart.ondemand.api.responses.*;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hoboventures.personalFinance.service.GenericBarchartService;
import org.hoboventures.personalFinance.util.EnvironmentUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Asha on 2/15/2017.
 */
@Component
public class LeadersService implements GenericBarchartService {

    private static final Logger logger = LoggerFactory.getLogger(LeadersService.class);

    @Qualifier("customAPIKey")
    @Autowired private BarchartOnDemandClient onDemandClient;
    @Autowired private EnvironmentUtil environmentUtil;

    @Override
    public Leaders leaders(){
		/* get active stocks on NYSE */
        final OnDemandRequest leadersRequest = new LeadersRequest.Builder().exchanges(new String[] {"NYSE"})
                .assetType(LeadersRequest.LeadersAssetType.STOCK).type(LeadersRequest.LeadersRequestType.ACTIVE).build();

        Leaders leaders = new Leaders();
        try {
            leaders = getResponse(leadersRequest, leaders.getClass());
            int count = 0;

            /* output the results */
            for (Leader leader : leaders.all()) {
                final String symbol = leader.getSymbol();
                final String name = leader.getSymbolName();
                final double last = leader.getLastPrice();
                logger.debug(count + " " + symbol + " (" + name + ") Last: " + last);

                count++;
            }
            /*save the results and update them at a later time */
            leaders.refresh();
        } catch (Exception e) {
            logger.error("Exception fetching leaders ", e);
        }
        return leaders;
    }

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

    public Quotes getQuote(QuoteRequest.QuoteRequestMode quoteMode, String... symbols) {
        Quotes quotes = null;
        if(ArrayUtils.isEmpty(symbols)){
            symbols = getPortfolioSymbols();
        }
        if(quoteMode == null){
            String quoteModeDefault = environmentUtil.getValue("quoteModeDefault");
            quoteMode = QuoteRequest.QuoteRequestMode.valueOf(StringUtils.upperCase(quoteModeDefault));
        }

        QuoteRequest.QuoteRequestField[] fields = QuoteRequest.QuoteRequestField.values();
        OnDemandRequest quoteRequest = new QuoteRequest.Builder().symbols(symbols).mode(quoteMode).fields(fields).build();
        try {
            quotes = getResponse(quoteRequest, Quotes.class);
            int count = 0;
            for (Quote quote : quotes.all()) {
                logger.debug(count + " " + quote.toString());
                count++;
            }
        } catch (Exception e) {
            logger.error("Exception fetching quote for symvbol(s): " + Arrays.toString(symbols), e);
        }
        return quotes;
    }

    private String[] getPortfolioSymbols() {
        String portfolioSymbols = "";
        for (FundTypes fundType : FundTypes.values()) {
            String fundValue = StringUtils.isBlank(portfolioSymbols) ? environmentUtil.getValue(fundType.name()) : "," + environmentUtil.getValue(fundType.name());
            portfolioSymbols = portfolioSymbols + fundValue;

        }
        String[] fundTypeSymbols = StringUtils.split(portfolioSymbols, ",");
        return fundTypeSymbols;
    }

    private <T extends ResponseBase> T getResponse(OnDemandRequest request, Class<T> type) throws Exception {
        T requestObject = null;
        if(ResponseBase.class.isAssignableFrom(type)) {
            requestObject = (T) onDemandClient.fetch(request);
        }
        return requestObject;
    }

    private enum FundTypes{
        stocks,
        etfs,
        bonds,
        //mutual_funds
        ;
    }
}
