package com.tuotiansudai.service;

public interface BindEmailService {

    boolean sendActiveEmail(String loginName, String email, String url);

    String verifyEmail(String loginName, String uuid, String ip, String platform, String deviceId);

}
