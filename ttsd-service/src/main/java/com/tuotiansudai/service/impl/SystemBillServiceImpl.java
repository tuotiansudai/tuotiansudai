package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.SystemBillPaginationItemDataDto;
import com.tuotiansudai.repository.mapper.SystemBillMapper;
import com.tuotiansudai.repository.model.SystemBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillModel;
import com.tuotiansudai.repository.model.SystemBillOperationType;
import com.tuotiansudai.service.SystemBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SystemBillServiceImpl implements SystemBillService {

    @Autowired
    private SystemBillMapper systemBillMapper;


    @Override
    public BaseDto<BasePaginationDataDto> findSystemBillPagination(Date startTime,
                                                                   Date endTime,
                                                                   SystemBillOperationType operationType,
                                                                   SystemBillBusinessType businessType,
                                                                   int index,
                                                                   int pageSize) {
        if (index < 1) {
            index = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }

        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        List<SystemBillPaginationItemDataDto> systemBillPaginationItemDataDtos = Lists.newArrayList();

        int count = systemBillMapper.findSystemBillCount(startTime, endTime, operationType, businessType);

        List<SystemBillModel> systemBillModelList = systemBillMapper.findSystemBillPagination(startTime, endTime, operationType, businessType, (index - 1) * pageSize, pageSize);

        for (SystemBillModel model : systemBillModelList) {
            SystemBillPaginationItemDataDto systemBillDto = new SystemBillPaginationItemDataDto(model);
            systemBillPaginationItemDataDtos.add(systemBillDto);
        }

        BasePaginationDataDto<SystemBillPaginationItemDataDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, systemBillPaginationItemDataDtos);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);
        return baseDto;
    }

    @Override
    public long findSumSystemIncome(Date startTime,
                                    Date endTime,
                                    SystemBillOperationType operationType,
                                    SystemBillBusinessType businessType) {
        return systemBillMapper.findSumSystemIncome(startTime, endTime, operationType, businessType);
    }

    @Override
    public long findSumSystemExpend(Date startTime,
                                    Date endTime,
                                    SystemBillOperationType operationType,
                                    SystemBillBusinessType businessType) {

        return systemBillMapper.findSumSystemExpend(startTime, endTime, operationType, businessType);
    }

    @Override
    public int findSystemBillCount(Date startTime,
                                   Date endTime,
                                   SystemBillOperationType operationType,
                                   SystemBillBusinessType businessType) {

        int count = systemBillMapper.findSystemBillCount(startTime, endTime, operationType, businessType);
        return count;
    }
}
