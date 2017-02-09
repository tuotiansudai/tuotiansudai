package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.LoanRepayModel;

import java.util.Date;
import java.util.List;

public interface LoanRepayService {

    long findByLoginNameAndTimeSuccessRepay(String loginName, Date startTime, Date endTime);

    List<LoanRepayModel> findLoanRepayInAccount(String loginName, Date startTime, Date endTime, int startLimit, int endLimit);
}
