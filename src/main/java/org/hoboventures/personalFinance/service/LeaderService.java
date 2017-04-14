package org.hoboventures.personalFinance.service;

import com.barchart.ondemand.api.responses.Leaders;

/**
 * Created by Asha on 2/15/2017.
 */
public interface LeaderService extends GenericBarchartService {

    Leaders leaders();
}
