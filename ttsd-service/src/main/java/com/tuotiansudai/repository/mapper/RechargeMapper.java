package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.RechargeModel;

public interface RechargeMapper {

    void create(RechargeModel model);

    RechargeModel findById(String id);
}
