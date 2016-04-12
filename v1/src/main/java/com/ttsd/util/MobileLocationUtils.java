package com.ttsd.util;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class MobileLocationUtils {
    enum Provider {
        BaiduApiStore,
        Taobao,
        PaiPai,
        Baifubao,
        Shouji360
    }

    private static Log log = LogFactory.getLog(MobileLocationUtils.class);

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final Provider[] PROVIDER_PRIORITY = new Provider[]{
            Provider.Taobao,
            Provider.Shouji360,
            Provider.Baifubao,
            Provider.PaiPai,
            Provider.BaiduApiStore
    };

    public static String locateMobileNumber(String phoneNumber) {
        for (Provider p: PROVIDER_PRIORITY){
            try{
                String location = locateMobileNumber(phoneNumber,p);
                return location;
            }catch (Exception e){
                log.warn("通过"+p+"获取手机位置失败",e);
            }
        }
        return "";
    }

    private static String locateMobileNumber(String phoneNumber, Provider provider) {
        switch (provider) {
            case BaiduApiStore:
                return locateMobileNumberBaiduApiStore(phoneNumber);
            case Taobao:
                return locateMobileNumberTaobao(phoneNumber);
            case PaiPai:
                return locateMobileNumberPaiPai(phoneNumber);
            case Baifubao:
                return locateMobileNumberBaifubao(phoneNumber);
            case Shouji360:
                return locateMobileNumber360(phoneNumber);
        }
        return "";
    }

    private static String locateMobileNumberBaiduApiStore(String phoneNumber) {
        // http://apistore.baidu.com/apiworks/servicedetail/117.html
        String httpUrl = "http://apis.baidu.com/apistore/mobilephoneservice/mobilephone";
        String apiKey = "21a66ace9b48be42ed199e8d58a70c91";
        Map<String, String> queryParam = new HashMap<>(1);
        queryParam.put("tel", phoneNumber);

        Map<String, String> headerParam = new HashMap<>(1);
        headerParam.put("apikey", apiKey);

        String jsonResult = httpGet(httpUrl, queryParam, headerParam);
        if (StringUtils.isBlank(jsonResult)) {
            return "";
        }

        JSONObject json = JSONObject.fromObject(jsonResult);
        String province = "";
        int errNum = json.getInt("errNum");
        if (errNum == 0) {
            JSONObject retData = json.getJSONObject("retData");
            province = retData.getString("province");
        }
        return province;
    }

    private static String locateMobileNumberTaobao(String phoneNumber) {
        // http://www.lai18.com/content/370817.html
        // http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=15850781443
        String httpUrl = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
        Map<String, String> queryParam = new HashMap<>(1);
        queryParam.put("tel", phoneNumber);
        String jsonResult = httpGet(httpUrl, queryParam, "GBK");
        if (StringUtils.isBlank(jsonResult)) {
            return "";
        }

        jsonResult = jsonResult.replace("__GetZoneResult_ =", "");
        JSONObject json = JSONObject.fromObject(jsonResult);
        String province = json.getString("province");
        return province;
    }

    private static String locateMobileNumberPaiPai(String phoneNumber) {
        // http://www.lai18.com/content/370817.html
        // http://virtual.paipai.com/extinfo/GetMobileProductInfo?mobile=15850781443&amount=10000&callname=getPhoneNumInfoExtCallback
        String httpUrl = "http://virtual.paipai.com/extinfo/GetMobileProductInfo";
        Map<String, String> queryParam = new HashMap<>(3);
        queryParam.put("mobile", phoneNumber);
        queryParam.put("amount", "10000");
        queryParam.put("callname", "T");
        String jsonResult = httpGet(httpUrl, queryParam, "gb2312");
        if (StringUtils.isBlank(jsonResult)) {
            return "";
        }
        jsonResult = jsonResult.split(";")[0];
        jsonResult = jsonResult.substring(2, jsonResult.length() - 1);
        JSONObject json = JSONObject.fromObject(jsonResult);
        String province = json.getString("province");
        return province;
    }

    private static String locateMobileNumberBaifubao(String phoneNumber) {
        // http://www.lai18.com/content/370817.html
        // https://www.baifubao.com/callback?cmd=1059&callback=phone&phone=15850781443

        String httpUrl = "https://www.baifubao.com/callback";
        Map<String, String> queryParam = new HashMap<>(3);
        queryParam.put("phone", phoneNumber);
        queryParam.put("callback", "T");
        queryParam.put("cmd", "1059");
        String jsonResult = httpGet(httpUrl, queryParam, "gb2312");
        if (StringUtils.isBlank(jsonResult)) {
            return "";
        }
        String[] jsonResultParts = jsonResult.trim().split("T\\(");
        if (jsonResultParts.length < 2) {
            return "";
        }
        jsonResult = jsonResultParts[1];
        jsonResult = jsonResult.substring(0, jsonResult.length() - 1);
        try {
            JSONObject json = JSONObject.fromObject(jsonResult);
            JSONObject data = json.getJSONObject("data");
            String province = data.getString("area");
            return province;
        } catch (Exception e) {
            return "";
        }
    }

    private static String locateMobileNumber360(String phoneNumber) {
        // http://cx.shouji.360.cn/phonearea.php?number=13674847382
        String httpUrl = "http://cx.shouji.360.cn/phonearea.php";
        Map<String, String> queryParam = new HashMap<>(1);
        queryParam.put("number", phoneNumber);
        String jsonResult = httpGet(httpUrl, queryParam);
        if (StringUtils.isBlank(jsonResult)) {
            return "";
        }
        JSONObject json = JSONObject.fromObject(jsonResult);
        JSONObject data = json.getJSONObject("data");
        String province = data.getString("province");
        return province;
    }

    private static String buildRequestQueryString(Map<String, String> params) {
        List<String> paramList = new ArrayList<>();
        for (String k : params.keySet()) {
            try {
                paramList.add(k + "=" + URLEncoder.encode(params.get(k), DEFAULT_CHARSET));
            } catch (UnsupportedEncodingException e) {
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

        BufferedReader reader = null;
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

            StringBuffer sbf = new StringBuffer();


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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

