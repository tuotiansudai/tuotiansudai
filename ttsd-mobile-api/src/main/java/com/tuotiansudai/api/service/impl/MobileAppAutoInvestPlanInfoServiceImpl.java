package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppAutoInvestPlanInfoService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.AutoInvestPlanMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppAutoInvestPlanInfoServiceImpl implements MobileAppAutoInvestPlanInfoService {
    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public BaseResponseDto getAutoInvestPlanInfoData(BaseParamDto baseParamDto) {
        BaseResponseDto baseDto = new BaseResponseDto();
        AutoInvestPlanInfoResponseDataDto autoInvestPlanInfoResponseDataDto = new AutoInvestPlanInfoResponseDataDto();
        String loginName = baseParamDto.getBaseParam().getUserId();
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel == null){
            autoInvestPlanInfoResponseDataDto.setAutoInvest(false);
        }else{
            autoInvestPlanInfoResponseDataDto.setAutoInvest(accountModel.isAutoInvest());
        }

        AutoInvestPlanModel autoInvestPlanModel = autoInvestPlanMapper.findByLoginName(loginName);
        if(autoInvestPlanModel != null){
            autoInvestPlanInfoResponseDataDto.setAutoInvestPlan(new AutoInvestPlanDataDto(autoInvestPlanModel));
        }

        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseDto.setData(autoInvestPlanInfoResponseDataDto);
        return baseDto;
    }
}
