package com.tuotiansudai.smswrapper.client;

import com.google.common.base.Strings;
import com.squareup.okhttp.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;

@Service
public class SmsClient {

    private static String REQUEST_BODY_TEMPLATE = "sn={0}&pwd={1}&mobile={2}&content={3}&ext=&stime=&rrid=";

    private static MediaType MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    @Value("${zucp_url}")
    private String url;

    @Value("${zucp_sn}")
    private String sn;

    @Value("${zucp_password}")
    private String password;

    @Autowired
    private OkHttpClient httpClient;

    public String sendSMS(String mobile, String content){
        String requestBody = this.generateRequestBody(mobile, content);

        if (!Strings.isNullOrEmpty(requestBody)) {
            RequestBody okRequestBody = RequestBody.create(MEDIA_TYPE, requestBody);
            Request request = new Request.Builder()
                    .url(this.url)
                    .post(okRequestBody)
                    .build();
            try {
                Response response = httpClient.newCall(request).execute();
                String responseBody = response.body().string();
                return this.parseResponse(responseBody);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String generateRequestBody(String mobile, String content) {
        String md5Password = this.getMD5();
        if (Strings.isNullOrEmpty(md5Password) || Strings.isNullOrEmpty(mobile) || Strings.isNullOrEmpty(content)) {
            return null;
        }
        return MessageFormat.format(REQUEST_BODY_TEMPLATE, this.sn, md5Password, mobile, content);
    }

    private String getMD5() {
        String sourceStr = this.sn + this.password;
        String resultStr = "";
        try {
            byte[] temp = sourceStr.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(temp);
            // resultStr = new String(md5.digest());
            byte[] b = md5.digest();
            for (byte aB : b) {
                char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                        '9', 'A', 'B', 'C', 'D', 'E', 'F'};
                char[] ob = new char[2];
                ob[0] = digit[(aB >>> 4) & 0X0F];
                ob[1] = digit[aB & 0X0F];
                resultStr += new String(ob);
            }
            return resultStr;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String parseResponse(String responseBody) {
        try {
            Document document = DocumentHelper.parseText(responseBody);
            Element rootElement = document.getRootElement();
            return rootElement.getText();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
