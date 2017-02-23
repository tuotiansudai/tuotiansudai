package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.RechargePaginationItemDataDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.repository.model.RechargeSource;
import com.tuotiansudai.repository.model.RechargeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConsoleRechargeService {

    @Autowired
    private RechargeMapper rechargeMapper;

    public List<String> findAllChannel() {
        return rechargeMapper.findAllChannels();
    }

    public BaseDto<BasePaginationDataDto<RechargePaginationItemDataDto>> findRechargePagination(String rechargeId, String mobile, RechargeSource source,
                                                                                                RechargeStatus status, String channel, int index, int pageSize, Date startTime, Date endTime, Role role) {
        if (index < 1) {
            index = 1;
        }

        BaseDto<BasePaginationDataDto<RechargePaginationItemDataDto>> baseDto = new BaseDto<>();
        List<RechargePaginationItemDataDto> rechargePaginationItemDataDtos = Lists.newArrayList();

        int count = rechargeMapper.findRechargeCount(rechargeId, mobile, source, status, channel, startTime, endTime, role);

        List<RechargeModel> rechargeModelList = rechargeMapper.findRechargePagination(rechargeId, mobile, source, status, channel, (index - 1) * pageSize, pageSize, startTime, endTime, role);

        for (RechargeModel model : rechargeModelList) {
            RechargePaginationItemDataDto rechargeDto = new RechargePaginationItemDataDto(model);
            rechargePaginationItemDataDtos.add(rechargeDto);
        }

        BasePaginationDataDto<RechargePaginationItemDataDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, rechargePaginationItemDataDtos);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);
        return baseDto;
    }

    public long findSumRechargeAmount(String rechargeId,
                                      String mobile,
                                      RechargeSource source,
                                      RechargeStatus status,
                                      String channel,
                                      Date startTime,
                                      Date endTime,
                                      Role role) {
        return rechargeMapper.findSumRechargeAmount(rechargeId, mobile, source, status, channel, role, startTime, endTime);
    }
}
