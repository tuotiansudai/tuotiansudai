package com.ttsd.api.dao;


import com.esoft.jdp2p.loan.model.Loan;

public interface LoanDetailDao {

    Loan getLoanById(String loanId);

}
