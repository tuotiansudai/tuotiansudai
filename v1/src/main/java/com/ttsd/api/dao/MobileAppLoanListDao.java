package com.ttsd.api.dao;

import com.esoft.jdp2p.loan.model.Loan;

import java.util.List;

public interface MobileAppLoanListDao {

    Integer getTotalCount();

    List<Loan> getInvestList(Integer index, Integer pageSize);
}
