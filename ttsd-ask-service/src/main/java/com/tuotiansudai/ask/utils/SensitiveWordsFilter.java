package com.tuotiansudai.ask.utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.regex.Pattern;

public class SensitiveWordsFilter {

    static Logger logger = Logger.getLogger(SensitiveWordsFilter.class);

    private static String sensitiveLevelOneWords;
    private static String sensitiveLevelTwoWords;
    private static String[] sensitiveWords;

    static {
        Properties config = new Properties();
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(SensitiveWordsFilter.class.getClassLoader().getResourceAsStream("sensitive.properties"), StandardCharsets.UTF_8.name());
            config.load(reader);
            sensitiveLevelOneWords = config.getProperty("levelOneWords");
            sensitiveLevelTwoWords = config.getProperty("levelTwoWords");
            sensitiveWords = config.getProperty("sensitiveWords").split("\\|");
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

    public static String matchSensitiveWords(String raw){
        for(String word : sensitiveWords){
            if (raw.contains(word)) {
                return word;
            }
        }
        return null;
    }
}
