package com.esoft.jdp2p.invest.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.LockMode;
import org.hibernate.ObjectNotFoundException;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdScheduler;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.config.ConfigConstants;
import com.esoft.archer.config.service.ConfigService;
import com.esoft.archer.user.UserBillConstants.OperatorInfo;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.InvestConstants.InvestStatus;
import com.esoft.jdp2p.invest.InvestConstants.TransferStatus;
import com.esoft.jdp2p.invest.exception.ExceedInvestTransferMoney;
import com.esoft.jdp2p.invest.exception.InvestTransferException;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.invest.model.TransferApply;
import com.esoft.jdp2p.invest.service.TransferService;
import com.esoft.jdp2p.loan.LoanConstants.LoanStatus;
import com.esoft.jdp2p.loan.LoanConstants.RepayStatus;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.repay.model.InvestRepay;
import com.esoft.jdp2p.repay.model.RepayRoadmap;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeePoint;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeeType;
import com.esoft.jdp2p.risk.service.SystemBillService;
import com.esoft.jdp2p.risk.service.impl.FeeConfigBO;
import com.esoft.jdp2p.schedule.ScheduleConstants;
import com.esoft.jdp2p.schedule.job.CheckInvestTransferOverExpectTime;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

@Service("transferService")
public class TransferServiceImpl implements TransferService {

	@Resource
	HibernateTemplate ht;

	@Resource
	FeeConfigBO feeConfigBO;

	@Resource
	ConfigService configService;

	@Resource
	StdScheduler scheduler;

	@Resource
	UserBillBO userBillBO;

