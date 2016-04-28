package com.esoft.jdp2p.invest.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.esoft.jdp2p.invest.service.InvestCalculator;
import com.esoft.jdp2p.repay.service.RepayCalculator;

@Service("investCalculator")
public class InvestCalculatorImpl implements InvestCalculator {

	@Resource
	RepayCalculator repayCalculator;

	@Override
	public Double calculateAnticipatedInterest(double investMoney, String loanId) {
		return repayCalculator
				.calculateAnticipatedInterest(investMoney, loanId);
	}

}
