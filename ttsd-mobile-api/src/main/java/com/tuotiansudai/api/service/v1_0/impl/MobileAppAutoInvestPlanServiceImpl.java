package com.tuotiansudai.api.service.v1_0.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.api.dto.v1_0.AutoInvestPlanDataDto;
import com.tuotiansudai.api.dto.v1_0.AutoInvestPlanRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppAutoInvestPlanService;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.log.repository.model.UserOpLogModel;
import com.tuotiansudai.log.repository.model.UserOpType;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.AutoInvestPlanMapper;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Locale;

@Service
public class MobileAppAutoInvestPlanServiceImpl implements MobileAppAutoInvestPlanService {

    private static Logger logger = Logger.getLogger(MobileAppAutoInvestPlanServiceImpl.class);

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    @Transactional
    public BaseResponseDto<AutoInvestPlanDataDto> buildAutoInvestPlan(AutoInvestPlanRequestDto autoInvestPlanRequestDto) {
        BaseResponseDto baseDto = new BaseResponseDto();
        String id = autoInvestPlanRequestDto.getAutoPlanId();
        String loginName = autoInvestPlanRequestDto.getBaseParam().getUserId();

        long minInvestAmount = AmountConverter.convertStringToCent(autoInvestPlanRequestDto.getMinInvestAmount());
        long maxInvestAmount = AmountConverter.convertStringToCent(autoInvestPlanRequestDto.getMaxInvestAmount());

        if (minInvestAmount > maxInvestAmount) {
            return new BaseResponseDto(ReturnMessage.MIN_NOT_EXCEED_MAX_INVEST_AMOUNT.getCode(), ReturnMessage.MIN_NOT_EXCEED_MAX_INVEST_AMOUNT.getMsg());
        }

        if (StringUtils.isEmpty(id)) {
            AutoInvestPlanModel autoInvestPlanModel = autoInvestPlanRequestDto.convertDtoToModel();
            autoInvestPlanModel.setId(idGenerator.generate());
            autoInvestPlanModel.setCreatedTime(new Date());
            autoInvestPlanMapper.create(autoInvestPlanModel);
        } else {
            AutoInvestPlanModel autoInvestPlanModelOrigin = autoInvestPlanMapper.findByLoginName(loginName);
            if (autoInvestPlanModelOrigin == null) {
                return new BaseResponseDto(ReturnMessage.AUTO_INVEST_PLAN_NOT_EXIST.getCode(), loginName + ReturnMessage.AUTO_INVEST_PLAN_NOT_EXIST.getMsg());
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

        // 发送用户行为日志 MQ
        sendUserOpLogMessage(autoInvestPlanRequestDto);
        return baseDto;
    }

    private void sendUserOpLogMessage(AutoInvestPlanRequestDto dto) {

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setId(idGenerator.generate());
        logModel.setLoginName(dto.getBaseParam().getUserId());
        logModel.setIp(dto.getIp());
        logModel.setDeviceId(dto.getBaseParam().getDeviceId());
        String platform = dto.getBaseParam().getPlatform();
        logModel.setSource(platform == null ? null : Source.valueOf(platform.toUpperCase(Locale.ENGLISH)));
        logModel.setOpType(UserOpType.AUTO_INVEST);
        logModel.setCreatedTime(new Date());
        logModel.setDescription(dto.isEnabled() ? "Turn On" : "Turn Off");

        try {
            String message = JsonConverter.writeValueAsString(logModel);
            mqWrapperClient.sendMessage(MessageQueue.UserOperateLog, message);
        } catch (JsonProcessingException e) {
            logger.error("[MQ] 自动投标 send user op log fail.", e);
        }
    }
}
