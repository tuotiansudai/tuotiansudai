package com.tuotiansudai.paywrapper.service.impl;


import com.tuotiansudai.dto.AccountDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.MerSendSmsPwdMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerSendSmsPwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerSendSmsPwdResponseModel;
import com.tuotiansudai.paywrapper.service.ChangeUmpayPasswordService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangeUmpayPasswordServiceImpl implements ChangeUmpayPasswordService{

    static Logger logger = Logger.getLogger(ChangeUmpayPasswordServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private PaySyncClient paySyncClient;

    @Override
    public BaseDto<PayDataDto> changeUmpayPassword(AccountDto accountDto) {
        AccountModel accountModel = accountMapper.findByLoginName(accountDto.getLoginName());
        String orderId = String.valueOf(idGenerator.generate());
        MerSendSmsPwdRequestModel merSendSmsPwdRequestModel = new MerSendSmsPwdRequestModel(accountModel.getPayUserId(), accountModel.getIdentityNumber(), orderId);

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();

        try {
            MerSendSmsPwdResponseModel responseModel = paySyncClient.send(MerSendSmsPwdMapper.class, merSendSmsPwdRequestModel, MerSendSmsPwdResponseModel.class);
            dataDto.setStatus(responseModel.isSuccess());
            dataDto.setCode(responseModel.getRetCode());
            dataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            dataDto.setMessage(e.getMessage());
        }
        baseDto.setData(dataDto);
        return baseDto;
    }
}
