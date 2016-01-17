package com.tuotiansudai.console.jpush.client;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;

public class MobileAppPushClientTest {

    public static void main(String[] args){
        String masterSecret = "7e85cd2928f860b70e8b3908";
        String appKey = "78d62eae2bba85e7c08ef7f5";
        JPushClient jPushClient = new JPushClient(masterSecret,appKey,3);

        // For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPushObject_all_all_alert();

        try {
            System.out.println("payload=======" + payload.toJSON());
            PushResult result = jPushClient.sendPush(payload);
            System.out.println("============" + result.isResultOK());
            System.out.println("============" + result.ERROR_MESSAGE_NONE);
            System.out.println("============" + result.ERROR_CODE_NONE);
            System.out.println("============" + result.ERROR_CODE_OK);
            System.out.println("============" + result.getOriginalContent());
            System.out.println("============" + result.getRateLimitQuota());
            System.out.println("============" + result.getRateLimitRemaining());
            System.out.println("============" + result.getRateLimitReset());
            System.out.println("========" + result.msg_id);
            System.out.println("===========" + result.sendno);


        } catch (APIConnectionException e) {
            // Connection error, should retry later

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            System.out.println("==========" + e.getMessage());
        }

    }

    public static PushPayload buildPushObject_all_all_alert() {
        return PushPayload.alertAll("mobile app test");
    }
}
