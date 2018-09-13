package com.tuotiansudai.borrow.utils;


import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

public class CommonUtils {

    public static String mapToFormData(Map<String, String> map) throws UnsupportedEncodingException {
        Map<String, String> mapCopy = Maps.newHashMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String encodeValue = URLEncoder.encode(entry.getValue(), "UTF-8");
            mapCopy.put(entry.getKey(), encodeValue);
        }

        return Joiner.on("&").withKeyValueSeparator("=").join(mapCopy);
    }
}
