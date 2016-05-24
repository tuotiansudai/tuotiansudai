package com.esoft.jdp2p.repay.service.impl;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.esoft.core.annotations.Message;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.config.ConfigConstants.RepayAlert;
import com.esoft.archer.config.service.ConfigService;
import com.esoft.archer.user.UserBillConstants.OperatorInfo;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.jdp2p.invest.InvestConstants.InvestStatus;
import com.esoft.jdp2p.invest.InvestConstants.TransferStatus;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.invest.model.TransferApply;
import com.esoft.jdp2p.invest.service.TransferService;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.LoanConstants.LoanStatus;
import com.esoft.jdp2p.loan.LoanConstants.RepayStatus;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.model.LoanType;
import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.jdp2p.message.MessageConstants;
import com.esoft.jdp2p.message.model.UserMessageTemplate;
import com.esoft.jdp2p.message.service.impl.MessageBO;
import com.esoft.jdp2p.repay.RepayConstants.RepayType;
import com.esoft.jdp2p.repay.RepayConstants.RepayUnit;
import com.esoft.jdp2p.repay.exception.AdvancedRepayException;
import com.esoft.jdp2p.repay.exception.IllegalLoanTypeException;
import com.esoft.jdp2p.repay.exception.NormalRepayException;
import com.esoft.jdp2p.repay.exception.OverdueRepayException;
import com.esoft.jdp2p.repay.exception.RepayException;
import com.esoft.jdp2p.repay.model.InvestRepay;
import com.esoft.jdp2p.repay.model.LoanRepay;
import com.esoft.jdp2p.repay.model.Repay;
import com.esoft.jdp2p.repay.service.NormalRepayCalculator;
import com.esoft.jdp2p.repay.service.RepayService;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeePoint;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeeType;
import com.esoft.jdp2p.risk.service.SystemBillService;
import com.esoft.jdp2p.risk.service.impl.FeeConfigBO;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-22 上午10:35:51
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-22 wangzhi 1.0
 */
@Service("repayService")
public class RepayServiceImpl implements RepayService {

	@Logger
	static Log log;

	@Resource
	HibernateTemplate ht;

	@Resource
	UserBillBO userBillBO;

	@Resource
	SystemBillService systemBillService;

	@Resource
	FeeConfigBO feeConfigBO;

	@Resource
	LoanService loanService;

	@Resource
	TransferService transferService;

	@Resource
	NormalRepayCalculator normalRepayRFCLCalculator;

	@Resource
	NormalRepayCalculator normalRepayCPMCalculator;

	@Resource
	NormalRepayRLIOCalculator normalRepayRLIOCalculator;

	@Resource
	MessageBO messageBO;

