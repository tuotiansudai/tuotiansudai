package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

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

    @Override
    @Transactional
    public BaseDto<PayDataDto> register(RegisterAccountDto dto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();

        try {
            UserModel userModel = userMapper.lockByLoginName(dto.getLoginName());

            MerRegisterPersonRequestModel requestModel = new MerRegisterPersonRequestModel(
                    MessageFormat.format(REGISTER_ORDER_ID_TEMPLATE, String.valueOf(userModel.getId()), String.valueOf(new Date().getTime())),
                    dto.getLoginName(),
                    dto.getUserName(),
                    dto.getIdentityNumber(),
                    dto.getMobile());

            MerRegisterPersonResponseModel responseModel = paySyncClient.send(MerRegisterPersonMapper.class,
                    requestModel,
                    MerRegisterPersonResponseModel.class);

            if (responseModel.isSuccess()) {
                if (accountMapper.findByLoginName(userModel.getLoginName()) == null) {
                    AccountModel accountModel = new AccountModel(userModel.getLoginName(),
                            dto.getUserName(),
                            dto.getIdentityNumber(),
                            responseModel.getUserId(),
                            responseModel.getAccountId(),
                            new Date());
                    accountMapper.create(accountModel);
                }
                if (!Iterators.tryFind(userRoleMapper.findByLoginName(userModel.getLoginName()).iterator(), new Predicate<UserRoleModel>() {
                    @Override
                    public boolean apply(UserRoleModel input) {
                        return input.getRole() == Role.INVESTOR;
                    }
                }).isPresent()) {
                    UserRoleModel userRoleModel = new UserRoleModel();
                    userRoleModel.setLoginName(dto.getLoginName());
                    userRoleModel.setRole(Role.INVESTOR);
                    userRoleMapper.create(Lists.newArrayList(userRoleModel));
                }
            }

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
