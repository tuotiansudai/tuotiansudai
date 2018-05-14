package com.tuotiansudai.fudian.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class AmountUtils {

    private static Logger logger = LoggerFactory.getLogger(BankClient.class);

    public static String toAmount(String centString) {
        try {
            long cent = Long.parseLong(centString);
            long yuan = cent / 100;
            long fen = cent % 100;
            return MessageFormat.format("{0}.{1}", String.valueOf(yuan), String.format("%02d", fen));
        } catch (NumberFormatException e) {
            logger.error("cent convert error, cent: {}", centString);
        }

        return "0";
    }
}
