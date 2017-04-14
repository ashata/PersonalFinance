package org.hoboventures.personalFinance.service;

import com.barchart.ondemand.api.QuoteRequest;
import com.barchart.ondemand.api.responses.Quotes;
import org.springframework.stereotype.Service;

/**
 * Created by Asha on 2/15/2017.
 */
@Service
public interface QuoteService extends GenericBarchartService {

    Quotes getQuote(QuoteRequest.QuoteRequestMode quoteMode, String... symbols);
}
