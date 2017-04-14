package org.hoboventures.personalFinance.service.impl;

import com.barchart.ondemand.BarchartOnDemandClient;
import com.barchart.ondemand.api.HistoryRequest;
import com.barchart.ondemand.api.LeadersRequest;
import com.barchart.ondemand.api.OnDemandRequest;
import com.barchart.ondemand.api.QuoteRequest;
import com.barchart.ondemand.api.responses.*;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hoboventures.personalFinance.dao.LeaderRepository;
import org.hoboventures.personalFinance.domain.LeadersDTO;
import org.hoboventures.personalFinance.service.GenericBarchartService;
import org.hoboventures.personalFinance.service.LeaderService;
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
import java.util.Arrays;
import java.util.List;

/**
 * Created by Asha on 2/15/2017.
 */
@Service
public class LeaderServiceImpl implements LeaderService {

    private static final Logger logger = LoggerFactory.getLogger(LeaderServiceImpl.class);

    @Qualifier("customAPIKey")
    @Autowired private BarchartOnDemandClient onDemandClient;
    @Autowired private EnvironmentUtil environmentUtil;
    @Autowired private LeaderRepository leaderRepository;

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
                LeadersDTO leaderDTO = new LeadersDTO();
                BeanUtils.copyProperties(leader, leaderDTO);
                leaderRepository.save(leaderDTO);
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
    public BarchartOnDemandClient getOnDemandClient() {
        return onDemandClient;
    }

    @Override
    public EnvironmentUtil getEnvironmentUtil() {
        return environmentUtil;
    }
}
