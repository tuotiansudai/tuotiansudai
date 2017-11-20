package com.tuotiansudai.spring;

import com.tuotiansudai.util.ETCDConfigReader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.text.MessageFormat;
import java.util.Properties;

public class ETCDPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private final static Logger logger = Logger.getLogger(ETCDPropertyPlaceholderConfigurer.class);

    @Override
    protected String resolvePlaceholder(String placeholder, Properties props) {
        super.resolvePlaceholder(placeholder, props);
        String value = ETCDConfigReader.getValue(placeholder);

        logger.info(MessageFormat.format("{0}={1}", placeholder, value));

        return value;
    }
}
