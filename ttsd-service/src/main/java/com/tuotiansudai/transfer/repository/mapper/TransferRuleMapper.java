package com.tuotiansudai.transfer.repository.mapper;

import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRuleMapper {

    TransferRuleModel find();

    void update(TransferRuleModel transferRuleModel);
}
