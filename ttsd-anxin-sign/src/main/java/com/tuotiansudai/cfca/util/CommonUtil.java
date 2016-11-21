package com.tuotiansudai.cfca.util;

import com.tuotiansudai.cfca.constant.SystemConst;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonUtil {
    private static final String MASK = "[^ -()]";
    private static final String MASK_CHAR = "*";
    private static final String LIKE_CHAR = "%";

    public static boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static boolean isNotEmpty(String str) {
        return (str != null && str.trim().length() != 0);
    }

    public static boolean isEmpty(String[] strs) {
        return (strs == null || strs.length == 0);
    }

    public static boolean isNotEmpty(String[] strs) {
        return (strs != null && strs.length != 0);
    }

    public static <T> boolean isEmpty(List<T> list) {
        return (list == null || list.isEmpty());
    }

    public static <T> boolean isNotEmpty(List<T> list) {
        return (list != null && !list.isEmpty());
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return (map == null || map.isEmpty());
    }

    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return (map != null && !map.isEmpty());
    }

    public static <K, V> List<V> mapToList(Map<K, V> map) {
        List<V> list = new ArrayList<V>();
        list.addAll(map.values());
        return list;
    }

    public static int booleanToInt(Boolean bool) {
        return bool == null ? 0 : (bool ? 1 : 0);
    }

    public static boolean intToBoolean(Integer i) {
        return i == null ? false : (i != 0 ? true : false);
    }

    public static Number nvl(Number obj, Number value) {
        if (obj == null) {
            return value;
        } else {
            return obj;
        }
    }

    public static String nvl(String str, String value) {
        if (isEmpty(str)) {
            return value;
        } else {
            return str;
        }
    }

    public static byte[] getBytes(String str) {
        try {
            return str.getBytes(SystemConst.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getString(byte[] bytes) {
        try {
            return new String(bytes, SystemConst.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String toString(String[] strings) {
        if (strings == null)
            return "";
        int iMax = strings.length - 1;
        if (iMax == -1)
            return "";

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(String.valueOf(strings[i]));
            if (i == iMax)
                return b.toString();
            b.append(";");
        }
    }

    public static String mask(String str, int begin, int end) {
        return str.substring(0, begin) + str.substring(begin, end).replaceAll(MASK, MASK_CHAR) + str.substring(end, str.length());
    }

    public static String getLikeStr(String str) {
        return LIKE_CHAR + str + LIKE_CHAR;
    }

    public static String formatSerialNo(String serialNo) {
        int length = serialNo.length();
        if (length >= 30 && length < 32) {
            char[] temp = new char[32];
            for (int i = 0; i < 32 - length; i++) {
                temp[i] = '0';
            }
            serialNo.getChars(0, length, temp, 32 - length);
            return new String(temp);
        } else {
            return serialNo;
        }
    }
}
