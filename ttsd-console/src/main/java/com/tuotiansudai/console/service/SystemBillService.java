package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.SystemBillPaginationItemDataDto;
import com.tuotiansudai.enums.BillOperationType;
import com.tuotiansudai.repository.mapper.BankSystemBillMapper;
import com.tuotiansudai.repository.mapper.SystemBillMapper;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.repository.model.BankSystemBillModel;
import com.tuotiansudai.repository.model.SystemBillModel;
import com.tuotiansudai.repository.model.SystemBillOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SystemBillService {

    @Autowired
    private BankSystemBillMapper bankSystemBillMapper;


    public BaseDto<BasePaginationDataDto<SystemBillPaginationItemDataDto>> findSystemBillPagination(Date startTime,
                                                                                                    Date endTime,
                                                                                                    BillOperationType operationType,
                                                                                                    SystemBillBusinessType businessType,
                                                                                                    int index,
                                                                                                    int pageSize) {
        if (index < 1) {
            index = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }

        BaseDto<BasePaginationDataDto<SystemBillPaginationItemDataDto>> baseDto = new BaseDto<>();
        List<SystemBillPaginationItemDataDto> systemBillPaginationItemDataDtos = Lists.newArrayList();

        int count = bankSystemBillMapper.findSystemBillCount(startTime, endTime, operationType, businessType);

        List<BankSystemBillModel> systemBillModelList = bankSystemBillMapper.findSystemBillPagination(startTime, endTime, operationType, businessType, (index - 1) * pageSize, pageSize);

        for (BankSystemBillModel model : systemBillModelList) {
            SystemBillPaginationItemDataDto systemBillDto = new SystemBillPaginationItemDataDto(model);
            systemBillPaginationItemDataDtos.add(systemBillDto);
        }

        BasePaginationDataDto<SystemBillPaginationItemDataDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, systemBillPaginationItemDataDtos);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);
        return baseDto;
    }

    public long findSumSystemIncome(Date startTime,
                                    Date endTime,
                                    BillOperationType operationType,
                                    SystemBillBusinessType businessType) {
        return bankSystemBillMapper.findSumSystemBillAmount(startTime, endTime, operationType, businessType);
    }

    public long findSumSystemExpend(Date startTime,
                                    Date endTime,
                                    BillOperationType operationType,
                                    SystemBillBusinessType businessType) {

        return bankSystemBillMapper.findSumSystemBillAmount(startTime, endTime, operationType, businessType);
    }

}
