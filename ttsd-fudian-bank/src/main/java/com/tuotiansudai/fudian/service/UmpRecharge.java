package com.tuotiansudai.fudian.service;

import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.fudian.umpClient.PayAsyncClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UmpRecharge {

    private final PayAsyncClient payAsyncClient;

    @Autowired
    private UmpRecharge(PayAsyncClient payAsyncClient){
        this.payAsyncClient = payAsyncClient;
    }

    public UmpAsyncMessage recharge(){

    }

}
