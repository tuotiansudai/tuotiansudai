package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.repository.model.RechargeStatus;
import org.apache.ibatis.annotations.Param;

public interface RechargeMapper {

    void create(RechargeModel model);

    void update(@Param("id") long id, @Param("status") RechargeStatus status);

    RechargeModel findById(long id);

    long findSumRechargeByLoginName(String loginName);
}
