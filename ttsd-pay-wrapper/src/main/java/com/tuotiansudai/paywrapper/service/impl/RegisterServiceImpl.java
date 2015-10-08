package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.MerRegisterPersonMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerRegisterPersonRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerRegisterPersonResponseModel;
import com.tuotiansudai.paywrapper.service.RegisterService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {

    static Logger logger = Logger.getLogger(RegisterServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Transactional
    public BaseDto register(RegisterAccountDto dto) {
        MerRegisterPersonRequestModel requestModel = new MerRegisterPersonRequestModel(dto.getLoginName(),
                dto.getUserName(),
                dto.getIdentityNumber(),
                dto.getMobile());

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();

        try {
            MerRegisterPersonResponseModel responseModel = paySyncClient.send(MerRegisterPersonMapper.class,
                    requestModel,
                    MerRegisterPersonResponseModel.class);

            UserModel userModel = userMapper.findByLoginName(dto.getLoginName());

            if (responseModel.isSuccess()) {
                AccountModel accountModel = new AccountModel(userModel.getLoginName(),
                        dto.getUserName(),
                        dto.getIdentityNumber(),
                        responseModel.getUserId(),
                        responseModel.getAccountId(),
                        new Date());
                accountMapper.create(accountModel);
                UserRoleModel userRoleModel = new UserRoleModel();
                userRoleModel.setLoginName(dto.getLoginName());
                userRoleModel.setRole(Role.INVESTOR);
                userRoleMapper.create(userRoleModel);
            }

            dataDto.setStatus(responseModel.isSuccess());
            dataDto.setCode(responseModel.getRetCode());
            dataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            dataDto.setStatus(false);
        }
        baseDto.setData(dataDto);
        return baseDto;
    }

    @Transactional
    public BaseDto merRegisterPerson(RegisterAccountDto dto) {
        MerRegisterPersonRequestModel requestModel = new MerRegisterPersonRequestModel(dto.getLoginName(),
                dto.getUserName(),
                dto.getIdentityNumber(),
                dto.getMobile());

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();

        try {
            MerRegisterPersonResponseModel responseModel = paySyncClient.send(MerRegisterPersonMapper.class,
                    requestModel,
                    MerRegisterPersonResponseModel.class);
            dataDto.setStatus(responseModel.isSuccess());
            dataDto.setCode(responseModel.getRetCode());
            dataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            dataDto.setStatus(false);
            dataDto.setMessage(e.getMessage());
        }
        baseDto.setData(dataDto);
        return baseDto;
    }
}
