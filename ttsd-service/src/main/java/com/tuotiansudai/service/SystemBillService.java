package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.SystemBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillOperationType;

import java.util.Date;


public interface SystemBillService {

    BaseDto<BasePaginationDataDto> findSystemBillPagination(Date startTime,
                                                            Date endTime,
                                                            SystemBillOperationType operationType,
                                                            SystemBillBusinessType businessType,
                                                            int index,
                                                            int pageSize);

    long findSumSystemIncome(Date startTime,
                             Date endTime,
                             SystemBillOperationType operationType,
                             SystemBillBusinessType businessType);


    long findSumSystemExpend(Date startTime,
                             Date endTime,
                             SystemBillOperationType operationType,
                             SystemBillBusinessType businessType);

    int findSystemBillCount(Date startTime,
                            Date endTime,
                            SystemBillOperationType operationType,
                            SystemBillBusinessType businessType);
}
