package com.esoft.jdp2p.message.service;

public interface SendCloudMailService {


    public boolean sendMailException(String toAddress, String title,
                                     String content);

    public boolean sendMail(String toAddress, String title,
                            String content);
}
