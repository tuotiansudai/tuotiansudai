package com.ttsd.api.dao;

import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;

import java.util.List;

public interface MobileAppInvestListDao {

    Integer getTotalCount(String loanId);

    List<Invest> getInvestList(Integer index, Integer pageSize,String loanId);
}
