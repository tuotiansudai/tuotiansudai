package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CreditLoanBillMapper {

    void create(CreditLoanBillModel model);

    SystemBillModel findByOrderId(@Param("orderId") Long orderId,
                                  @Param("systemBillBusinessType") SystemBillBusinessType systemBillBusinessType);

    List<SystemBillModel> findCreditLoanBillPagination(@Param(value = "startTime") Date startTime,
                                                   @Param(value = "endTime") Date endTime,
                                                   @Param(value = "operationType") SystemBillOperationType operationType,
                                                   @Param(value = "businessType") SystemBillBusinessType businessType,
                                                   @Param(value = "index") int index,
                                                   @Param(value = "pageSize") int pageSize);

    int findCreditLoanBillCount(@Param(value = "startTime") Date startTime,
                            @Param(value = "endTime") Date endTime,
                            @Param(value = "operationType") SystemBillOperationType operationType,
                            @Param(value = "businessType") SystemBillBusinessType businessType);


    long findSumCreditLoanIncome(@Param(value = "startTime") Date startTime,
                             @Param(value = "endTime") Date endTime,
                             @Param(value = "operationType") SystemBillOperationType operationType,
                             @Param(value = "businessType") SystemBillBusinessType businessType);

    long findSumCreditLoanExpend(@Param(value = "startTime") Date startTime,
                             @Param(value = "endTime") Date endTime,
                             @Param(value = "operationType") SystemBillOperationType operationType,
                             @Param(value = "businessType") SystemBillBusinessType businessType);

}
