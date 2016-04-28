package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppNoPasswordInvestTurnOnService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppNoPasswordInvestTurnOnServiceImpl implements MobileAppNoPasswordInvestTurnOnService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public BaseResponseDto noPasswordInvestTurnOn(BaseParamDto baseParamDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String loginName = baseParamDto.getBaseParam().getUserId();
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        accountModel.setNoPasswordInvest(true);
        accountMapper.update(accountModel);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }
}
