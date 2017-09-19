package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.CreditLoanRechargeModel;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CreditLoanRechargeMapper {

    void create(CreditLoanRechargeModel model);

    CreditLoanRechargeModel findById(long id);

    void updateCreditLoanRecharge(CreditLoanRechargeModel creditLoanRechargeModel);

    List<CreditLoanRechargeModel> findByAccountName(String accountName);
}
