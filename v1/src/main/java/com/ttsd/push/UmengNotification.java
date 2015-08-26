package com.ttsd.push;

import com.google.common.collect.Sets;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

public abstract class UmengNotification {
    // This JSONObject is used for constructing the whole request string.
    protected final JSONObject rootJson = new JSONObject();

    // This object is used for sending the post request to Umeng
    protected HttpClient client = HttpClientBuilder.create().build();

    // The host
    protected static final String host = "http://msg.umeng.com";

    // The upload path
    protected static final String uploadPath = "/upload";

    // The post path
    protected static final String postPath = "/api/send";

    // The app master secret
    protected String appMasterSecret;

    // The user agent
    protected final String USER_AGENT = "Mozilla/5.0";

    // Keys can be set in the root level
    protected static final HashSet<String> ROOT_KEYS = Sets.newHashSet("appkey", "timestamp", "type", "device_tokens",
            "alias", "alias_type", "file_id", "filter",
            "production_mode", "feedback", "description", "thirdparty_id");

    // Keys can be set in the policy level
    protected static final HashSet<String> POLICY_KEYS = Sets.newHashSet("start_time", "expire_time", "max_send_num");

    // Set predefined keys in the rootJson, for extra keys(Android) or customized keys(IOS) please
    // refer to corresponding methods in the subclass.
    public abstract boolean setPredefinedKeyValue(String key, Object value) throws Exception;

    public void setAppMasterSecret(String secret) {
        appMasterSecret = secret;
    }

    public boolean send() throws IOException {
        String url = host + postPath;
        String postBody = rootJson.toString();
        String sign = DigestUtils.md5Hex(("POST" + url + postBody + appMasterSecret).getBytes(StandardCharsets.UTF_8));
        url = url + "?sign=" + sign;
        HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", USER_AGENT);
        StringEntity se = new StringEntity(postBody, StandardCharsets.UTF_8);
        post.setEntity(se);
        // Send the post request and get the response
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
        if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
            System.out.println("Notification sent successfully.");
        } else {
            System.out.println("Failed to send the notification!");
        }
        return true;
    }
}
