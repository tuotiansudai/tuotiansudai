package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRechargeService {

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();


}
