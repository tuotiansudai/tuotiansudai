package com.tuotiansudai.fudian.util;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.regex.Pattern;

public class AmountUtils {

    private static Logger logger = LoggerFactory.getLogger(BankClient.class);

    public static String toYuan(String centString) {
        return toYuan(Long.parseLong(centString));
    }

    public static String toYuan(long cent) {
        try {
            long yuan = cent / 100;
            long fen = cent % 100;
            return MessageFormat.format("{0}.{1}", String.valueOf(yuan), String.format("%02d", fen));
        } catch (NumberFormatException e) {
            logger.error("cent convert error, cent: {}", cent);
        }

        return "0";
    }

    public static long toCent(String yuan) {
        if (!Pattern.matches("^\\d+\\.\\d{2}$", yuan)) {
            return 0L;
        }

        String[] split = yuan.split("\\.");
        return Long.parseLong(split[0]) * 100 + Long.parseLong(split[1]);
    }



}
