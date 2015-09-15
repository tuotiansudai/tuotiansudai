package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.service.AgreementService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by Administrator on 2015/9/15.
 */
@Service
public class AgreementServiceImpl implements AgreementService{

    static Logger logger = Logger.getLogger(AgreementServiceImpl.class);

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> agreement(AgreementDto dto) {
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());

        return null;
    }

    @Override
    public String agreementCallback(Map<String, String> paramsMap, String queryString) {
        return null;
    }

}
