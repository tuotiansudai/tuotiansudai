package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppNoPasswordInvestService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppNoPasswordInvestServiceImpl implements MobileAppNoPasswordInvestService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public BaseResponseDto getNoPasswordInvestData(BaseParamDto baseParamDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String loginName = baseParamDto.getBaseParam().getUserId();
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if(accountModel == null){
            return new BaseResponseDto(ReturnMessage.USER_IS_NOT_CERTIFICATED.getCode(),ReturnMessage.USER_IS_NOT_CERTIFICATED.getMsg());
        }
        NoPasswordInvestResponseDataDto noPasswordInvestResponseDataDto = new NoPasswordInvestResponseDataDto();
        noPasswordInvestResponseDataDto.setAutoInvest(accountModel.isAutoInvest());
        noPasswordInvestResponseDataDto.setNoPasswordInvest(accountModel.isNoPasswordInvest());
        baseResponseDto.setData(noPasswordInvestResponseDataDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }
}
