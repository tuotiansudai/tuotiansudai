package com.tuotiansudai.transfer.service;

import com.tuotiansudai.dto.TransferRuleDto;
import org.springframework.transaction.annotation.Transactional;

public interface TransferRuleService {

    TransferRuleDto getTransferRule();

    @Transactional
    boolean updateTransferRule(TransferRuleDto transferRuleDto, String operatorLoginName, String ip);
}
