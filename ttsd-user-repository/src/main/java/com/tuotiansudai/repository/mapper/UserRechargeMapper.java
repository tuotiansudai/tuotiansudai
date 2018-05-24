package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserRechargeModel;
import feign.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRechargeMapper {

    void create(UserRechargeModel userRechargeModel);

    void update(UserRechargeModel userRechargeModel);

    UserRechargeModel findById(@Param(value = "id") long id);

    long sumRechargeSuccessAmountByLoginName(@Param(value = "loginName") String loginName);

}
