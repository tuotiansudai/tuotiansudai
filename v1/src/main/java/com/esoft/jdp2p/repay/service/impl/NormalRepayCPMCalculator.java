package com.esoft.jdp2p.repay.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.DateUtil;
import com.esoft.jdp2p.repay.RepayConstants.InterestPoint;
import com.esoft.jdp2p.repay.RepayConstants.InterestType;
import com.esoft.jdp2p.repay.RepayConstants.RepayUnit;
import com.esoft.jdp2p.repay.exception.IllegalLoanTypeException;
import com.esoft.jdp2p.repay.model.Repay;
import com.esoft.jdp2p.repay.model.RepayCustomPeriod;
import com.esoft.jdp2p.repay.service.NormalRepayCalculator;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 等额本息，正常还款计算器
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-5-21 下午5:40:47
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-5-21 wangzhi 1.0
 */
@Service("normalRepayCPMCalculator")
public class NormalRepayCPMCalculator implements NormalRepayCalculator {

	@Override
	public List<Repay> generateRepays(double investMoney, Date investTime,
			double rate, Integer deadline, String repayTimeUnit,
			Integer repayTimePeriod, Date interestBeginTime,
			String interestType, String interestBeginPoint,
			List<RepayCustomPeriod> customPeriods) {
		// TODO:自定义（不等额分期还款）尚未实现
		if (interestType.equals(InterestType.DAY)) {
			// 按天计息
			if (repayTimeUnit.equals(RepayUnit.DAY)) {
				// 按天s还款
				throw new IllegalLoanTypeException("interestType: "
						+ interestType + ", repayTimeUnit:" + repayTimeUnit
						+ ". 等额本息，不支持按天计息。");
			} else if (repayTimeUnit.equals(RepayUnit.MONTH)) {
				// 按月s还款
				throw new IllegalLoanTypeException("interestType: "
						+ interestType + ", repayTimeUnit:" + repayTimeUnit
						+ ". 等额本息，不支持按天计息。");
			}
		} else if (interestType.equals(InterestType.MONTH)) {
			// 按月计息
			if (interestBeginPoint
					.equals(InterestPoint.INTEREST_BEGIN_ON_INVEST)) {
				// 不支持即投即生息
				throw new IllegalLoanTypeException("interestType: "
						+ interestType + ", interestPoint:"
						+ interestBeginPoint + ". 按月计息不支持即投即生息。");
			}
			if (repayTimeUnit.equals(RepayUnit.MONTH)) {
				// 按月s还款
				return generateDayMonthRepays(investMoney, rate, deadline,
						repayTimePeriod, interestBeginTime);
			} else if (repayTimeUnit.equals(RepayUnit.DAY)) {
				// 按天s还款
				// 按月计息，按天还款，抛异常
				throw new IllegalLoanTypeException("interestType: "
						+ interestType + ", repayTimeUnit:" + repayTimeUnit
						+ ". 按月计息不支持按天还款。");
			}
		}
		throw new IllegalLoanTypeException("interestType: " + interestType
				+ ", repayTimeUnit:" + repayTimeUnit + ". 不支持该借款类型。");
	}

	/**
	 * 生成按月计息、按月s还款的投资还款数据
	 * 
	 * @param investMoney
	 *            投资金额
	 * @param investTime
	 *            投资时间
	 * @param rate
	 *            年利率
	 * @param deadline
	 *            总期数
	 * @param repayTimePeriod
	 *            还款周期 （两月，三月之类）
	 * @param interestBeginTime
	 *            开始计息时间
	 */
	private List<Repay> generateDayMonthRepays(double investMoney, double rate,
			Integer deadline, Integer repayTimePeriod, Date interestBeginTime) {
		List<Repay> repays = new ArrayList<Repay>();
		for (int i = 1; i <= deadline; i++) {
			Repay ir = new Repay();
			ir.setDefaultInterest(0D);

			double corpus = ArithUtil
					.round((investMoney * (rate / 12) * Math.pow(
							(1 + rate / 12), i - 1))
							/ (Math.pow((1 + rate / 12), deadline) - 1), 2);

			ir.setCorpus(corpus);

			double interest = ArithUtil.round(
					(investMoney * (rate / 12) * (Math.pow((1 + rate / 12),
							deadline) - Math.pow((1 + rate / 12), i - 1)))
							/ (Math.pow((1 + rate / 12), deadline) - 1), 2);
			ir.setInterest(interest);
			ir.setLength(repayTimePeriod);
			ir.setPeriod(i);
			// 计息日+第几期*还款周期=还款日
			ir.setRepayDay(DateUtil.addMonth(interestBeginTime, i
					* repayTimePeriod));
			repays.add(ir);
		}
		return repays;
	}

}
