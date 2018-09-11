package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppAutoInvestPlanInfoService;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.AutoInvestPlanMapper;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.repository.model.BankAccountModel;
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
    private BankAccountMapper bankAccountMapper;

    @Override
    public BaseResponseDto<AutoInvestPlanInfoResponseDataDto> getAutoInvestPlanInfoData(BaseParamDto baseParamDto) {
        BaseResponseDto baseDto = new BaseResponseDto();
        AutoInvestPlanInfoResponseDataDto autoInvestPlanInfoResponseDataDto = new AutoInvestPlanInfoResponseDataDto();
        String loginName = baseParamDto.getBaseParam().getUserId();
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(loginName, Role.INVESTOR);
        if (bankAccountModel == null){
            autoInvestPlanInfoResponseDataDto.setAutoInvest(false);
        }else{
            autoInvestPlanInfoResponseDataDto.setAutoInvest(bankAccountModel.isAuthorization());
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
