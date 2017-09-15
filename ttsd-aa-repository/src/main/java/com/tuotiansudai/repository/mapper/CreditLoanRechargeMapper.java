package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.CreditLoanRechargeModel;
import org.springframework.stereotype.Repository;


@Repository
public interface CreditLoanRechargeMapper {

    void create(CreditLoanRechargeModel model);

    CreditLoanRechargeModel findById(long id);

    void updateCreditLoanRecharge(CreditLoanRechargeModel creditLoanRechargeModel);
}
