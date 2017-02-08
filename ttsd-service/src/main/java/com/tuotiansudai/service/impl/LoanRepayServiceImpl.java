package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.service.LoanRepayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LoanRepayServiceImpl implements LoanRepayService {

    static Logger logger = Logger.getLogger(LoanRepayServiceImpl.class);

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Override
    public List<LoanRepayModel> findLoanRepayInAccount(String loginName, Date startTime, Date endTime, int startLimit, int endLimit) {
        return this.loanRepayMapper.findByLoginNameAndTimeRepayList(loginName, startTime, endTime, startLimit, endLimit);
    }

    @Override
    public long findByLoginNameAndTimeSuccessRepay(String loginName, Date startTime, Date endTime) {
        return loanRepayMapper.findByLoginNameAndTimeSuccessRepay(loginName, startTime, endTime);
    }
}
