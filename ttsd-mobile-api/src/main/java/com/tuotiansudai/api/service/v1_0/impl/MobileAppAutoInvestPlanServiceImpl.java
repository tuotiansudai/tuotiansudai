package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppAutoInvestPlanService;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.repository.mapper.AutoInvestPlanMapper;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class MobileAppAutoInvestPlanServiceImpl implements MobileAppAutoInvestPlanService {

    private static Logger logger = Logger.getLogger(MobileAppAutoInvestPlanServiceImpl.class);

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    @Autowired
    private UserOpLogService userOpLogService;


    @Override
    @Transactional
    public BaseResponseDto<AutoInvestPlanDataDto> buildAutoInvestPlan(AutoInvestPlanRequestDto dto) {
        BaseResponseDto baseDto = new BaseResponseDto();
        String id = dto.getAutoPlanId();
        String loginName = dto.getBaseParam().getUserId();

        long minInvestAmount = AmountConverter.convertStringToCent(dto.getMinInvestAmount());
        long maxInvestAmount = AmountConverter.convertStringToCent(dto.getMaxInvestAmount());

        if (minInvestAmount > maxInvestAmount) {
            return new BaseResponseDto(ReturnMessage.MIN_NOT_EXCEED_MAX_INVEST_AMOUNT.getCode(), ReturnMessage.MIN_NOT_EXCEED_MAX_INVEST_AMOUNT.getMsg());
        }

        if (StringUtils.isEmpty(id)) {
            AutoInvestPlanModel autoInvestPlanModel = dto.convertDtoToModel();
            autoInvestPlanModel.setId(IdGenerator.generate());
            autoInvestPlanModel.setCreatedTime(new Date());
            autoInvestPlanMapper.create(autoInvestPlanModel);
        } else {
            AutoInvestPlanModel autoInvestPlanModelOrigin = autoInvestPlanMapper.findByLoginName(loginName);
            if (autoInvestPlanModelOrigin == null) {
                return new BaseResponseDto(ReturnMessage.AUTO_INVEST_PLAN_NOT_EXIST.getCode(), loginName + ReturnMessage.AUTO_INVEST_PLAN_NOT_EXIST.getMsg());
            }
            AutoInvestPlanModel autoInvestPlanModel = dto.convertDtoToModel();
            autoInvestPlanModel.setCreatedTime(autoInvestPlanModelOrigin.getCreatedTime());
            autoInvestPlanMapper.update(autoInvestPlanModel);

        }
        AutoInvestPlanModel autoInvestPlanModelNewest = autoInvestPlanMapper.findByLoginName(loginName);
        AutoInvestPlanDataDto autoInvestPlanDataDto = new AutoInvestPlanDataDto(autoInvestPlanModelNewest);
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseDto.setData(autoInvestPlanDataDto);

        // 发送用户行为日志 MQ
        BaseParam baseParam = dto.getBaseParam();
        userOpLogService.sendUserOpLogMQ(baseParam.getUserId(), dto.getIp(), baseParam.getPlatform(), baseParam.getDeviceId(),
                UserOpType.AUTO_INVEST, dto.isEnabled() ? "Turn On" : "Turn Off");
        return baseDto;
    }
}
