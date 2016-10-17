package com.tuotiansudai.client;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.MNSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MQClient {
    @Value("${mns.accesskeyid}")
    private String MNSAccessKeyId;
    @Value("${mns.accesskeysecret}")
    private String MNSAccessKeySecret;
    @Value("${mns.accountendpoint}")
    private String MNSAccountEndpoint;

    private static MNSClient client;


    public MNSClient getMnsClient() {
        if(client == null){
            CloudAccount account = new CloudAccount(MNSAccessKeyId, MNSAccessKeySecret, MNSAccountEndpoint);
            return account.getMNSClient();
        }
        return client;

    }

}
