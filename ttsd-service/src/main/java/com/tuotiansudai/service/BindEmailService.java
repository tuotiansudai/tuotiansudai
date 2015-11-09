package com.tuotiansudai.service;

public interface BindEmailService {

    boolean sendActiveEmail(String email,String url);

    String verifyEmail(String uuid);

}