	@Resource
	SystemBillService sbs;

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void applyInvestTransfer(TransferApply ta)
			throws ExceedInvestTransferMoney {
		RepayRoadmap investRRM = ta.getInvest().getRepayRoadmap();

		// (负的本债权未收利息-未还投资手续费) <= 折让金 <= (本债权未收本金-转让手续费)
		if (ArithUtil.add(-investRRM.getUnPaidInterest(),
				investRRM.getUnPaidFee()) > ta.getPremium()
				|| ta.getPremium() > ArithUtil.sub(investRRM.getUnPaidCorpus(),
						calculateFee(ta))) {
			throw new ExceedInvestTransferMoney("折让金区间不正确");
		}

		if (ta.getPremium() > 0) {
			String canBigger = configService
					.getConfigValue(ConfigConstants.InvestTransfer.CAN_GREATER_THAN_SELF_WORTH);
			if (!"1".equals(canBigger)) {
				throw new ExceedInvestTransferMoney("转让金额不能大于可转让金额");
			}
		} else if (ta.getPremium() < 0) {
			String canLess = configService
					.getConfigValue(ConfigConstants.InvestTransfer.CAN_LESS_THAN_SELF_WORTH);
			if (!"1".equals(canLess)) {
				throw new ExceedInvestTransferMoney("转让金额不能小于可转让金额");
			}
		} else {// 如果相等
			String canEquel;
			try {
				canEquel = configService
						.getConfigValue(ConfigConstants.InvestTransfer.CAN_EQUAL_SELF_WORTH);
			} catch (ObjectNotFoundException onfe) {
				canEquel = "1";
			}
			if (!"1".equals(canEquel)) {
				throw new ExceedInvestTransferMoney("转让金额不能等于可转让金额");
			}
		}
		// 债权转让期限，到期自动取消转让
		String timelimit;
		try {
			timelimit = configService
					.getConfigValue(ConfigConstants.InvestTransfer.DEAD_LINE);
		} catch (ObjectNotFoundException onfe) {
			timelimit = "7";
		}
		// 到期时间
		Date deadline = DateUtil.addDay(new Date(), Integer.valueOf(timelimit));

		// 转让期限
		ta.setDeadline(deadline);
		ta.setApplyTime(new Date());

		ta.setId(IdGenerator.randomUUID());
		ta.setStatus(TransferStatus.TRANSFERING);

		ht.save(ta);

		// 在转让有效期内未达成转让的，自动撤销其转让申请。
		JobDetail jobDetail = JobBuilder
				.newJob(CheckInvestTransferOverExpectTime.class)
				.withIdentity(
						ta.getId(),
						ScheduleConstants.JobGroup.CHECK_INVEST_TRANSFER_OVER_EXPECT_TIME)
				.build();// 任务名，任务组，任务执行类
		jobDetail.getJobDataMap().put(
				CheckInvestTransferOverExpectTime.INVEST_TRANSFER_ID,
				ta.getId());
		SimpleTrigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(
						ta.getId(),
						ScheduleConstants.TriggerGroup.CHECK_INVEST_TRANSFER_OVER_EXPECT_TIME)
				.forJob(jobDetail)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule())
				.startAt(ta.getDeadline()).build();
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void transfer(String transferApplyId, String userId,
			double transferCorpus) throws InsufficientBalance,
			ExceedInvestTransferMoney {
		// 购买债权，就是一笔投资啊，不过得考虑部分购买。
		double remainCorpus = calculateRemainCorpus(transferApplyId);
		// 出价必须大于0，小于可购买的金额
		if (transferCorpus <= 0 || transferCorpus > remainCorpus) {
			throw new ExceedInvestTransferMoney("购买本金必须小于等于" + remainCorpus
					+ "且大于0");
		}

		TransferApply ta = ht.get(TransferApply.class, transferApplyId);
		ht.evict(ta);
		ta = ht.get(TransferApply.class, transferApplyId, LockMode.UPGRADE);

		// 购买的本金占所有转让本金的比例。
		double corpusRateInAll = ArithUtil.div(transferCorpus, ta.getCorpus());
		//转让本金占持有本金的本例
		double transferCorpusRate = ArithUtil.div(transferCorpus, ta.getInvest().getMoney());

		// 判断ta是否都被购买了
		if (remainCorpus == transferCorpus) {
			// 债权全部被购买，债权转让完成
			ta.setStatus(TransferStatus.TRANSFED);
		}

		Invest investNew = new Invest();
		investNew.setUser(new User(userId));
		investNew.setInvestMoney(transferCorpus);
		investNew.setIsAutoInvest(false);
		investNew.setMoney(transferCorpus);
		investNew.setStatus(InvestConstants.InvestStatus.REPAYING);
		investNew.setRate(ta.getInvest().getRate());
		investNew.setTime(new Date());
		investNew.setTransferApply(ta);
		investNew.setLoan(ta.getInvest().getLoan());
		investNew.setId(IdGenerator.randomUUID());

		ht.save(investNew);

		// 减去invest中持有的本金
		ta.getInvest().setMoney(
				ArithUtil.sub(ta.getInvest().getMoney(), transferCorpus));
		if (ta.getInvest().getMoney() == 0) {
			// 投资全部被转让，则投资状态变为“完成”。
			ta.getInvest().setStatus(InvestStatus.COMPLETE);
		}

		
		// 债权的购买金额：债权的价格*corpusRate
		double buyPrice = ArithUtil.mul(ta.getPrice(), corpusRateInAll, 2);
		/**caijinmin 增加债权转让成功状态 201501222046 begin*/
		userBillBO.transferOutFromBalance(userId, buyPrice,
				OperatorInfo.TRANSFER_BUY, "购买债权，编号：" + investNew.getId());
		/**caijinmin 增加债权转让成功状态 201501222046 end*/
		// 购买时候，扣除手续费，从转让人收到的金额中扣除。费用根据购买价格计算
		double fee = feeConfigBO.getFee(FeePoint.TRANSFER, FeeType.FACTORAGE,
				null, null, buyPrice);
		// 购买人转出，原持有人转入，手续费转入系统
		sbs.transferInto(fee, OperatorInfo.TRANSFER,
				"购买债权手续费，编号：" + investNew.getId());
		userBillBO
				.transferIntoBalance(ta.getInvest().getUser().getId(),
						buyPrice, OperatorInfo.TRANSFER, "债权转让成功，编号："
								+ transferApplyId);
		userBillBO.transferOutFromBalance(ta.getInvest().getUser().getId(),
				fee, OperatorInfo.TRANSFER, "债权转让成功手续费，编号：" + transferApplyId);
		// 生成购买债权后的还款数据，调整之前的还款数据
		for (Iterator iterator = ta.getInvest().getInvestRepays().iterator(); iterator.hasNext();) {
			InvestRepay ir =  (InvestRepay) iterator.next();
			if (ir.getStatus().equals(RepayStatus.WAIT_REPAY_VERIFY)
					|| ir.getStatus().equals(RepayStatus.OVERDUE)
					|| ir.getStatus().equals(RepayStatus.BAD_DEBT)) {
				throw new RuntimeException("investRepay with status "
						+ RepayStatus.WAIT_REPAY_VERIFY + "exist!");
			} else if (ir.getStatus().equals(RepayStatus.REPAYING)) {
				// 根据购买本金比例，生成债权还款信息
				InvestRepay irNew = new InvestRepay();
				try {
					BeanUtils.copyProperties(irNew, ir);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			    irNew.setId(IdGenerator.randomUUID());
//			    还款本金*转让本金比例*购买本金比例
				irNew.setCorpus(ArithUtil.mul(ir.getCorpus(),transferCorpusRate, 2));
				irNew.setDefaultInterest(ArithUtil.mul(ir.getDefaultInterest(),transferCorpusRate,2));
				irNew.setFee(ArithUtil.mul(ir.getFee(),transferCorpusRate, 2));
				irNew.setInterest(ArithUtil.mul(ir.getInterest(),transferCorpusRate, 2));
				irNew.setInvest(investNew);
				// 修改原投资的还款信息
				ir.setCorpus(ArithUtil.sub(ir.getCorpus(),
						irNew.getCorpus()));
				ir.setDefaultInterest(ArithUtil.sub(
						ir.getDefaultInterest(), irNew.getDefaultInterest()));
				ir.setFee(ArithUtil.sub(ir.getFee(), irNew.getFee()));
				ir.setInterest(ArithUtil.sub(ir.getInterest(),
						irNew.getInterest()));
				ht.merge(irNew);
				if (ir.getCorpus()+ir.getInterest() == 0) {
					ht.delete(ir);
					iterator.remove();
				}else{
				    ht.update(ir);
				}
			}
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public double calculateInvestTransferCompletedRate(String transferApplyId)
			throws NoMatchingObjectsException {
		TransferApply ta = ht.get(TransferApply.class, transferApplyId);
		return ArithUtil.sub(1, ArithUtil.div(
				calculateRemainCorpus(ta.getId()), ta.getCorpus(), 4)) * 100;
	}

	@Override
	public double calculateRemainCorpus(String transferApplyId) {
		// 查询该债权下所有的投资
		TransferApply ta = ht.get(TransferApply.class, transferApplyId);
		double transferedCorpus = 0D;
		String freezeMoney = "";
		try{
			freezeMoney = configService.getConfigValue("freeze_money");
		}catch(ObjectNotFoundException e){
			freezeMoney = "";
		}
		for (Invest i : ta.getInvests()) {
			if("0".equals(freezeMoney)){
				if (!InvestStatus.CANCEL.equals(i.getStatus()) && !InvestStatus.WAIT_AFFIRM.equals(i.getStatus())) {
					transferedCorpus = ArithUtil.add(i.getInvestMoney(),
							transferedCorpus);
				}
			}else{
				if (!InvestStatus.CANCEL.equals(i.getStatus())) {
					transferedCorpus = ArithUtil.add(i.getInvestMoney(),
							transferedCorpus);
				}
			}
		}
		return ArithUtil.sub(ta.getCorpus(), transferedCorpus);
	}

	@Override
	public boolean canTransfer(String investId) throws InvestTransferException {
		Invest invest = ht.get(Invest.class, investId);
		if (invest == null) {
			throw new ObjectNotFoundException(investId, Invest.class.getName());
		}
		
		//有正在等待第三方确认的还款，false
		if (invest.getLoan().getStatus().equals(LoanStatus.WAIT_REPAY_VERIFY)) {
			return false;
		}

		// 已经是债权转入的，不允许再次转让
		if (invest.getTransferApply() != null) {
			return false;
		}

		// 有正在进行部分债权转让的投资，不允许再次申请债权转让
		List<TransferApply> tas = Lists.newArrayList(Collections2.filter(
				invest.getTransferApplies(), new Predicate<TransferApply>() {
					public boolean apply(TransferApply transferApply) {
						return transferApply.getStatus().equals(
								TransferStatus.TRANSFERING)
								|| transferApply.getStatus().equals(
										TransferStatus.WAITCONFIRM);
					}
				}));

		if (tas.size() > 0) {
			return false;
		}

		// 1、投资人持有该债权满一个月。
		// 查询持有该债权多长时间
		String paidRepayMin;
		try {
			// 债权的最少持有时间
			paidRepayMin = configService
					.getConfigValue(ConfigConstants.InvestTransfer.PAID_REPAY_COUNT_MIN);
		} catch (ObjectNotFoundException onfe) {
			// 默认值为1
			paidRepayMin = "1";
		}
		Integer paidRepaySize = invest.getRepayRoadmap().getPaidPeriod();
		if (paidRepaySize < Integer.valueOf(paidRepayMin)) {
			// 不合格
			// FIXME:最好能提示不合格的原因，下同。
			return false;
		}
		// 要转让的债权剩余期数大于或等于?期，
		String remainRepayCountMin;
		try {
			remainRepayCountMin = configService
					.getConfigValue(ConfigConstants.InvestTransfer.REMAIN_REPAY_COUNT_MIN);
		} catch (ObjectNotFoundException onfe) {
			remainRepayCountMin = "3";
		}

		if (invest.getRepayRoadmap().getUnPaidPeriod() < Integer
				.valueOf(remainRepayCountMin)) {
			// 不合格
			return false;
		}
		// 剩余本金大于或等于?元。
		String remainCorpus;
		try {
			remainCorpus = configService
					.getConfigValue(ConfigConstants.InvestTransfer.REMAIN_CORPUS_MIN);
		} catch (ObjectNotFoundException onfe) {
			remainCorpus = "1000";
		}
		if (invest.getRepayRoadmap().getUnPaidCorpus() < Integer
				.valueOf(remainCorpus)) {
			// 不合格
			return false;
		}
		// 在申请日，该债权必须是正常还款中状态方可申请转让。当前为逾期状态不能转让，当期已收到全部或部分还款不能转让。
		if (!invest.getStatus().equals(InvestConstants.InvestStatus.REPAYING)) {
			// 不合格
			return false;
		}
		// 转让申请应为一个非还款日，至少在还款日的前?天
		// 下一个合约还款日
		Date today = new Date();// 当前日期
		String deadLine;
		try {
			deadLine = configService
					.getConfigValue(ConfigConstants.InvestTransfer.APPLY_BEFORE_REPAY_DAY);
		} catch (ObjectNotFoundException onfe) {
			deadLine = "7";
		}
		// 当前日期向后7天的日期
		Date newDate = DateUtil.addDay(new Date(), Integer.valueOf(deadLine));
		if (newDate.after(invest.getRepayRoadmap().getNextRepayDate())) {
			return false;
		}
		return true;
	}

	@Override
	public void dealOverExpectTime(String investTransferId) {
		cancel(investTransferId);
	}

	@Override
	public String calculateRemainTime(String transferApplyId)
			throws NoMatchingObjectsException {
		TransferApply ta = ht.get(TransferApply.class, transferApplyId);
		if (ta == null) {
			throw new NoMatchingObjectsException(Loan.class, "transferApplyId:"
					+ transferApplyId);
		}
		if (ta.getDeadline() == null) {
			return "未开始";
		}
		Long time = (ta.getDeadline().getTime() - System.currentTimeMillis()) / 1000;

		if (time < 0) {
			return "已到期";
		}
		long days = time / 3600 / 24;
		long hours = (time / 3600) % 24;
		long minutes = (time / 60) % 60;
		if (minutes < 1) {
			minutes = 1L;
		}

		return days + "天" + hours + "时" + minutes + "分";
	}

	@Override
	public double calculateWorth(String investId, double corpus) {
		Invest invest = ht.get(Invest.class, investId);
		if (invest == null) {
			return 0D;
		}
		List<InvestRepay> irsOri = invest.getInvestRepays();

		// 根据还款日排序
		Collections.sort(irsOri, new Comparator<InvestRepay>() {
			@Override
			public int compare(InvestRepay ir1, InvestRepay ir2) {
				return ir1.getRepayDay().compareTo(ir2.getRepayDay());
			}
		});
		// 求债权的当前价格
		Double interestM = null;
		Integer daysM = null;
		Integer daysPassedM = null;
		Double unPaidCorpus = null;
		int period = 0;
		for (int i = 0; i < irsOri.size(); i++) {
			InvestRepay irTemp = irsOri.get(i);
			if (irTemp.getStatus().equals(RepayStatus.REPAYING)) {
				interestM = irTemp.getInterest();
				// 上一个还款日
				Date prevRepayDay;
				if (i == 0) {
					prevRepayDay = irTemp.getInvest().getLoan()
							.getInterestBeginTime();
				} else {
					prevRepayDay = irsOri.get(i - 1).getRepayDay();
				}
				daysM = DateUtil.getIntervalDays(prevRepayDay,
						irTemp.getRepayDay());
				if (prevRepayDay.before(new Date())) {
					daysPassedM = DateUtil
						.getIntervalDays(prevRepayDay, new Date());
				} else {
					daysPassedM = 0;
				}
				unPaidCorpus = irTemp.getInvest().getRepayRoadmap()
						.getUnPaidCorpus();
				period = irTemp.getPeriod();
				break;
			}
		}
		
		String hql= "from InvestRepay ir where ir.invest.transferApply.invest.id=? and ir.period=?";
		List<InvestRepay> irs = ht.find(hql, new Object[]{investId,period});
		for (InvestRepay irTemp : irs) {
			unPaidCorpus = ArithUtil.add(unPaidCorpus, irTemp.getInvest().getRepayRoadmap().getUnPaidCorpus());
			interestM = ArithUtil.add(interestM, irTemp.getInterest());
		}

		// (当期利息/当期天数)*已过天数+待收本金 = 债权的当前价格
		double worth = (interestM / daysM) * daysPassedM + unPaidCorpus;
		return corpus / invest.getInvestMoney() * worth;
	}

	@Override
	public double calculateFee(TransferApply ta) {
		return feeConfigBO.getFee(FeePoint.TRANSFER, FeeType.FACTORAGE, null,
				null, ta.getCorpus());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void cancel(String investTransferId) {
		// 未转让的部分，取消。
		TransferApply ta = ht.get(TransferApply.class, investTransferId);
		if(!ta.getStatus().equals(TransferStatus.TRANSFED)){
			ta.setStatus(TransferStatus.CANCEL);
			ht.update(ta);
		}
	}

}
