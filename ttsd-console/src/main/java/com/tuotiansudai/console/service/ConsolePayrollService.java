package com.tuotiansudai.console.service;

import com.google.common.collect.Sets;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.PayrollDetailMapper;
import com.tuotiansudai.repository.mapper.PayrollMapper;
import com.tuotiansudai.repository.model.PayrollDetailModel;
import com.tuotiansudai.repository.model.PayrollModel;
import com.tuotiansudai.repository.model.PayrollStatusType;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ConsolePayrollService {

    private static Logger logger = Logger.getLogger(ConsolePayrollService.class);

    @Autowired
    private PayrollMapper payrollMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private PayrollDetailMapper payrollDetailMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Transactional
    public void primaryAudit(long payRollId, String loginName) {
        PayrollModel payrollModel = payrollMapper.findById(payRollId);
        if (payrollModel == null
                || Sets.newHashSet(PayrollStatusType.PENDING, PayrollStatusType.REJECTED).contains(payrollModel.getStatus())) {
            logger.debug("payRollId not exist or status no pending rejected ");
            return;
        }
        payrollModel.setUpdatedBy(loginName);
        payrollModel.setUpdatedTime(new Date());
        payrollModel.setStatus(PayrollStatusType.AUDITED);
        payrollMapper.update(payrollModel);
    }

    @Transactional
    public BaseDto<BaseDataDto> advancedAudit(long payRollId,String loginName) {
        if (!isSufficientBalance(payRollId)) {
            logger.info("system balance is not sufficient");
            return new BaseDto<>(new BaseDataDto(false, "系统账户余额不足!"));
        }
        PayrollModel payrollModel = payrollMapper.findById(payRollId);
        payrollModel.setUpdatedBy(loginName);
        payrollModel.setUpdatedTime(new Date());
        payrollModel.setStatus(PayrollStatusType.AUDITED);
        payrollMapper.update(payrollModel);
        logger.info(String.format("%s send payroll message begin ...", String.valueOf(payRollId)));
        beginPayroll(payRollId);
        logger.info(String.format("%s send payroll message end ...", String.valueOf(payRollId)));

        return new BaseDto<>(new BaseDataDto(true));
    }

    @Transactional
    public void reject(long payRollId, String loginName) {
        PayrollModel payrollModel = payrollMapper.findById(payRollId);
        if (payrollModel == null
                || Sets.newHashSet(PayrollStatusType.PENDING, PayrollStatusType.AUDITED).contains(payrollModel.getStatus())) {
            logger.debug("payRollId not exist or status no pending audited ");
            return;
        }
        payrollModel.setUpdatedBy(loginName);
        payrollModel.setUpdatedTime(new Date());
        payrollModel.setStatus(PayrollStatusType.REJECTED);
        payrollMapper.update(payrollModel);

    }

    private boolean isSufficientBalance(long payRollId) {
        long systemBalance = obtainSystemBalanceFromUmp();

        return systemBalance > 0 && systemBalance >= sumPayingAmount(payRollId);
    }

    private long obtainSystemBalanceFromUmp() {
        Map<String, String> systemInfo = payWrapperClient.getPlatformStatus();

        return systemInfo != null && systemInfo.containsKey("账户余额")
                ? AmountConverter.convertStringToCent(systemInfo.get("账户余额")) : 0l;

    }

    private long sumPayingAmount(long payRollId) {
        List<PayrollDetailModel> details = payrollDetailMapper.findByPayrollId(payRollId);

        return details.stream().map(detail -> detail.getAmount()).reduce(0l, Long::sum).longValue();
    }


    private void beginPayroll(long payrollId) {
        mqWrapperClient.sendMessage(MessageQueue.Payroll, String.valueOf(payrollId));
    }
}
