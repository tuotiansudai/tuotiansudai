package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.TransferRuleModel;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRuleMapper {

    TransferRuleModel find();

    void update(TransferRuleModel transferRuleModel);
}
