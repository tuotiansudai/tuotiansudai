package com.ttsd.push;

import com.google.common.collect.Sets;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;

public abstract class AndroidNotification extends UmengNotification {
    // Keys can be set in the payload level
    protected static final HashSet<String> PAYLOAD_KEYS = Sets.newHashSet("display_type");

    // Keys can be set in the body level
    protected static final HashSet<String> BODY_KEYS = Sets.newHashSet("ticker", "title", "text", "builder_id", "icon",
            "largeIcon", "img", "play_vibrate", "play_lights", "play_sound",
            "sound", "after_open", "url", "activity", "custom");

    // Set key/value in the rootJson, for the keys can be set please see ROOT_KEYS, PAYLOAD_KEYS,
    // BODY_KEYS and POLICY_KEYS.
    @Override
    public boolean setPredefinedKeyValue(String key, Object value) throws JSONException {
        if (ROOT_KEYS.contains(key)) {
            // This key should be in the root level
            rootJson.put(key, value);
        } else if (PAYLOAD_KEYS.contains(key)) {
            // This key should be in the payload level
            JSONObject payloadJson;
            if (rootJson.has("payload")) {
                payloadJson = rootJson.getJSONObject("payload");
            } else {
                payloadJson = new JSONObject();
                rootJson.put("payload", payloadJson);
            }
            payloadJson.put(key, value);
        } else if (BODY_KEYS.contains(key)) {
            // This key should be in the body level
            JSONObject bodyJson;
            JSONObject payloadJson;
            // 'body' is under 'payload', so build a payload if it doesn't exist
            if (rootJson.has("payload")) {
                payloadJson = rootJson.getJSONObject("payload");
            } else {
                payloadJson = new JSONObject();
                rootJson.put("payload", payloadJson);
            }
            // Get body JSONObject, generate one if not existed
            if (payloadJson.has("body")) {
                bodyJson = payloadJson.getJSONObject("body");
            } else {
                bodyJson = new JSONObject();
                payloadJson.put("body", bodyJson);
            }
            bodyJson.put(key, value);
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

    // Set extra key/value for Android notification
    public boolean setExtraField(String key, String value) throws JSONException {
        JSONObject payloadJson;
        JSONObject extraJson;
        if (rootJson.has("payload")) {
            payloadJson = rootJson.getJSONObject("payload");
        } else {
            payloadJson = new JSONObject();
            rootJson.put("payload", payloadJson);
        }

        if (payloadJson.has("extra")) {
            extraJson = payloadJson.getJSONObject("extra");
        } else {
            extraJson = new JSONObject();
            payloadJson.put("extra", extraJson);
        }
        extraJson.put(key, value);
        return true;
    }

}
