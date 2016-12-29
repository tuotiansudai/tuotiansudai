package com.tuotiansudai.console.service;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.TransferRuleDto;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.task.OperationTask;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.task.TaskConstant;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferRuleService {

    @Autowired
    private TransferRuleMapper transferRuleMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;


    @SuppressWarnings(value = "unchecked")
    public TransferRuleDto getTransferRule() {
        OperationTask<TransferRuleDto> task = (OperationTask<TransferRuleDto>) redisWrapperClient.hgetSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, OperationType.TRANSFER_RULE.name());
        TransferRuleModel transferRuleModel = transferRuleMapper.find();

        return new TransferRuleDto(transferRuleModel, task);
    }

    @Transactional
    public boolean updateTransferRule(TransferRuleDto transferRuleDto, String operatorLoginName, String ip) {
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        transferRuleModel.setLevelOneFee(transferRuleDto.getLevelOneFee() / 100);
        transferRuleModel.setLevelTwoFee(transferRuleDto.getLevelTwoFee() / 100);
        transferRuleModel.setLevelThreeFee(transferRuleDto.getLevelThreeFee() / 100);
        transferRuleModel.setDiscount(transferRuleDto.getDiscount() / 100);
        transferRuleModel.setDaysLimit(transferRuleDto.getDaysLimit());
        transferRuleModel.setMultipleTransferEnabled(transferRuleDto.isMultipleTransferEnabled());
        transferRuleMapper.update(transferRuleModel);
        return true;
    }
}
