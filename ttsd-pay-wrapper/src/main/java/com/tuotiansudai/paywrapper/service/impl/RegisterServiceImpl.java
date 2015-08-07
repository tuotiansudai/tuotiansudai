package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.paywrapper.client.PayClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.MerRegisterPersonMapper;
import com.tuotiansudai.paywrapper.repository.model.request.MerRegisterPersonRequestModel;
import com.tuotiansudai.paywrapper.repository.model.response.MerRegisterPersonResponseModel;
import com.tuotiansudai.paywrapper.service.RegisterService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class RegisterServiceImpl implements RegisterService {

    static Logger logger = Logger.getLogger(RegisterServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PayClient payClient;

    @Transactional
    public BaseDto register(RegisterAccountDto dto) {
        MerRegisterPersonRequestModel requestModel = new MerRegisterPersonRequestModel(dto.getLoginName(),
                dto.getUserName(),
                dto.getIdentityNumber(),
                dto.getMobile());

        BaseDto<PayDataDto> baseDto = new BaseDto();
        PayDataDto dataDto = new PayDataDto();

        try {
            MerRegisterPersonResponseModel responseModel = payClient.send(MerRegisterPersonMapper.class,
                    requestModel,
                    MerRegisterPersonResponseModel.class);

            UserModel userModel = userMapper.findByLoginName(dto.getLoginName());

            if (responseModel.isSuccess()) {
                AccountModel accountModel = new AccountModel(userModel.getLoginName(),
                        dto.getUserName(),
                        dto.getIdentityNumber(),
                        responseModel.getUmpUserId(),
                        responseModel.getUmpAccountId(),
                        new Date());
                accountMapper.create(accountModel);
            }

            dataDto.setStatus(responseModel.isSuccess());
            dataDto.setCode(responseModel.getReturnCode());
            dataDto.setMessage(responseModel.getReturnMessage());
        } catch (PayException e) {
            dataDto.setStatus(false);
        }
        baseDto.setData(dataDto);
        return baseDto;
    }
}
