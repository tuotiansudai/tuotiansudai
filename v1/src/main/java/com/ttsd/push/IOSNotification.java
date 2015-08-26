package com.ttsd.push;

import com.google.common.collect.Sets;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

public abstract class IOSNotification extends UmengNotification {

    // Keys can be set in the aps level
    protected static final HashSet<String> APS_KEYS = Sets.newHashSet("alert", "badge", "sound", "content-available");

    @Override
    public boolean setPredefinedKeyValue(String key, Object value) throws JSONException {
        if (ROOT_KEYS.contains(key)) {
            // This key should be in the root level
            rootJson.put(key, value);
        } else if (APS_KEYS.contains(key)) {
            // This key should be in the aps level
            JSONObject apsJson;
            JSONObject payloadJson;
            if (rootJson.has("payload")) {
                payloadJson = rootJson.getJSONObject("payload");
            } else {
                payloadJson = new JSONObject();
                rootJson.put("payload", payloadJson);
            }
            if (payloadJson.has("aps")) {
                apsJson = payloadJson.getJSONObject("aps");
            } else {
                apsJson = new JSONObject();
                payloadJson.put("aps", apsJson);
            }
            apsJson.put(key, value);
        } else if (POLICY_KEYS.contains(key)) {
            // This key should be in the body level
            JSONObject policyJson;
            if (rootJson.has("policy")) {
                policyJson = rootJson.getJSONObject("policy");
            } else {
                policyJson = new JSONObject();
                rootJson.put("policy", policyJson);
            }
            policyJson.put(key, value);
        }

        return true;
    }

    // Set customized key/value for IOS notification
    public boolean setCustomizedField(String key, String value) throws JSONException {
        //rootJson.put(key, value);
        JSONObject payloadJson;
        if (rootJson.has("payload")) {
            payloadJson = rootJson.getJSONObject("payload");
        } else {
            payloadJson = new JSONObject();
            rootJson.put("payload", payloadJson);
        }
        payloadJson.put(key, value);
        return true;
    }

}
