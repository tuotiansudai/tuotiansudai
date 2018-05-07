package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.repository.mapper.UserBankCardMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserBankCardModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBindBankCardService {

    private final BankWrapperClient bankWrapperClient;

    private final UserBankCardMapper userBankCardMapper;

    private final UserOpLogService userOpLogService;

    private final UserMapper userMapper;

    @Autowired
    public UserBindBankCardService(BankWrapperClient bankWrapperClient, UserMapper userMapper, UserBankCardMapper userBankCardMapper, UserOpLogService userOpLogService) {
        this.bankWrapperClient = bankWrapperClient;
        this.userMapper = userMapper;
        this.userBankCardMapper = userBankCardMapper;
        this.userOpLogService = userOpLogService;
    }

    public UserBankCardModel findBankCard(String loginName) {
        return userBankCardMapper.findByLoginName(loginName);
    }

    public BaseDto<PayFormDataDto> bind(String loginName, Source source, String ip, String deviceId) {
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>(payFormDataDto);

        UserBankCardModel userBankCardModel = userBankCardMapper.findByLoginName(loginName);

        if (userBankCardModel != null) {
            payFormDataDto.setMessage("已绑定银行卡，请勿重复绑定");
            payFormDataDto.setStatus(false);
            return baseDto;
        }

        // 发送用户行为日志 MQ消息
        userOpLogService.sendUserOpLogMQ(loginName, ip, source.name(), deviceId, UserOpType.BIND_CARD, null);

        UserModel userModel = this.userMapper.findByLoginName(loginName);

        baseDto = bankWrapperClient.bindBankCard(loginName, userModel.getMobile(), "UU02631330626431001", "UA02631330626471001");

        return baseDto;
    }

    public BaseDto<PayFormDataDto> unbind(String loginName, Source source, String ip, String deviceId) {
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>(payFormDataDto);

        UserBankCardModel userBankCardModel = userBankCardMapper.findByLoginName(loginName);

//        if (userBankCardModel == null) {
//            payFormDataDto.setMessage("未绑定银行卡");
//            payFormDataDto.setStatus(false);
//            return baseDto;
//        }

        // 发送用户行为日志 MQ消息
        userOpLogService.sendUserOpLogMQ(loginName, ip, source.name(), deviceId, UserOpType.UNBIND_CARD, null);

        UserModel userModel = this.userMapper.findByLoginName(loginName);

        baseDto = bankWrapperClient.unbindBankCard(loginName, userModel.getMobile(), "UU02631330626431001", "UA02631330626471001");

        return baseDto;
    }
}
