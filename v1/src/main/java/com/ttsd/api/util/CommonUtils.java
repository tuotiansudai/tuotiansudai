package com.ttsd.api.util;


import com.google.common.base.Joiner;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CommonUtils {

    public static String encryptUserName(String userName) {

        if (userName.length() > 3) {

            userName = userName.substring(0, 3) + "***";

        } else {

            userName += "***";

        }
        return userName;
    }

    public static String encryptBankCardNo(String cardNo) {
        if (StringUtils.isEmpty(cardNo)) {
            return "";
        }

        if (cardNo.length() > 4) {

            cardNo = cardNo.substring(0, 4) + "***" + cardNo.substring(cardNo.length() - 4);

        }
        return cardNo;
    }

    public static String convertRealMoneyByType(double money, String type) {
        if ("ti_balance".equals(type)) {
            return "+" + money;
        } else if ("to_balance".equals(type) || "to_frozen".equals(type)) {
            return "-" + money;
        }
        return "" + money;
    }

    public static String mapToFormData(Map<String, String> map, boolean isURLEncoder) throws UnsupportedEncodingException {
        String formData = "";
        if (map != null && map.size() > 0) {
            formData = Joiner.on("&").withKeyValueSeparator("=").join(map);
            if (isURLEncoder) {
                formData = URLEncoder.encode(formData, "UTF-8");
            }
        }
        return formData;
    }
}
