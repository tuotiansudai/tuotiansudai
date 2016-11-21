package com.tuotiansudai.cfca.constant;

import java.util.Arrays;
import java.util.Locale;

public class SystemConst {
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final Locale DEFAULT_LOCALE = Locale.CHINA;

    private static final String[] SORT = {Locale.CHINA.toString()};

    static {
        Arrays.sort(SORT);
    }

    public static boolean isSupportedLocale(String locale) {
        return (Arrays.binarySearch(SORT, locale) >= 0);
    }
}
