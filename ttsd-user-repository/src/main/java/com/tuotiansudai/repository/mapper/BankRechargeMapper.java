package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BankRechargeModel;
import feign.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRechargeMapper {

    void create(BankRechargeModel userRechargeModel);

    void update(BankRechargeModel userRechargeModel);

    BankRechargeModel findById(@Param(value = "id") long id);

    long sumRechargeSuccessAmountByLoginName(@Param(value = "loginName") String loginName);

}
