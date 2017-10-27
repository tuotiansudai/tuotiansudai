package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tuotiansudai.repository.mapper.PayrollMapper;
import com.tuotiansudai.repository.model.PayrollModel;
import com.tuotiansudai.repository.model.PayrollStatusType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsolePayrollService {

    private static Logger logger = Logger.getLogger(ConsolePayrollService.class);

    @Autowired
    private PayrollMapper payrollMapper;

    @Transactional
    public void primaryAudit(long payRollId) {
        PayrollModel payrollModel = payrollMapper.findById(payRollId);
        if (payrollModel == null
                || Sets.newHashSet(PayrollStatusType.PENDING, PayrollStatusType.REJECTED).contains(payrollModel.getStatus())) {
            logger.debug("payRollId not exist or status no pending rejected ");
            return;
        }
        

    }

    public void advancedAudit(long payRollId) {

    }

    public boolean checkSufficientBalance(long payRollId) {
        return true;
    }


}
