package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.repository.model.RechargeSource;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RechargeServiceImpl implements RechargeService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Override
    public BaseDto<PayFormDataDto> recharge(RechargeDto rechargeDto) {
        return payWrapperClient.recharge(rechargeDto);
    }

    @Override
    public long sumSuccessRechargeAmount(String loginName) {
        return rechargeMapper.findSumSuccessRechargeByLoginName(loginName);
    }

    @Override
    public List<String> findAllChannel() {
        return rechargeMapper.findAllChannels();
    }

    @Override
    public BaseDto<BasePaginationDataDto> findRechargePagination(String rechargeId, String loginName, RechargeSource source,
                                                                 RechargeStatus status, String channel, int index, int pageSize, Date startTime, Date endTime) {
        if (index < 1) {
            index = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }

        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        List<RechargePaginationItemDataDto> rechargePaginationItemDataDtos = Lists.newArrayList();

        int count = rechargeMapper.findRechargeCount(rechargeId, loginName, source, status, channel, startTime, endTime);

        List<RechargeModel> rechargeModelList = rechargeMapper.findRechargePagination(rechargeId, loginName, source, status, channel, (index - 1) * pageSize, pageSize, startTime, endTime);

        for (RechargeModel model : rechargeModelList) {
            RechargePaginationItemDataDto rechargeDto = new RechargePaginationItemDataDto(model);
            rechargePaginationItemDataDtos.add(rechargeDto);
        }

        BasePaginationDataDto<RechargePaginationItemDataDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, rechargePaginationItemDataDtos);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);
        return baseDto;
    }


    @Override
    public long findSumRechargeAmount(String rechargeId,
                                      String loginName,
                                      RechargeSource source,
                                      RechargeStatus status,
                                      String channel,
                                      Date startTime,
                                      Date endTime) {
        return rechargeMapper.findSumRechargeAmount(rechargeId, loginName, source, status, channel, startTime, endTime);
    }
}
