package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserRechargeModel;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRechargeMapper {

    void create(UserRechargeModel userRechargeModel);

    void update(UserRechargeModel userRechargeModel);
}