	@Resource
	ConfigService configService;
	@Value("${repay.remind.mobileList}")
	private String repayRemindMobileList;

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void normalRepay(String repayId) throws InsufficientBalance,
			NormalRepayException {
		// 正常还款
		LoanRepay repay = ht.get(LoanRepay.class, repayId, LockMode.UPGRADE);
		normalRepay(repay);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void normalRepay(LoanRepay repay) throws InsufficientBalance,
			NormalRepayException {
		ht.evict(repay);
		repay = ht.get(LoanRepay.class, repay.getId(), LockMode.UPGRADE);
		// 正常还款
		if (!(repay.getStatus().equals(LoanConstants.RepayStatus.REPAYING) || repay
				.getStatus()
				.equals(LoanConstants.RepayStatus.WAIT_REPAY_VERIFY))) {
			// 该还款不处于正常还款状态。
			throw new NormalRepayException("还款：" + repay.getId() + "不处于正常还款状态。");
		}
		List<InvestRepay> irs = ht
				.find("from InvestRepay ir where ir.invest.loan.id=? and ir.period=?",
						new Object[] { repay.getLoan().getId(),
								repay.getPeriod() });

		// TODO:投资的所有还款信息加和，判断是否等于借款的还款信息，如果不相等，抛异常

		// 更改投资的还款信息
		for (InvestRepay ir : irs) {

			// FIXME: 记录repayWay信息
			ir.setStatus(LoanConstants.RepayStatus.COMPLETE);
			ir.setTime(new Date());
			ht.update(ir);

			userBillBO.transferIntoBalance(
					ir.getInvest().getUser().getId(),
					ArithUtil.add(ir.getCorpus(), ir.getInterest()),
					OperatorInfo.NORMAL_REPAY,
					"投资：" + ir.getInvest().getId() + "收到还款, 还款ID:"
							+ repay.getId() + "  借款ID:"
							+ repay.getLoan().getId() + "  本金："
							+ ir.getCorpus() + "  利息：" + ir.getInterest());
			// 投资者手续费
			userBillBO.transferOutFromBalance(ir.getInvest().getUser().getId(),
					ir.getFee(), OperatorInfo.INVEST_FEE, "投资："
							+ ir.getInvest().getId() + "收到还款，扣除手续费, 还款ID:"
							+ repay.getId());
			systemBillService.transferInto(ir.getFee(),
					OperatorInfo.INVEST_FEE, "投资：" + ir.getInvest().getId()
							+ "收到还款，扣除手续费, 还款ID:" + repay.getId());

		}

		try {
			cancelTransfering(repay.getLoan().getId());
		} catch (RepayException e) {
			throw new NormalRepayException(e.getMessage(), e.getCause());
		}

		// 更改借款的还款信息
		double payMoney = ArithUtil.add(
				ArithUtil.add(repay.getCorpus(), repay.getInterest()),
				repay.getFee());
		repay.setTime(new Date());
		repay.setStatus(LoanConstants.RepayStatus.COMPLETE);

		// 借款者的账户，扣除还款。
		userBillBO.transferOutFromBalance(
				repay.getLoan().getUser().getId(),
				payMoney,
				OperatorInfo.NORMAL_REPAY,
				"借款：" + repay.getLoan().getId() + "正常还款, 还款ID：" + repay.getId()
						+ " 本金：" + repay.getCorpus() + "  利息："
						+ repay.getInterest() + "  手续费：" + repay.getFee());
		// 借款者手续费
		systemBillService.transferInto(repay.getFee(),
				OperatorInfo.NORMAL_REPAY, "借款：" + repay.getLoan().getId()
						+ "正常还款，扣除手续费， 还款ID：" + repay.getId());

		ht.merge(repay);
		// 判断是否所有还款结束，更改等待还款的投资状态和还款状态，还有项目状态。
		loanService.dealComplete(repay.getLoan().getId());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void generateRepay(String loanId) {
		Loan loan = ht.get(Loan.class, loanId, LockMode.UPGRADE);
		ht.evict(loan);
		loan = ht.get(Loan.class, loanId, LockMode.UPGRADE);
		LoanType loanType = ht.get(LoanType.class, loan.getType().getId());
		if (loan.getInterestBeginTime() == null) {
			// 发起借款的时候，如果不制定计息开始时间，则默认为放款日
			loan.setInterestBeginTime(new Date());
		}
		// 先付利息后还本金
		if (loanType.getRepayType().equals(RepayType.RFCL)) {
			gRepays(loan, normalRepayRFCLCalculator);
		} else if (loanType.getRepayType().equals(RepayType.CPM)) {
			gRepays(loan, normalRepayCPMCalculator);
		} else if (loanType.getRepayType().equals(RepayType.RLIO)) {
			gRepays(loan, normalRepayRLIOCalculator);
		} else {
			throw new IllegalLoanTypeException("RepayType: "
					+ loan.getType().getRepayType() + ". 不支持该还款类型。");
		}
	}

	/**
	 * 生成还款数据
	 * 
	 * @param loan
	 * @param loanType
	 */
	private void gRepays(Loan loan, NormalRepayCalculator normalRepayCalculator) {
		LoanType loanType = loan.getType();
		List<List<InvestRepay>> allInvestRepays = new ArrayList<List<InvestRepay>>();
		List<Invest> invests = loanService.getSuccessfulInvests(loan.getId());
		for (Invest im : invests) {
			List<Repay> repays = normalRepayCalculator.generateRepays(
					im.getMoney(), im.getTime(), loan.getRate(),
					loan.getDeadline(), loanType.getRepayTimeUnit(),
					loanType.getRepayTimePeriod(), loan.getInterestBeginTime(),
					loanType.getInterestType(), loanType.getInterestPoint(),
					null);
			// 保存投资的还款信息
			Double investInterest = 0D;
			List<InvestRepay> irs = new ArrayList<InvestRepay>();
			for (Repay repay : repays) {
				InvestRepay investRepay = new InvestRepay();
				investRepay.setCorpus(repay.getCorpus());
				investRepay.setDefaultInterest(repay.getDefaultInterest());
				// 投资编号+还款第几期（四位，左侧补0）
				investRepay.setId(im.getId()
						+ StringUtils.leftPad(repay.getPeriod().toString(), 4,
								"0"));
				investRepay.setInterest(repay.getInterest());
				investRepay.setInvest(im);
				investRepay.setLength(repay.getLength());
				investRepay.setPeriod(repay.getPeriod());
				investRepay.setRepayDay(repay.getRepayDay());
				investRepay.setStatus(LoanConstants.RepayStatus.REPAYING);
				// 投资者手续费=所得利息*借款中存储的投资者手续费比例
				investRepay.setFee(ArithUtil.round(
						ArithUtil.mul(repay.getInterest(),
								loan.getInvestorFeeRate()), 2));
				investInterest = ArithUtil.add(investInterest,
						investRepay.getInterest());
				ht.save(investRepay);
				irs.add(investRepay);
			}
			ht.update(im);
			allInvestRepays.add(irs);
		}

		generateLoanRepays(loan, loanType, allInvestRepays);
	}

	/**
	 * 生成借款的还款数据
	 * 
	 * @param loan
	 * @param loanType
	 * @param allInvestRepays
	 */
	private void generateLoanRepays(Loan loan, LoanType loanType,
			List<List<InvestRepay>> allInvestRepays) {
		// 创建loanRepays以便保存
		List<LoanRepay> loanRepays = new ArrayList<LoanRepay>();
		// 借款手续费，平均到每笔还款中收取
		Double fee = null;
		if (loanType.getRepayType().equals(RepayType.RLIO)) {
			fee = loan.getFeeOnRepay();
		} else {
			fee = ArithUtil.div(loan.getFeeOnRepay(), loan.getDeadline(), 2);
		}
		for (int i = 1; i <= loan.getDeadline(); i++) {
			// 初始化loanRepay信息
			LoanRepay loanRepay = new LoanRepay();
			loanRepay.setCorpus(0D);
			loanRepay.setDefaultInterest(0D);
			loanRepay.setId(loan.getId()
					+ StringUtils.leftPad(String.valueOf(i), 4, "0"));
			loanRepay.setInterest(0D);
			loanRepay.setLength(loanType.getRepayTimePeriod());
			loanRepay.setLoan(loan);
			// 借款者手续费
			loanRepay.setFee(fee);
			loanRepay.setPeriod(i);
			if (loanType.getRepayTimeUnit().equals(RepayUnit.DAY)) {
				// 按天s还款
				loanRepay.setRepayDay(DateUtil.addDay(
						loan.getInterestBeginTime(),
						i * loanType.getRepayTimePeriod()));
			} else if (loanType.getRepayTimeUnit().equals(RepayUnit.MONTH)) {
				// 按月s还款
				loanRepay.setRepayDay(DateUtil.addMonth(
						loan.getInterestBeginTime(),
						i * loanType.getRepayTimePeriod()));
			}
			loanRepay.setStatus(LoanConstants.RepayStatus.REPAYING);

			loanRepays.add(loanRepay);
			// 到期还本付息，这里有bug
			if (loanType.getRepayType().equals(RepayType.RLIO)) {
				if (loanType.getRepayTimeUnit().equals(RepayUnit.DAY)) {
					// 按天s还款
					loanRepay.setLength(loan.getDeadline());
					// 计息日+第几期*还款周期单位=还款日
					loanRepay.setRepayDay(DateUtil.addDay(
							loan.getInterestBeginTime(), loan.getDeadline()));
				} else if (loanType.getRepayTimeUnit().equals(RepayUnit.MONTH)) {
					// 按月s还款
					loanRepay.setLength(loan.getDeadline());
					// 计息日+第几期*还款周期=还款日
					loanRepay.setRepayDay(DateUtil.addMonth(
							loan.getInterestBeginTime(), loan.getDeadline()));
				}
				break;
			}
		}

		// 根据每笔投资的还款信息，更新借款的还款信息。
		for (List<InvestRepay> irs : allInvestRepays) {
			for (InvestRepay ir : irs) {
				loanRepays.get(ir.getPeriod() - 1).setCorpus(
						ArithUtil.add(loanRepays.get(ir.getPeriod() - 1)
								.getCorpus(), ir.getCorpus()));
				loanRepays.get(ir.getPeriod() - 1).setInterest(
						ArithUtil.add(loanRepays.get(ir.getPeriod() - 1)
								.getInterest(), ir.getInterest()));
			}
		}
		// 保存借款的还款信息
		for (LoanRepay loanRepay : loanRepays) {
			ht.save(loanRepay);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void advanceRepay(String loanId) throws InsufficientBalance,
			AdvancedRepayException {
		Loan loan = ht.get(Loan.class, loanId);
		ht.evict(loan);
		loan = ht.get(Loan.class, loanId, LockMode.UPGRADE);
		if (loan.getStatus().equals(LoanStatus.REPAYING) || loan.getStatus().equals(LoanStatus.WAIT_REPAY_VERIFY)) {
			// 查询当期还款是否已还清
			String repaysHql = "select lr from LoanRepay lr where lr.loan.id = ?";
			List<LoanRepay> repays = ht.find(repaysHql, loanId);
			// 剩余所有本金
			double sumCorpus = 0D;
			// 手续费总额
			double feeAll = 0D;
			for (LoanRepay repay : repays) {
				if (repay.getStatus()
						.equals(LoanConstants.RepayStatus.REPAYING)
						|| repay.getStatus().equals(
								RepayStatus.WAIT_REPAY_VERIFY)) {
					// 在还款期，而且没还款
					if (isInRepayPeriod(repay.getRepayDay())) {
						// 有未完成的当期还款。
						throw new AdvancedRepayException("当期还款未完成");
					} else {
						sumCorpus = ArithUtil.add(sumCorpus, repay.getCorpus());
						feeAll = ArithUtil.add(feeAll, repay.getFee());
						repay.setTime(new Date());
						repay.setStatus(LoanConstants.RepayStatus.COMPLETE);
					}
				} else if (repay.getStatus().equals(
						LoanConstants.RepayStatus.BAD_DEBT)
						|| repay.getStatus().equals(
								LoanConstants.RepayStatus.OVERDUE)) {
					// 还款中存在逾期或者坏账
					throw new AdvancedRepayException("还款中存在逾期或者坏账");
				}
			}

			// 给投资人的罚金
			double feeToInvestor = feeConfigBO.getFee(
					FeePoint.ADVANCE_REPAY_INVESTOR, FeeType.PENALTY, null,
					null, sumCorpus);
			// 给系统的罚金
			double feeToSystem = feeConfigBO.getFee(
					FeePoint.ADVANCE_REPAY_SYSTEM, FeeType.PENALTY, null, null,
					sumCorpus);

			double sumPay = ArithUtil.add(feeToInvestor, feeToSystem,
					sumCorpus, feeAll);
			// 扣除还款金额+罚金
			userBillBO.transferOutFromBalance(loan.getUser().getId(), sumPay,
					OperatorInfo.ADVANCE_REPAY, "借款：" + loan.getId()
							+ "提前还款，本金：" + sumCorpus + "，用户罚金：" + feeToInvestor
							+ "，系统罚金：" + feeToSystem + "，借款手续费：" + feeAll);
			// 余额不足，抛异常
			// 按比例分给投资人和系统（默认优先给投资人，剩下的给系统，以防止提转差额的出现）
			List<InvestRepay> irs = ht
					.find("from InvestRepay ir where ir.invest.loan.id=? and ir.status=?",
							new Object[] { loan.getId(), RepayStatus.REPAYING });

			double feeToInvestorTemp = feeToInvestor;
			// 更改投资的还款信息
			for (int i = 0; i < irs.size(); i++) {
				InvestRepay ir = irs.get(i);

				// FIXME: 记录repayWay信息
				ir.setStatus(LoanConstants.RepayStatus.COMPLETE);
				ir.setTime(new Date());

				// 罚金
				double cashFine;
				if (i == irs.size() - 1) {
					cashFine = feeToInvestorTemp;
				} else {
					cashFine = ArithUtil.round(ir.getCorpus() / sumCorpus
							* feeToInvestor, 2);
					feeToInvestorTemp = ArithUtil.sub(feeToInvestorTemp,
							cashFine);
				}

				userBillBO.transferIntoBalance(
						ir.getInvest().getUser().getId(),
						ArithUtil.add(ir.getCorpus(), cashFine),
						OperatorInfo.ADVANCE_REPAY, "投资："
								+ ir.getInvest().getId() + "收到还款" + "  本金："
								+ ir.getCorpus() + "  罚息：" + cashFine);
			}

			try {
				cancelTransfering(loanId);
			} catch (RepayException e) {
				throw new AdvancedRepayException(e.getMessage(), e.getCause());
			}

			systemBillService.transferInto(ArithUtil.add(feeToSystem, feeAll),
					OperatorInfo.ADVANCE_REPAY,
					"提前还款罚金及借款手续费到账，借款ID:" + loan.getId());

			// 改项目状态。
			loan.setStatus(LoanConstants.LoanStatus.COMPLETE);
			ht.merge(loan);
			loanService.dealComplete(loan.getId());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void overdueRepay(String repayId) throws InsufficientBalance,
			OverdueRepayException {
		LoanRepay lr = ht.get(LoanRepay.class, repayId);
		ht.evict(lr);
		lr = ht.get(LoanRepay.class, repayId, LockMode.UPGRADE);
		if (lr.getStatus().equals(LoanConstants.RepayStatus.OVERDUE)
				|| lr.getStatus().equals(LoanConstants.RepayStatus.BAD_DEBT)
				|| lr.getStatus().equals(RepayStatus.WAIT_REPAY_VERIFY)) {
			List<InvestRepay> irs = ht
					.find("from InvestRepay ir where ir.invest.loan.id=? and ir.period=?",
							new Object[] { lr.getLoan().getId(), lr.getPeriod() });

			double defaultInterest = lr.getDefaultInterest();
			// 更改投资的还款信息
			for (InvestRepay ir : irs) {
				ir.setStatus(LoanConstants.RepayStatus.COMPLETE);
				ir.setTime(new Date());
				ht.update(ir);

				userBillBO.transferIntoBalance(
						ir.getInvest().getUser().getId(),
						ArithUtil.add(ir.getCorpus(), ir.getInterest(),
								ir.getDefaultInterest()),
						OperatorInfo.OVERDUE_REPAY,
						"投资：" + ir.getInvest().getId() + "收到还款, 还款ID:"
								+ lr.getId() + "  借款ID:" + lr.getLoan().getId()
								+ "  本金：" + ir.getCorpus() + "  利息："
								+ ir.getInterest() + "  罚息："
								+ ir.getDefaultInterest());
				defaultInterest = ArithUtil.sub(defaultInterest,
						ir.getDefaultInterest());
				// 投资者手续费
				userBillBO.transferOutFromBalance(ir.getInvest().getUser()
						.getId(), ir.getFee(), OperatorInfo.INVEST_FEE,
						"投资：" + ir.getInvest().getId() + "收到还款，扣除手续费, 还款ID:"
								+ lr.getId());
				systemBillService.transferInto(ir.getFee(),
						OperatorInfo.INVEST_FEE, "投资："
								+ ir.getInvest().getId() + "收到还款，扣除手续费, 还款ID:"
								+ lr.getId());
			}

			// 更改借款的还款信息
			double payMoney = ArithUtil.add(
					ArithUtil.add(lr.getCorpus(), lr.getInterest()),
					lr.getFee(), lr.getDefaultInterest());
			lr.setTime(new Date());
			lr.setStatus(LoanConstants.RepayStatus.COMPLETE);

			// 投资者的借款账户，扣除还款。
			userBillBO.transferOutFromBalance(
					lr.getLoan().getUser().getId(),
					payMoney,
					OperatorInfo.OVERDUE_REPAY,
					"借款：" + lr.getLoan().getId() + "逾期还款, 还款ID：" + lr.getId()
							+ " 本金：" + lr.getCorpus() + "  利息："
							+ lr.getInterest() + "  手续费：" + lr.getFee()
							+ "  罚息：" + lr.getDefaultInterest());
			// 借款者手续费
			systemBillService.transferInto(lr.getFee(),
					OperatorInfo.OVERDUE_REPAY, "借款：" + lr.getLoan().getId()
							+ "逾期还款，扣除手续费， 还款ID：" + lr.getId());
			// 罚息转入网站账户
			systemBillService.transferInto(defaultInterest,
					OperatorInfo.OVERDUE_REPAY, "借款：" + lr.getLoan().getId()
							+ "逾期还款，扣除罚金， 还款ID：" + lr.getId());
			ht.merge(lr);
			Long count = (Long) ht
					.find("select count(repay) from LoanRepay repay where repay.loan.id=? and (repay.status=? or repay.status=?)",
							lr.getLoan().getId(), RepayStatus.OVERDUE, RepayStatus.BAD_DEBT).get(0);
			if(count == 0){
				//如果没有逾期或者坏账的还款，则更改借款状态。
				lr.getLoan().setStatus(LoanStatus.REPAYING);
				ht.update(lr.getLoan());
			}
			// 判断是否所有还款结束，更改等待还款的投资状态和还款状态，还有项目状态。
			loanService.dealComplete(lr.getLoan().getId());
			try {
				cancelTransfering(lr.getLoan().getId());
			} catch (RepayException e) {
				throw new OverdueRepayException(e.getMessage(), e.getCause());
			}
		} else {
			throw new OverdueRepayException("还款不处于逾期还款状态");
		}
	}

	/**
	 * 还款时候，取消正在转让的债权
	 * 
	 * @param loanId
	 * @throws RepayException
	 */
	private void cancelTransfering(String loanId) throws RepayException {
		// 取消投资下面的所有正在转让的债权
		String hql = "from TransferApply ta where ta.invest.loan.id=? and ta.status=?";
		List<TransferApply> tas = ht.find(hql, new String[] { loanId,
				TransferStatus.WAITCONFIRM });
		if (tas.size() > 0) {
			// 有购买了等待第三方确认的债权，所以不能还款。
			throw new RepayException("有等待第三方确认的债权转让，不能还款！");
		}
		tas = ht.find(hql, new String[] { loanId, TransferStatus.TRANSFERING });
		for (TransferApply ta : tas) {
			transferService.cancel(ta.getId());
		}
	}

	@Override
	public void overdueRepayByAdmin(String repayId, String adminUserId) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void checkLoanOverdue() {
		if (log.isDebugEnabled()) {
			log.debug("checkLoanOverdue start");
		}
		List<LoanRepay> lrs = ht
				.find("from LoanRepay repay where repay.status !='"
						+ LoanConstants.RepayStatus.COMPLETE + "'");
		for (LoanRepay lr : lrs) {
			ht.lock(lr, LockMode.UPGRADE);
			Loan loan = lr.getLoan();
			ht.lock(loan, LockMode.UPGRADE);
			List<InvestRepay> irs = ht
					.find("from InvestRepay ir where ir.invest.loan.id=? and ir.period=?",
							new Object[] { lr.getLoan().getId(), lr.getPeriod() });

			if (lr.getStatus().equals(LoanConstants.RepayStatus.REPAYING)) {
				Date repayDay = DateUtil.addDay(
						DateUtil.StringToDate(DateUtil.DateToString(
								lr.getRepayDay(), DateStyle.YYYY_MM_DD_CN)), 1);
				if (repayDay.before(new Date())) {
					lr.setStatus(LoanConstants.RepayStatus.OVERDUE);
					// FIXME:冻结用户，只允许还钱，其他都不能干。
					loan.setStatus(LoanConstants.LoanStatus.OVERDUE);
					for (InvestRepay ir : irs) {
						ir.setStatus(RepayStatus.OVERDUE);
						ir.getInvest().setStatus(InvestStatus.OVERDUE);
						ht.update(ir.getInvest());
						ht.update(ir);
					}
				}
			}

			if (lr.getStatus().equals(LoanConstants.RepayStatus.OVERDUE)) {
				if (log.isDebugEnabled()) {
					log.debug("checkLoanOverdue overdue repayId:" + lr.getId());
				}
				// 计算逾期罚息, 用户罚息+网站罚息
				double defalutInterestAll = 0D;
				for (InvestRepay ir : irs) {
					// 单笔投资罚息
					double overdueAllMoney = ArithUtil.mul(
							ArithUtil.add(ir.getCorpus(), ir.getInterest()),
							DateUtil.getIntervalDays(new Date(),
									ir.getRepayDay()));
					ir.setDefaultInterest(feeConfigBO.getFee(
							FeePoint.OVERDUE_REPAY_INVESTOR, FeeType.PENALTY,
							null, null, overdueAllMoney));
					defalutInterestAll = ArithUtil.add(defalutInterestAll,
							ir.getDefaultInterest());
				}
				ht.update(lr);
				// 网站罚息
				double overdueLRAllMoney = ArithUtil.mul(
						ArithUtil.add(lr.getCorpus(), lr.getInterest()),
						DateUtil.getIntervalDays(new Date(), lr.getRepayDay()));
				// 用户罚息+网站罚息
				lr.setDefaultInterest(ArithUtil
						.add(defalutInterestAll, feeConfigBO.getFee(
								FeePoint.OVERDUE_REPAY_SYSTEM, FeeType.PENALTY,
								null, null, overdueLRAllMoney)));
				if (DateUtil.addYear(lr.getRepayDay(), 1).before(new Date())) {
					// 逾期一年以后，项目改为还账状态
					if (log.isDebugEnabled()) {
						log.debug("checkLoanOverdue badDebt repayId:"
								+ lr.getId());
					}
					lr.setStatus(LoanConstants.RepayStatus.BAD_DEBT);
					loan.setStatus(LoanConstants.LoanStatus.BAD_DEBT);
					for (InvestRepay ir : irs) {
						ir.setStatus(RepayStatus.BAD_DEBT);
						ir.getInvest().setStatus(InvestStatus.BAD_DEBT);
						ht.update(ir.getInvest());
						ht.update(ir);
					}
				}
			}
			ht.update(lr);
			ht.update(loan);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void autoRepay() {
		if (log.isDebugEnabled()) {
			log.debug("autoRepay start");
		}
		List<LoanRepay> lrs = ht.find(
				"from LoanRepay repay where repay.status =?",
				LoanConstants.RepayStatus.REPAYING);
		for (LoanRepay lr : lrs) {
			ht.lock(lr, LockMode.UPGRADE);
			Loan loan = lr.getLoan();
			ht.lock(loan, LockMode.UPGRADE);
			List<InvestRepay> irs = ht
					.find("from InvestRepay ir where ir.invest.loan.id=? and ir.period=?",
							new Object[] { lr.getLoan().getId(), lr.getPeriod() });
			Date repayDay = DateUtil.addDay(
					DateUtil.StringToDate(DateUtil.DateToString(
							lr.getRepayDay(), DateStyle.YYYY_MM_DD_CN)), 1);
			if (repayDay.before(new Date())) {
				// 到还款日了，自动扣款
				try {
					normalRepay(lr);
				} catch (InsufficientBalance e) {
					// 账户余额不足，则逾期
					if (log.isDebugEnabled()) {
						log.debug("autoRepay InsufficientBalance overdue repayId:"
								+ lr.getId());
					}
					lr.setStatus(LoanConstants.RepayStatus.OVERDUE);
					// FIXME:冻结用户，只允许还钱，其他都不能干。
					loan.setStatus(LoanConstants.LoanStatus.OVERDUE);
					for (InvestRepay ir : irs) {
						ir.setStatus(RepayStatus.OVERDUE);
						ir.getInvest().setStatus(InvestStatus.OVERDUE);
						ht.update(ir.getInvest());
						ht.update(ir);
					}
				} catch (NormalRepayException e) {
					throw new RuntimeException(e);
				}
			}
			ht.update(lr);
			ht.update(loan);
		}
	}

	@Override
	public boolean isInRepayPeriod(Date repayDate) {
		repayDate = DateUtil.StringToDate(DateUtil.DateToString(repayDate,
				DateStyle.YYYY_MM_DD_CN));
		Date now = new Date();
		Date upperLimit = DateUtil.addMonth(repayDate, -1);
		repayDate = DateUtil.addMinute(repayDate, 1439);
		// 还款日上推一个月，算是还款期。
		if ((now.before(repayDate)) && (!now.before(upperLimit))) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public void repayAlert() {
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
		int daysBefore = Integer.parseInt(configService
				.getConfigValue(RepayAlert.DAYS_BEFORE));
		String repayDate = simple.format(DateUtil.addDay(new Date(), daysBefore));
		log.debug("repayDate:====" + repayDate);
		String loanRepayAmountSql = "select ifnull(round(sum(corpus) + sum(default_interest) + sum(interest),2),0.00) from loan_repay"
				+ " where status = '"+ LoanConstants.RepayStatus.REPAYING + "' and repay_day like '" + repayDate + "%'";
		Double loanRepayAmount = ((Number)ht.getSessionFactory().getCurrentSession()
									.createSQLQuery(loanRepayAmountSql)
									.uniqueResult()).doubleValue();
		String mobileList = repayRemindMobileList;

		if(loanRepayAmount.doubleValue() > 0){
			String loanRepaySql = "select * from loan_repay"
					+ " where status = '"+ LoanConstants.RepayStatus.REPAYING + "' and repay_day like '" + repayDate + "%'";
			List<LoanRepay> loanRepays = ht.getSessionFactory().getCurrentSession()
											.createSQLQuery(loanRepaySql)
											.addEntity(LoanRepay.class)
											.list();
			for (LoanRepay loanRepay : loanRepays) {
				if(mobileList.indexOf(loanRepay.getLoan().getUser().getMobileNumber()) < 0){
					mobileList += loanRepay.getLoan().getUser().getMobileNumber() + ",";
				}
			}
			log.debug("repayAlert|mobile:" + mobileList + ";date:" + DateUtil.addDay(new Date(), daysBefore) + ";loanRepayAmount:" + loanRepayAmount) ;
			Map<String, String> params = new HashMap<String, String>();
			params.put("loanRepayAmount", loanRepayAmount.toString());
			String[] mobiles = mobileList.split(",");
			for(String num:mobiles){
				if(org.apache.commons.lang3.StringUtils.isNotEmpty(num)){
					messageBO.sendSMS(ht.get(UserMessageTemplate.class,
									MessageConstants.UserMessageNodeId.REPAY_ALERT + "_sms"),
							params, num);
				}
			}
		}
	}
}
