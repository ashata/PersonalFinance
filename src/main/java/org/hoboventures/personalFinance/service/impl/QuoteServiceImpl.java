package org.hoboventures.personalFinance.service.impl;

import com.barchart.ondemand.BarchartOnDemandClient;
import com.barchart.ondemand.api.OnDemandRequest;
import com.barchart.ondemand.api.QuoteRequest;
import com.barchart.ondemand.api.responses.Quote;
import com.barchart.ondemand.api.responses.Quotes;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hoboventures.personalFinance.dao.QuoteRepository;
import org.hoboventures.personalFinance.domain.QuotesDTO;
import org.hoboventures.personalFinance.service.QuoteService;
import org.hoboventures.personalFinance.util.EnvironmentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by Asha on 2/15/2017.
 */
@Component
public class QuoteServiceImpl implements QuoteService {

    private static final Logger logger = LoggerFactory.getLogger(QuoteServiceImpl.class);

    @Qualifier("customAPIKey")
    @Autowired private BarchartOnDemandClient onDemandClient;
    @Autowired private EnvironmentUtil environmentUtil;
    @Autowired private QuoteRepository quoteRepository;

    @Override
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

                elasticFlushQuote(quote);

                count++;
            }
        } catch (Exception e) {
            logger.error("Exception fetching quote for symvbol(s): " + Arrays.toString(symbols), e);
        }
        return quotes;
    }

    private void elasticFlushQuote(Quote quote) {
        QuotesDTO quotesDTO = new QuotesDTO();
        BeanUtils.copyProperties(quote, quotesDTO);
        quoteRepository.save(quotesDTO);
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
