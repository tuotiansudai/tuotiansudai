package com.tuotiansudai.api.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppAutoInvestPlanInfoService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.AutoInvestPlanMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.util.AutoInvestMonthPeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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
        }else{
            AutoInvestPlanDataDto autoInvestPlanDataDto = new AutoInvestPlanDataDto();
            autoInvestPlanDataDto.setEnabled(true);
            List<AutoInvestMonthPeriod> allPeriods = Arrays.asList(AutoInvestMonthPeriod.AllPeriods);
            List<AutoInvestPeriodDto> autoInvestPeriodsDtos = Lists.transform(allPeriods, new Function<AutoInvestMonthPeriod, AutoInvestPeriodDto>() {

                @Override
                public AutoInvestPeriodDto apply(AutoInvestMonthPeriod input) {
                    AutoInvestPeriodDto autoInvestPeriodDto = new AutoInvestPeriodDto();
                    autoInvestPeriodDto.setPid("" + input.getPeriodValue());
                    autoInvestPeriodDto.setpName("" + input.getPeriodName());
                    autoInvestPeriodDto.setSelected(false);
                    return autoInvestPeriodDto;
                }
            });
            autoInvestPlanDataDto.setAutoInvestPeriods(autoInvestPeriodsDtos);
            autoInvestPlanInfoResponseDataDto.setAutoInvestPlan(autoInvestPlanDataDto);
        }

        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseDto.setData(autoInvestPlanInfoResponseDataDto);
        return baseDto;
    }
}
