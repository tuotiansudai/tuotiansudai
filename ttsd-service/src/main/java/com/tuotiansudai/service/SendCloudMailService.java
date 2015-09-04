package com.tuotiansudai.service;

public interface SendCloudMailService {


    boolean sendMail(String toAddress, String title,
                     String content);
}
