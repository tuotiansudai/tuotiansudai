package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.CreditLoanBillModel;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditLoanBillMapper {

    void create(CreditLoanBillModel model);

}
