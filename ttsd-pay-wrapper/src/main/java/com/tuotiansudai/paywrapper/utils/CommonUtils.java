package com.tuotiansudai.paywrapper.utils;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommonUtils {
    static Logger logger = Logger.getLogger(CommonUtils.class);

    public static Map<String, String> mapToFormData(Map<String, String> map, boolean isURLEncoder) {
        Map<String, String> mapCopy = null;
        try {
            if (isURLEncoder) {
                mapCopy = new HashMap<>();
                Iterator iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry) iterator.next();
                    String encodeValue = null;
                    encodeValue = URLEncoder.encode(entry.getValue(), "UTF-8");
                    mapCopy.put(entry.getKey(), encodeValue);

                }
            } else {
                mapCopy = map;
            }

        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(),e);
            return map;
        }
//        String formData = "";
//        if (mapCopy != null && mapCopy.size() > 0) {
//            formData = Joiner.on("&").withKeyValueSeparator("=").join(mapCopy);
//        }
        return mapCopy;
    }

}
