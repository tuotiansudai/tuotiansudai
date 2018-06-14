package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.repository.model.CreditLoanRechargeModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditLoanRechargeMapper {

    void create(CreditLoanRechargeModel model);

    CreditLoanRechargeModel findById(long id);

    void updateCreditLoanRechargeStatus(@Param(value = "id") long id,
                                        @Param(value = "rechargeStatus") BankRechargeStatus rechargeStatus);

    List<CreditLoanRechargeModel> findByAccountName(String accountName);
}
