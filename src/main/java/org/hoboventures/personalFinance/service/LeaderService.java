package org.hoboventures.personalFinance.service;

import com.barchart.ondemand.api.responses.Leaders;
import org.springframework.stereotype.Service;

/**
 * Created by Asha on 2/15/2017.
 */
@Service
public interface LeaderService extends GenericBarchartService {

    Leaders leaders();
}
