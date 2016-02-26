package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.RepayStatus;

import java.util.Date;
import java.util.List;

public interface LoanRepayService {

    BaseDto<BasePaginationDataDto> findLoanRepayPagination(int index, int pageSize, Long loanId,
                                                           String loginName, Date startTime, Date endTime, RepayStatus repayStatus);

    long findByLoginNameAndTimeSuccessRepay(String loginName,Date startTime,Date endTime);

    List<LoanRepayModel> findLoanRepayInAccount(String loginName,Date startTime,Date endTime,int startLimit,int endLimit);

    void calculateDefaultInterest();

    void loanRepayNotify();

}
