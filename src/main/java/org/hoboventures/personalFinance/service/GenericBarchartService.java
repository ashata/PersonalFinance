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

    default <T extends ResponseBase> T getResponse(BarchartOnDemandClient onDemandClient, OnDemandRequest request, Class<T> type) throws Exception {
        T requestObject = null;
        if(ResponseBase.class.isAssignableFrom(type)) {
            requestObject = (T) onDemandClient.fetch(request);
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
