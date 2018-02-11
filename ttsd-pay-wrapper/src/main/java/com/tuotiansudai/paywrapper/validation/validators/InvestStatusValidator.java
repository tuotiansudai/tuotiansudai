package com.tuotiansudai.paywrapper.validation.validators;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferSearchResponseModel;
import com.tuotiansudai.paywrapper.service.UMPayRealTimeStatusService;
import com.tuotiansudai.paywrapper.validation.ValidationScheduler;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;

@Component
public class InvestStatusValidator {

    private final static Logger logger = Logger.getLogger(ValidationScheduler.class);

    private final static String NOT_FOUND_ORDER_ID = "00240005"; //无原记录

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    private final InvestMapper investMapper;

    private final UMPayRealTimeStatusService umPayRealTimeStatusService;

    private final MQWrapperClient mqWrapperClient;


    @Autowired
    public InvestStatusValidator(InvestMapper investMapper, UMPayRealTimeStatusService umPayRealTimeStatusService, MQWrapperClient mqWrapperClient) {
        this.investMapper = investMapper;
        this.umPayRealTimeStatusService = umPayRealTimeStatusService;
        this.mqWrapperClient = mqWrapperClient;
    }

    public BaseDto<PayDataDto> validate(long investId) {
        BaseDto<PayDataDto> dto = new BaseDto<>(new PayDataDto(true));

        InvestModel investModel = investMapper.findById(investId);

        if (investModel == null) {
            dto.getData().setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder().put("transferStatus", "投资不存在").build()));
            return dto;
        }

        if (investModel.getStatus() != InvestStatus.WAIT_PAY) {
            dto.getData().setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder().put("transferStatus", investModel.getStatus().getDescription()).build()));
            return dto;
        }

        try {
            TransferSearchResponseModel transferStatus = umPayRealTimeStatusService.getTransferStatus(String.valueOf(investId), DATE_FORMAT.format(investModel.getCreatedTime()), "03");

            if (transferStatus.getRetCode().equalsIgnoreCase(NOT_FOUND_ORDER_ID)) {
                dto.getData().setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder().put("transferStatus", transferStatus.getRetMsg()).build()));
                dto.getData().setStatus(new DateTime().isAfter(new DateTime(investModel.getCreatedTime()).plusMinutes(1)));
                return dto;
            }

            if (transferStatus.isSuccess()) {
                String statusMessage = transferStatus.generateHumanReadableInfo().get("交易状态");
                String tranStatus = transferStatus.getTranState();

                // 交易状态 0:初始 4:不明 6:其他
                if (Lists.newArrayList("0", "4", "6").contains(tranStatus)) {
                    dto.getData().setStatus(false);
                    dto.getData().setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder().put("transferStatus", statusMessage).build()));
                    return dto;
                }

                // 交易状态 2:成功
                if (tranStatus.equals("2")) {
                    mqWrapperClient.sendMessage(MessageQueue.InvestCallback, String.valueOf(investId));
                    dto.getData().setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder().put("transferStatus", statusMessage).build()));
                    return dto;
                }

                // 交易状态 3:失败 5:交易关闭
                if (Lists.newArrayList("3", "5").contains(tranStatus)) {
                    investModel.setStatus(InvestStatus.FAIL);
                    investMapper.update(investModel);
                    dto.getData().setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder().put("transferStatus", statusMessage).build()));
                    return dto;
                }
            } else {
                logger.warn(MessageFormat.format("invest ({0}) status is ({1}), message ({2})", String.valueOf(investId), transferStatus.getRetCode(), transferStatus.getRetMsg()));

                dto.getData().setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder().put("transferStatus", transferStatus.getRetMsg()).build()));

                return dto;
            }
        } catch (Exception e) {
            logger.warn(e.getLocalizedMessage(), e);
            dto.getData().setStatus(false);
            dto.getData().setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder().put("transferStatus", "查询异常").build()));

        }
        return dto;
    }
}
