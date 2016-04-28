package com.esoft.jdp2p.repay.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.core.util.ArithUtil;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.repay.RepayConstants.RepayType;
import com.esoft.jdp2p.repay.exception.IllegalLoanTypeException;
import com.esoft.jdp2p.repay.model.AdvanceRepay;
import com.esoft.jdp2p.repay.model.LoanRepay;
import com.esoft.jdp2p.repay.model.Repay;
import com.esoft.jdp2p.repay.service.RepayCalculator;
import com.esoft.jdp2p.repay.service.RepayService;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeePoint;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeeType;
import com.esoft.jdp2p.risk.service.impl.FeeConfigBO;

@Service("repayCalculator")
public class RepayCalculatorImpl implements RepayCalculator {

	@Resource
	NormalRepayRFCLCalculator normalRepayRFCLCalculator;

	@Resource
	NormalRepayCPMCalculator normalRepayCPMCalculator;
	
	@Resource
	NormalRepayRLIOCalculator normalRepayRLIOCalculator;

	@Resource
	HibernateTemplate ht;

	@Resource
	RepayService repayService;
	@Resource
	FeeConfigBO feeConfigBO;

	@Override
	public Double calculateAnticipatedInterest(double money, String loanId) {
		Loan loan = ht.get(Loan.class, loanId);
		Double sumInterest = 0D;
		// 如果不制定该值，则默认为放款日；在放款之前，默认为预计执行时间
		Date interestBeginTime = loan.getInterestBeginTime();
		if (interestBeginTime == null) {
			interestBeginTime = loan.getGiveMoneyTime();
		}
		if (interestBeginTime == null) {
			interestBeginTime = loan.getExpectTime();
		}
		List<Repay> repays = null;
		// 先付利息后还本金
		if (loan.getType().getRepayType().equals(RepayType.RFCL)) {
			repays = normalRepayRFCLCalculator.generateRepays(money,
					interestBeginTime, loan.getRate(), loan.getDeadline(), loan
							.getType().getRepayTimeUnit(), loan.getType()
							.getRepayTimePeriod(), interestBeginTime, loan
							.getType().getInterestType(), loan.getType()
							.getInterestPoint(), null);
		} else if (loan.getType().getRepayType().equals(RepayType.CPM)) {
			repays = normalRepayCPMCalculator.generateRepays(money, null, loan
					.getRate(), loan.getDeadline(), loan.getType()
					.getRepayTimeUnit(), loan.getType().getRepayTimePeriod(),
					interestBeginTime, loan.getType().getInterestType(), loan
							.getType().getInterestPoint(), null);
		} else if (loan.getType().getRepayType().equals(RepayType.RLIO)) {
			repays = normalRepayRLIOCalculator.generateRepays(money, null, loan
					.getRate(), loan.getDeadline(), loan.getType()
					.getRepayTimeUnit(), loan.getType().getRepayTimePeriod(),
					interestBeginTime, loan.getType().getInterestType(), loan
					.getType().getInterestPoint(), null);
		} else {
			throw new IllegalLoanTypeException("RepayType: "
					+ loan.getType().getRepayType() + ". 不支持该还款类型。");
		}
		for (Repay repay : repays) {
			sumInterest = ArithUtil.add(sumInterest, repay.getInterest());
		}
		return sumInterest;
	}

	@Override
	public double calculateOverdueRepayFee(String loanId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AdvanceRepay calculateAdvanceRepay(String loanId) {
		if (StringUtils.isEmpty(loanId)) {
			return null;
		}
		String repaysHql = "select lr from LoanRepay lr where lr.loan.id = ?";
		List<LoanRepay> repays = ht.find(repaysHql, loanId);

		AdvanceRepay ar = new AdvanceRepay();
		// 剩余所有本金
		double sumCorpus = 0D;
		// 手续费总额
		double feeAll = 0D;
		for (LoanRepay repay : repays) {
			if (repay.getStatus().equals(LoanConstants.RepayStatus.REPAYING)) {
				// 在还款期，而且没还款
				if (!repayService.isInRepayPeriod(repay.getRepayDay())) {
					sumCorpus = ArithUtil.add(sumCorpus, repay.getCorpus());
					feeAll = ArithUtil.add(feeAll, repay.getFee());
				}
			}
		}
		// 给投资人的罚金
		double feeToInvestor = feeConfigBO.getFee(
				FeePoint.ADVANCE_REPAY_INVESTOR, FeeType.PENALTY, null, null,
				sumCorpus);
		// 给系统的罚金
		double feeToSystem = feeConfigBO.getFee(FeePoint.ADVANCE_REPAY_SYSTEM,
				FeeType.PENALTY, null, null, sumCorpus);

		ar.setCorpus(sumCorpus);
		ar.setRepayFee(feeAll);
		ar.setFeeToInvestor(feeToInvestor);
		ar.setFeeToSystem(feeToSystem);
		return ar;
	}

}
