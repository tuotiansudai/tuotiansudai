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
 * Description: 先付利息后还本金，正常还款计算器
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-5-21 下午5:40:47
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-5-21 wangzhi 1.0
 */
@Service("normalRepayRLIOCalculator")
public class NormalRepayRLIOCalculator implements NormalRepayCalculator {

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
				return generateDayDayRepays(investMoney, investTime, rate,
						deadline, repayTimePeriod, interestBeginTime,
						interestBeginPoint);
			} else if (repayTimeUnit.equals(RepayUnit.MONTH)) {
				// 按月s还款
				return generateDayMonthRepays(investMoney, investTime, rate,
						deadline, repayTimePeriod, interestBeginTime,
						interestBeginPoint);
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
				throw new IllegalLoanTypeException("interestType: "
						+ interestType + ", repayTimeUnit:" + repayTimeUnit
						+ ". 到期还本付息，不支持按月计息。");
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
	 * 生成按天计息、按月s还款的投资还款数据
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
	 * @param interestBeginPoint
	 *            计息节点（即投即生息，放款后生息 之类）
	 */
	private List<Repay> generateDayMonthRepays(double investMoney,
			Date investTime, double rate, Integer deadline,
			Integer repayTimePeriod, Date interestBeginTime,
			String interestBeginPoint) {
		List<Repay> repays = new ArrayList<Repay>();

		Repay ir = new Repay();
		ir.setDefaultInterest(0D);
		// 只要不是最后一期，无需还本金
		ir.setCorpus(investMoney);

		// interestBeginTime的第二天开始计息
		int interestDays = DateUtil.getIntervalDays(interestBeginTime,
				DateUtil.addMonth(interestBeginTime, deadline));
		if (interestBeginPoint.equals(InterestPoint.INTEREST_BEGIN_ON_INVEST)) {
			// 需处理 即投即生息 所产生的利息
			// 计息的天数，投资后第二天开始计息
			// FIXME:此处有bug,不能往前选超过一个还款周期,不然就会在第一个还款日以后,还有投资出现,就没法计算了.
			interestDays = interestDays
					- DateUtil.calculateIntervalDays(interestBeginTime,
							investTime);
		}
		// 金额*还款周期*还款周期单位*还款周期单位利率 = 利息
		ir.setInterest(ArithUtil.round(rate / 365 * interestDays * investMoney,
				2));
		ir.setLength(deadline);
		ir.setPeriod(1);
		// 计息日+第几期*还款周期=还款日
		ir.setRepayDay(DateUtil.addMonth(interestBeginTime, deadline));
		repays.add(ir);
		return repays;
	}

	/**
	 * 生成按天计息、按天s还款的投资还款数据
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
	 *            还款周期 （两天，三天之类）
	 * @param interestBeginTime
	 *            开始计息时间
	 * @param interestBeginPoint
	 *            计息节点（即投即生息，放款后生息 之类）
	 */
	private List<Repay> generateDayDayRepays(double investMoney,
			Date investTime, double rate, Integer deadline,
			Integer repayTimePeriod, Date interestBeginTime,
			String interestBeginPoint) {
		List<Repay> repays = new ArrayList<Repay>();

		Repay ir = new Repay();
		ir.setDefaultInterest(0D);
		// 只要不是最后一期，无需还本金
		ir.setCorpus(investMoney);

		// interestBeginTime的第二天开始计息
		int interestDays = deadline;
		if (interestBeginPoint.equals(InterestPoint.INTEREST_BEGIN_ON_INVEST)) {
			// 需处理 即投即生息 所产生的利息
			// 计息的天数，投资后第二天开始计息
			// FIXME:此处有bug,不能往前选超过一个还款周期,不然就会在第一个还款日以后,还有投资出现,就没法计算了.
			interestDays = interestDays
					- DateUtil.calculateIntervalDays(interestBeginTime,
							investTime) - 1;
		}
		// 金额*还款周期*还款周期单位*还款周期单位利率 = 利息
		ir.setInterest(ArithUtil.round(rate / 365 * interestDays * investMoney,
				2));
		ir.setLength(deadline);
		ir.setPeriod(1);
		// 计息日+第几期*还款周期单位=还款日
		ir.setRepayDay(DateUtil.addDay(interestBeginTime, deadline));
		repays.add(ir);
		return repays;
	}

}
