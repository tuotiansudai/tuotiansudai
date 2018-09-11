package com.tuotiansudai.ask.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.NumberRange;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

public class FakeMobileUtil {

    static Logger logger = Logger.getLogger(FakeMobileUtil.class);

    private static List<String> FAKE_MOBILES;

    private static List<String> CARRIERS = Lists.newArrayList("134","135","136","137","138","139","150","151","152","157","158","159","187","188","130","131","132","155","156","185","186","185","186","180","189");

    static {
        Properties config = new Properties();
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(FakeMobileUtil.class.getClassLoader().getResourceAsStream("fake-mobile.properties"), StandardCharsets.UTF_8.name());
            config.load(reader);
            FAKE_MOBILES = Lists.newArrayList(config.getProperty("fakeMobiles").split(",")) ;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    public static String generateFakeMobile(String mobile) {
        if (FAKE_MOBILES.contains(mobile)) {
            return  CARRIERS.get((int) (Math.random() * CARRIERS.size())) + RandomStringUtils.randomNumeric(8);
        }

        return null;
    }

    public static boolean mobileIsFakeMobile(String mobile){
        return FAKE_MOBILES.contains(mobile);
    }
}
