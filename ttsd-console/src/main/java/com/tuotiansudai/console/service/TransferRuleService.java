package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.TransferRuleDto;

public interface TransferRuleService {

    TransferRuleDto getTransferRule();

    boolean updateTransferRule(TransferRuleDto transferRuleDto, String operatorLoginName, String ip);
}
