package com.ttsd.api.dao;

import com.esoft.jdp2p.loan.model.Loan;

import java.util.Date;
import java.util.List;

public interface MobileAppLoanListDao {

    Integer getTotalCount();

    List<Loan> getLoanList(Integer index, Integer pageSize);

    List<Loan> getCompletedXsInvest();

    Date getRaiseCompletedTime(String loanId);
}
