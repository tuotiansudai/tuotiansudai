package com.tuotiansudai.util;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobileLocationUtils {
    enum Provider {
        Shouji360,
        JuHe
    }

    private static Logger log = Logger.getLogger(MobileLocationUtils.class);
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final Provider[] PROVIDER_PRIORITY = new Provider[]{
            Provider.Shouji360,
            Provider.JuHe,
    };

    public static String[] locateMobileNumber(String phoneNumber) {
        for (Provider p : PROVIDER_PRIORITY) {
            try {
                String[] location = locateMobileNumber(phoneNumber, p);
                if (StringUtils.isNotEmpty(location[0])) {
                    return location;
                }
            } catch (Exception e) {
                log.debug("通过" + p + "获取手机位置失败", e);
            }
        }
        return new String[]{"", ""};
    }

    private static String[] locateMobileNumber(String phoneNumber, Provider provider) {
        switch (provider) {
            case Shouji360:
                return locateMobileNumber360(phoneNumber);
            case JuHe:
                return locateMobileNumberJuHe(phoneNumber);
        }
        return new String[]{"", ""};
    }

    private static String[] locateMobileNumber360(String phoneNumber) {
        // https://cx.shouji.360.cn/phonearea.php?number=13674847382
        String[] provinceAndCity = {"", ""};
        String httpUrl = "https://cx.shouji.360.cn/phonearea.php";
        Map<String, String> queryParam = new HashMap<>(1);
        queryParam.put("number", phoneNumber);
        String jsonResult = httpGet(httpUrl, queryParam, "gb2312");
        if (StringUtils.isBlank(jsonResult)) {
            return provinceAndCity;
        }
        JSONObject json = JSONObject.fromObject(jsonResult);
        JSONObject data = json.getJSONObject("data");
        String province = data.getString("province");
        String city = data.getString("city");
        provinceAndCity[0] = unifyProvince(province);
        if (StringUtils.isEmpty(city)) {
            city = provinceAndCity[0];
        }
        provinceAndCity[1] = city;
        return provinceAndCity;
    }

    private static String[] locateMobileNumberJuHe(String phoneNumber) {
        //http://apis.juhe.cn/mobile/get?phone=13674847382&key=ae009facc22347bc29af361b2592bdfd
        String[] provinceAndCity = {"", ""};
        String httpUrl = "http://apis.juhe.cn/mobile/get";
        Map<String, String> queryParam = new HashMap<>(1);
        queryParam.put("phone", phoneNumber);
        queryParam.put("key", "ae009facc22347bc29af361b2592bdfd");
        String jsonResult = httpGet(httpUrl, queryParam);
        if (StringUtils.isBlank(jsonResult)) {
            return provinceAndCity;
        }
        JSONObject json = JSONObject.fromObject(jsonResult);
        JSONObject data = json.getJSONObject("result");
        String province = "";
        String city = "";

        if (data.size() > 0) {
            province = data.getString("province");
            city = data.getString("city");
        }
        provinceAndCity[0] = unifyProvince(province);
        if (StringUtils.isEmpty(city)) {
            city = provinceAndCity[0];
        }
        provinceAndCity[1] = city;

        return provinceAndCity;
    }

    private static String buildRequestQueryString(Map<String, String> params) {
        List<String> paramList = new ArrayList<>();
        for (String k : params.keySet()) {
            try {
                paramList.add(k + "=" + URLEncoder.encode(params.get(k), DEFAULT_CHARSET));
            } catch (UnsupportedEncodingException ignored) {
            }
        }
        return StringUtils.join(paramList, "&");
    }

    private static String httpGet(String httpUrl, Map<String, String> queryParams) {
        return httpGet(httpUrl, queryParams, null, null);
    }

    private static String httpGet(String httpUrl, Map<String, String> queryParams, String charset) {
        return httpGet(httpUrl, queryParams, null, charset);
    }

    private static String httpGet(String httpUrl, Map<String, String> queryParams, Map<String, String> headerParams) {
        return httpGet(httpUrl, queryParams, headerParams, null);
    }

    private static String httpGet(String httpUrl, Map<String, String> queryParams, Map<String, String> headerParams, String charset) {
        String queryString = buildRequestQueryString(queryParams);
        if (StringUtils.isNotBlank(queryString)) {
            httpUrl = httpUrl + "?" + queryString;
        }

        BufferedReader reader;
        String result = "";
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // set header
            if (headerParams != null && headerParams.size() > 0) {
                for (String k : headerParams.keySet()) {
                    connection.setRequestProperty(k, headerParams.get(k));
                }
            }

            connection.connect();
            InputStream is = connection.getInputStream();

            StringBuilder sbf = new StringBuilder();


            if (StringUtils.isBlank(charset)) {
                charset = DEFAULT_CHARSET;
            }

            reader = new BufferedReader(new InputStreamReader(is, charset));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String unifyProvince(String province) {
        if (province == null) province = "";

        if (province.contains("省")) {
            province = province.substring(0, province.indexOf("省"));
        } else if (province.contains("市")) {
            province = province.substring(0, province.indexOf("市"));
        } else if (province.contains("内蒙古")) {
            province = "内蒙古";
        } else if (province.contains("广西")) {
            province = "广西";
        } else if (province.contains("西藏")) {
            province = "西藏";
        } else if (province.contains("宁夏")) {
            province = "宁夏";
        } else if (province.contains("新疆")) {
            province = "新疆";
        } else if (province.contains("香港")) {
            province = "香港";
        } else if (province.contains("澳门")) {
            province = "澳门";
        }
        return province;
    }
}

