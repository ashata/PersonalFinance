package org.hoboventures.personalFinance.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by Asha on 2/15/2017.
 */
@Component
public class EnvironmentUtil {

    @Autowired private Environment env;

    public String getValue(String name) {
        String value = env.getProperty(name);
        return value;
    }

    public int getIntegerValue(String name) {
        return Integer.getInteger(getValue(name));
    }
}
