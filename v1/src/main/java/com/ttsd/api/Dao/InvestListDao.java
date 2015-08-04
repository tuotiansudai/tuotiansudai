package com.ttsd.api.dao;

import com.esoft.jdp2p.loan.model.Loan;
import com.ttsd.api.dto.InvestDto;

import java.util.List;

public interface InvestListDao {

    boolean isHasNextPage(Integer index, Integer pageSize);

    List<Loan> getInvestList(Integer index, Integer pageSize);
}
