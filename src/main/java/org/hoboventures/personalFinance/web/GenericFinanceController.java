package org.hoboventures.personalFinance.web;

import com.barchart.ondemand.api.HistoryRequest;
import com.barchart.ondemand.api.QuoteRequest;
import com.barchart.ondemand.api.responses.History;
import com.barchart.ondemand.api.responses.Leaders;
import com.barchart.ondemand.api.responses.Quotes;
import org.apache.commons.lang.StringUtils;
import org.hoboventures.personalFinance.service.GenericBarchartService;
import org.hoboventures.personalFinance.service.HistoryService;
import org.hoboventures.personalFinance.service.LeaderService;
import org.hoboventures.personalFinance.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Asha on 12/2/2016.
 */
@RestController
public class GenericFinanceController {

    @Autowired private LeaderService leaderService;
    @Autowired private HistoryService historyService;
    @Autowired private QuoteService quoteService;

    @GetMapping(value = "history")
    public History history(){
        return historyService.history("");
    }

    @GetMapping(value = "history/{symbol}")
    public History historyBySymbol(@PathVariable(value = "symbol") String symbol){
        return historyService.history(symbol);
    }

    @GetMapping(value = "portfolio/{historyRequestType}")
    public List<History> portfolio(@PathVariable(value = "historyRequestType") String historyRequestType){
        return historyService.profilePortfolio(HistoryRequest.HistoryRequestType.valueOf(historyRequestType.toUpperCase()));
    }

    @GetMapping(value = "portfolio")
    public List<History> portfolio(){
        return portfolio("Daily");
    }

    @GetMapping(value = "quote")
    public Quotes portfolioQuote(){
        return quoteService.getQuote(null);
    }

    @GetMapping(value = "quote/{quoteMode}")
    public Quotes portfolioQuote(@PathVariable("quoteMode") String quoteMode){
        return getQuoteRequestMode(quoteMode, null);
    }

    @GetMapping(value = "quote/{symbol}/{quoteMode}")
    public Quotes quote(@PathVariable("symbol") String symbol, @PathVariable("quoteMode") String quoteMode){
        return getQuoteRequestMode(quoteMode, symbol);
    }

    @GetMapping(value = "leaders")
    public Leaders leaders(){
        return leaderService.leaders();
    }

    //symbol last parameter so it can be easily extended to String...
    private Quotes getQuoteRequestMode(String quoteMode, String symbol) {
        Quotes quotes;
        if(StringUtils.isBlank(quoteMode)){
            quotes = quoteService.getQuote(QuoteRequest.QuoteRequestMode.END_OF_DAY, symbol);
        }
        else{
            quotes = quoteService.getQuote(QuoteRequest.QuoteRequestMode.valueOf(quoteMode.toUpperCase()), symbol);
        }
        return quotes;
    }
}
