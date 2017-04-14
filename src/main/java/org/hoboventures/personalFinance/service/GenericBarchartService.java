package org.hoboventures.personalFinance.service;

import com.barchart.ondemand.BarchartOnDemandClient;
import com.barchart.ondemand.api.HistoryRequest;
import com.barchart.ondemand.api.OnDemandRequest;
import com.barchart.ondemand.api.QuoteRequest;
import com.barchart.ondemand.api.responses.History;
import com.barchart.ondemand.api.responses.Leaders;
import com.barchart.ondemand.api.responses.Quotes;
import com.barchart.ondemand.api.responses.ResponseBase;
import org.apache.commons.lang.StringUtils;
import org.hoboventures.personalFinance.util.EnvironmentUtil;

import java.util.List;

/**
 * Created by Asha on 2/15/2017.
 */
public interface GenericBarchartService {

    BarchartOnDemandClient getOnDemandClient();
    EnvironmentUtil getEnvironmentUtil();

    default String[] getPortfolioSymbols() {
        String portfolioSymbols = "";
        for (FundTypes fundType : FundTypes.values()) {
            String fundValue = StringUtils.isBlank(portfolioSymbols) ? getEnvironmentUtil().getValue(fundType.name()) : "," + getEnvironmentUtil().getValue(fundType.name());
            portfolioSymbols = portfolioSymbols + fundValue;

        }
        String[] fundTypeSymbols = StringUtils.split(portfolioSymbols, ",");
        return fundTypeSymbols;
    }

    default <T extends ResponseBase> T getResponse(OnDemandRequest request, Class<T> type) throws Exception {
        T requestObject = null;
        if(ResponseBase.class.isAssignableFrom(type)) {
            requestObject = (T) getOnDemandClient().fetch(request);
        }
        return requestObject;
    }

    enum FundTypes{
        stocks,
        etfs,
        bonds,
        //mutual_funds
        ;
    }
}
