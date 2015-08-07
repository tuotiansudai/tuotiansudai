package com.ttsd.api.dao;


import com.esoft.jdp2p.loan.model.Loan;

public interface MobileAppLoanDetailDao {

    Loan getLoanById(String loanId);

}
