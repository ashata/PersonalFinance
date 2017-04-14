package org.hoboventures.personalFinance.service;

import com.barchart.ondemand.api.HistoryRequest;
import com.barchart.ondemand.api.responses.History;

import java.util.List;

/**
 * Created by Asha on 2/15/2017.
 */
public interface HistoryService extends GenericBarchartService {

    History history(String symbol);

    List<History> profilePortfolio(HistoryRequest.HistoryRequestType historyRequestType);
}
