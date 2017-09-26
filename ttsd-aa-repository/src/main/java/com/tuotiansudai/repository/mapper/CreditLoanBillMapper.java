package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.CreditLoanBillBusinessType;
import com.tuotiansudai.repository.model.CreditLoanBillModel;
import com.tuotiansudai.repository.model.CreditLoanBillOperationType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CreditLoanBillMapper {

    void create(CreditLoanBillModel model);

    long findSumAmountByInAndBusinessType();

    long findSumAmountByOutAndBusinessType();

    List<CreditLoanBillModel> findCreditLoanBillPagination(@Param(value = "startTime") Date startTime,
                                                           @Param(value = "endTime") Date endTime,
                                                           @Param(value = "operationType") CreditLoanBillOperationType operationType,
                                                           @Param(value = "businessType") CreditLoanBillBusinessType businessType,
                                                           @Param(value = "index") int index,
                                                           @Param(value = "pageSize") int pageSize);

    int findCreditLoanBillCount(@Param(value = "startTime") Date startTime,
                                @Param(value = "endTime") Date endTime,
                                @Param(value = "operationType") CreditLoanBillOperationType operationType,
                                @Param(value = "businessType") CreditLoanBillBusinessType businessType);


    long findSumCreditLoanIncome(@Param(value = "startTime") Date startTime,
                                 @Param(value = "endTime") Date endTime,
                                 @Param(value = "operationType") CreditLoanBillOperationType operationType,
                                 @Param(value = "businessType") CreditLoanBillBusinessType businessType);

    long findSumCreditLoanExpend(@Param(value = "startTime") Date startTime,
                                 @Param(value = "endTime") Date endTime,
                                 @Param(value = "operationType") CreditLoanBillOperationType operationType,
                                 @Param(value = "businessType") CreditLoanBillBusinessType businessType);
}
