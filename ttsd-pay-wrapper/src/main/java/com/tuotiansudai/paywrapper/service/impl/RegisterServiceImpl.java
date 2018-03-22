package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.MerRegisterPersonMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerRegisterPersonRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerRegisterPersonResponseModel;
import com.tuotiansudai.paywrapper.service.RegisterService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {

    static Logger logger = Logger.getLogger(RegisterServiceImpl.class);

    private static final String REGISTER_ORDER_ID_TEMPLATE = "{0}X{1}";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    @Transactional
    public BaseDto<PayDataDto> register(RegisterAccountDto dto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();

        try {

            MerRegisterPersonRequestModel requestModel = new MerRegisterPersonRequestModel(
                    MessageFormat.format(REGISTER_ORDER_ID_TEMPLATE, String.valueOf(userMapper.findByLoginName(dto.getLoginName()).getId()), String.valueOf(new Date().getTime())),
                    dto.getLoginName(),
                    dto.getUserName(),
                    dto.getIdentityNumber(),
                    dto.getMobile());

            MerRegisterPersonResponseModel responseModel = paySyncClient.send(MerRegisterPersonMapper.class,
                    requestModel,
                    MerRegisterPersonResponseModel.class);

            if (responseModel.isSuccess()) {
                if (accountMapper.findByLoginName(dto.getLoginName()) == null) {
                    AccountModel accountModel = new AccountModel(dto.getLoginName(), responseModel.getUserId(), responseModel.getAccountId(), new Date());
                    accountMapper.create(accountModel);
                    userMapper.updateUserNameAndIdentityNumber(dto.getLoginName(), dto.getUserName(), dto.getIdentityNumber());
                    dataDto.setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder()
                            .put("payUserId", responseModel.getUserId())
                            .put("payAccountId", responseModel.getAccountId())
                            .build()));
                }

                List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(dto.getLoginName());
                if (userRoleModels.stream().noneMatch(userRoleModel -> userRoleModel.getRole() == Role.INVESTOR)) {
                    userRoleMapper.create(Lists.newArrayList(new UserRoleModel(dto.getLoginName(), Role.INVESTOR)));
                }
                mqWrapperClient.sendMessage(MessageQueue.AccountRegistered_CompletePointTask, dto.getLoginName());
            }

            dataDto.setStatus(responseModel.isSuccess());
            dataDto.setCode(responseModel.getRetCode());
            dataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            dataDto.setCode(e.getCode().getValue());
            dataDto.setMessage(e.getMessage());
        }
        baseDto.setData(dataDto);
        return baseDto;
    }
}
