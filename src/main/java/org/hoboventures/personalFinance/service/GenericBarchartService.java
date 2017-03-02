package org.hoboventures.personalFinance.service;

import com.barchart.ondemand.api.HistoryRequest;
import com.barchart.ondemand.api.QuoteRequest;
import com.barchart.ondemand.api.responses.History;
import com.barchart.ondemand.api.responses.Leaders;
import com.barchart.ondemand.api.responses.Quotes;

import java.util.List;

/**
 * Created by Asha on 2/15/2017.
 */
public interface GenericBarchartService {

    Leaders leaders();

    History history(String symbol);

    List<History> profilePortfolio(HistoryRequest.HistoryRequestType historyRequestType);

    Quotes getQuote(QuoteRequest.QuoteRequestMode quoteMode, String... symbols);
}
