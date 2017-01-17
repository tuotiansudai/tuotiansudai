package com.tuotiansudai.rest.ask.utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class SensitiveWordsFilter {

    static Logger logger = Logger.getLogger(SensitiveWordsFilter.class);

    private static String sensitiveLevelOneWords;
    private static String sensitiveLevelTwoWords;

    static {
        Properties config = new Properties();
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(SensitiveWordsFilter.class.getClassLoader().getResourceAsStream("sensitive.properties"), StandardCharsets.UTF_8.name());
            config.load(reader);
            sensitiveLevelOneWords = config.getProperty("levelOneWords");
            sensitiveLevelTwoWords = config.getProperty("levelTwoWords");
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

    public static String replace(String raw) {
//        return raw.replaceAll(sensitiveLevelOneWords,  "");
        return raw;
    }

    public static String filter(String raw) {
//        return raw.replaceAll(sensitiveLevelTwoWords,  "*");
        return raw;
    }

    public static boolean match(String raw) {
//        return !replace(raw).equals(raw);
        return false;
    }
}
