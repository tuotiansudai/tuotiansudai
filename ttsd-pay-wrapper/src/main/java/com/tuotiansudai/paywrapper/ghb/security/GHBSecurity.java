package com.tuotiansudai.paywrapper.ghb.security;

import com.tuotiansudai.paywrapper.ghb.security.enums.Priority;
import com.tuotiansudai.paywrapper.ghb.security.enums.RequestType;
import com.tuotiansudai.paywrapper.ghb.security.provider.MD5;
import com.tuotiansudai.paywrapper.ghb.security.provider.RSA;

public class GHBSecurity {
    public static String buildMessageHeader(RequestType requestType) {
        return buildMessageHeader(Priority.NORMAL, requestType);
    }

    public static String buildMessageHeader(Priority priority, RequestType requestType) {
        return String.format("001X%1d%1d          ", priority.getCode(), requestType.getCode());
    }

    public static String buildMessageDigest(String messageBody) {
        String md5 = MD5.hash(messageBody);
        String sign = RSA.sign(md5);
        return String.format("%08d%s", sign.length(), sign);
    }

    public static String buildMessage(String messageBody) {
        return buildMessage(RequestType.SYNC, messageBody);
    }

    public static String buildMessage(RequestType requestType, String messageBody) {
        return buildMessage(
                buildMessageHeader(requestType),
                buildMessageDigest(messageBody),
                messageBody
        );
    }

    public static String buildMessage(String messageHeader, String messageDigest, String messageBody) {
        return messageHeader + messageDigest + messageBody;
    }

    public static String decodeMessageBody(String message) {
        if (!message.startsWith("001X")) {
            throw new GHBSecurityException("包头校验失败, message:\r\n\r\n" + message + "\r\n\r\n");
        }
        int digestLength = Integer.parseInt(message.substring(16, 24).trim(), 10);
        String digest = message.substring(24, 24 + digestLength);
        String body = message.substring(24 + digestLength);
        String md5 = MD5.hash(body);
        if (RSA.verify(md5, digest)) {
            return body;
        } else {
            throw new GHBSecurityException("验签失败, message:\r\n\r\n" + message + "\r\n\r\n");
        }
    }
}
