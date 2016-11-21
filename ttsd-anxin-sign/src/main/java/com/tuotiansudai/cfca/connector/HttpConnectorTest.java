package com.tuotiansudai.cfca.connector;

import com.tuotiansudai.cfca.constant.MIMEType;
import com.tuotiansudai.cfca.constant.Request;
import com.tuotiansudai.cfca.constant.SystemConst;
import com.tuotiansudai.cfca.util.CommonUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class HttpConnectorTest {

    public static String JKS_PATH = "/workspace/dev-config/anxinsign.jks";
    public static String JKS_PWD = "123abc";
    public static String ALIAS = "anxinsign";
    public String URL = "https://210.74.42.33:9443/FEP/";

    public int connectTimeout = 3000;
    public int readTimeout = 10000;
    public String channel = "Test";
    public boolean isSSL = true;
    public String keyStorePath = JKS_PATH;
    public String keyStorePassword = JKS_PWD;
    public String trustStorePath = JKS_PATH;
    public String trustStorePassword = JKS_PWD;

    private HttpClient httpClient;

    public void init() {
        httpClient = new HttpClient();
        httpClient.config.connectTimeout = connectTimeout;
        httpClient.config.readTimeout = readTimeout;
        httpClient.httpConfig.userAgent = "TrustSign FEP";
        httpClient.httpConfig.contentType = MIMEType.FORM;
        httpClient.httpConfig.accept = MIMEType.JSON;
        try {
            if (isSSL) {
                httpClient.initSSL(keyStorePath, keyStorePassword.toCharArray(), trustStorePath, trustStorePassword.toCharArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!URL.endsWith("/")) {
            URL += "/";
        }
    }

    public String post(String uri, String data, String signature) {
        return deal(uri, "POST", prepare(data, signature, null));
    }

    public String post(String uri, String data, String signature, Map<String, String> map) {
        return deal(uri, "POST", prepare(data, signature, map));
    }

    public String post(String uri, String data, String signature, File file) {
        return deal(uri, "POST", data, file, signature);
    }

    public byte[] getFile(String uri) {
        HttpURLConnection connection = null;
        try {
            connection = httpClient.connect(URL + uri, "GET");
            int responseCode = httpClient.send(connection, null);
            System.out.println("responseCode:" + responseCode);
            if (responseCode != 200) {
                System.out.println(CommonUtil.getString(httpClient.receive(connection)));
            }

            return httpClient.receive(connection);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            httpClient.disconnect(connection);
        }
    }

    private String prepare(String data, String signature, Map<String, String> map) {
        try {
            StringBuilder request = new StringBuilder();
            request.append(Request.CHANNEL).append("=").append(URLEncoder.encode(channel, SystemConst.DEFAULT_CHARSET));
            if (CommonUtil.isNotEmpty(data)) {
                request.append("&").append(Request.DATA).append("=").append(URLEncoder.encode(data, SystemConst.DEFAULT_CHARSET));
            }
            if (CommonUtil.isNotEmpty(signature)) {
                request.append("&").append(Request.SIGNATURE).append("=").append(URLEncoder.encode(signature, SystemConst.DEFAULT_CHARSET));
            }
            if (CommonUtil.isNotEmpty(map)) {
                for (Entry<String, String> pair : map.entrySet()) {
                    request.append("&").append(pair.getKey()).append("=")
                            .append(pair.getValue() == null ? "" : URLEncoder.encode(pair.getValue(), SystemConst.DEFAULT_CHARSET));
                }
            }
            return request.toString();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private String deal(String uri, String method, String request) {
        HttpURLConnection connection = null;
        try {
            connection = httpClient.connect(URL + uri, method);
            System.out.println(URL + uri);
            System.out.println(method);
            System.out.println(request);
            int responseCode = httpClient.send(connection, request == null ? null : CommonUtil.getBytes(request));
            System.out.println("responseCode:" + responseCode);
            return CommonUtil.getString(httpClient.receive(connection));
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            httpClient.disconnect(connection);
        }
    }

    private String deal(String uri, String method, String request, File file, String signature) {
        HttpURLConnection connection = null;
        try {
            connection = httpClient.connect(URL + uri, method);
            System.out.println(URL + uri);
            System.out.println(method);
            System.out.println(request);
            int responseCode = httpClient.send(connection, request == null ? null : CommonUtil.getBytes(request), file, signature);
            System.out.println("responseCode:" + responseCode);
            return CommonUtil.getString(httpClient.receive(connection));
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            httpClient.disconnect(connection);
        }
    }

}
