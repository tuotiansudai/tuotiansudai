package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.AutoInvestPlanDataDto;
import com.tuotiansudai.api.dto.v1_0.AutoInvestPlanRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppAutoInvestPlanService;
import com.tuotiansudai.repository.mapper.AutoInvestPlanMapper;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class MobileAppAutoInvestPlanServiceImpl implements MobileAppAutoInvestPlanService {
    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Override
    @Transactional
    public BaseResponseDto buildAutoInvestPlan(AutoInvestPlanRequestDto autoInvestPlanRequestDto) {
        BaseResponseDto baseDto = new BaseResponseDto();
        String id = autoInvestPlanRequestDto.getAutoPlanId();
        String loginName = autoInvestPlanRequestDto.getBaseParam().getUserId();

        long minInvestAmount = AmountConverter.convertStringToCent(autoInvestPlanRequestDto.getMinInvestAmount());
        long maxInvestAmount = AmountConverter.convertStringToCent(autoInvestPlanRequestDto.getMaxInvestAmount());

        if(minInvestAmount > maxInvestAmount){
            return new BaseResponseDto(ReturnMessage.MIN_NOT_EXCEED_MAX_INVEST_AMOUNT.getCode(),ReturnMessage.MIN_NOT_EXCEED_MAX_INVEST_AMOUNT.getMsg());
        }

        if(StringUtils.isEmpty(id)){
            AutoInvestPlanModel autoInvestPlanModel = autoInvestPlanRequestDto.convertDtoToModel();
            autoInvestPlanModel.setId(idGenerator.generate());
            autoInvestPlanModel.setCreatedTime(new Date());
            autoInvestPlanMapper.create(autoInvestPlanModel);
        }else {
            AutoInvestPlanModel autoInvestPlanModelOrigin = autoInvestPlanMapper.findByLoginName(loginName);
            if(autoInvestPlanModelOrigin == null){
                return new BaseResponseDto(ReturnMessage.AUTO_INVEST_PLAN_NOT_EXIST.getCode(),loginName + ReturnMessage.AUTO_INVEST_PLAN_NOT_EXIST.getMsg());
            }
            AutoInvestPlanModel autoInvestPlanModel = autoInvestPlanRequestDto.convertDtoToModel();
            autoInvestPlanModel.setCreatedTime(autoInvestPlanModelOrigin.getCreatedTime());
            autoInvestPlanMapper.update(autoInvestPlanModel);

        }
        AutoInvestPlanModel autoInvestPlanModelNewest = autoInvestPlanMapper.findByLoginName(loginName);
        AutoInvestPlanDataDto autoInvestPlanDataDto = new AutoInvestPlanDataDto(autoInvestPlanModelNewest);
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseDto.setData(autoInvestPlanDataDto);
        return baseDto;
    }
}
