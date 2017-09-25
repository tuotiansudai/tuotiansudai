package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.CreditLoanBillBusinessType;
import com.tuotiansudai.repository.model.CreditLoanBillModel;
import com.tuotiansudai.repository.model.SystemBillOperationType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditLoanBillMapper {

    void create(CreditLoanBillModel model);

    long findSumAmountByInAndBusinessType();

    long findSumAmountByOutAndBusinessType();
}
