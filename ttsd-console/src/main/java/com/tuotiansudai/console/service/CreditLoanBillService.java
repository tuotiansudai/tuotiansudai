package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.CreditLoanBillPaginationItemDataDto;
import com.tuotiansudai.repository.mapper.CreditLoanBillMapper;
import com.tuotiansudai.repository.model.CreditLoanBillBusinessType;
import com.tuotiansudai.repository.model.CreditLoanBillModel;
import com.tuotiansudai.repository.model.CreditLoanBillOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CreditLoanBillService {

    @Autowired
    private CreditLoanBillMapper creditLoanBillMapper;


    public BaseDto<BasePaginationDataDto<CreditLoanBillPaginationItemDataDto>> findCreditLoanBillPagination(Date startTime,
                                                                                                            Date endTime,
                                                                                                            CreditLoanBillOperationType operationType,
                                                                                                            CreditLoanBillBusinessType businessType,
                                                                                                            int index,
                                                                                                            int pageSize) {
        if (index < 1) {
            index = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }

        BaseDto<BasePaginationDataDto<CreditLoanBillPaginationItemDataDto>> baseDto = new BaseDto<>();
        List<CreditLoanBillPaginationItemDataDto> creditLoanBillPaginationItemDataDtos = Lists.newArrayList();

        int count = creditLoanBillMapper.findCreditLoanBillCount(startTime, endTime, operationType, businessType);

        List<CreditLoanBillModel> creditLoanBillModelList = creditLoanBillMapper.findCreditLoanBillPagination(startTime, endTime, operationType, businessType, (index - 1) * pageSize, pageSize);

        for (CreditLoanBillModel model : creditLoanBillModelList) {
            CreditLoanBillPaginationItemDataDto creditLoanBillDto = new CreditLoanBillPaginationItemDataDto(model);
            creditLoanBillPaginationItemDataDtos.add(creditLoanBillDto);
        }

        BasePaginationDataDto<CreditLoanBillPaginationItemDataDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, creditLoanBillPaginationItemDataDtos);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);
        return baseDto;
    }

    public long findSumCreditLoanIncome(Date startTime,
                                    Date endTime,
                                    CreditLoanBillOperationType operationType,
                                    CreditLoanBillBusinessType businessType) {
        return creditLoanBillMapper.findSumCreditLoanIncome(startTime, endTime, operationType, businessType);
    }

    public long findSumCreditLoanExpend(Date startTime,
                                    Date endTime,
                                    CreditLoanBillOperationType operationType,
                                    CreditLoanBillBusinessType businessType) {

        return creditLoanBillMapper.findSumCreditLoanExpend(startTime, endTime, operationType, businessType);
    }

}
