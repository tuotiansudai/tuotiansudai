package com.esoft.jdp2p.invest.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.hibernate.ObjectNotFoundException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.config.ConfigConstants;
import com.esoft.archer.config.service.ConfigService;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.ArithUtil;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.exception.ExceedMoneyNeedRaised;
import com.esoft.jdp2p.invest.model.AutoInvest;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.invest.service.AutoInvestService;
import com.esoft.jdp2p.invest.service.InvestService;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanCalculator;
import com.esoft.jdp2p.loan.service.LoanService;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 自动投标service
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-3-8 上午10:38:48
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-3-8 wangzhi 1.0
 */
@Service("autoInvestService")
public class AutoInvestServiceImpl implements AutoInvestService {

	@Logger
	Log log;

	@Resource
	private HibernateTemplate ht;

	@Resource
	private InvestService investService;

	@Resource
	private ConfigService configService;

	@Resource
	private LoanService loanService;

	@Resource
	private UserBillBO userBillBO;

	@Resource
	private LoanCalculator loanCalculator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.esoft.jdp2p.invest.service.impl.AutoInvestService#settingAutoInvest
	 * (com.esoft.jdp2p.invest.model.AutoInvest)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void settingAutoInvest(AutoInvest ai) {
		AutoInvest ai2 = ht.get(AutoInvest.class, ai.getUserId());
		if (ai2 == null || !ai2.getStatus().equals(ai.getStatus())) {
			// 之前没有设置，或者开启或者关闭了设置。直接扔到队尾
			ai.setLastAutoInvestTime(new Date());
		}
		ht.merge(ai);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.esoft.jdp2p.invest.service.impl.AutoInvestService#autoInvest(java
	 * .lang.String)
	 */
	@Override
	@Async
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void autoInvest(String loanId) {
		boolean isOpenAutoInvest = false;
		try {
			String isOpenStr = configService
					.getConfigValue(ConfigConstants.AutoInvest.IS_OPEN);
			if (isOpenStr.equals("1")) {
				isOpenAutoInvest = true;
			}
		} catch (ObjectNotFoundException onfe) {
			// 如果找不到该config
			isOpenAutoInvest = false;
		}
		if (log.isDebugEnabled()) {
			log.debug("autoInvest switch: " + isOpenAutoInvest);
		}
		if (!isOpenAutoInvest) {
			return;
		}
		Loan loan = ht.get(Loan.class, loanId);

		// XXX:判定当前标的是否为新手标或者定向标,如果是则返回
		if ("xs".equals(loan.getLoanActivityType())) {
			return;
		} else if ("dx".equals(loan.getLoanActivityType())) {
			return;
		} else {
			ht.evict(loan);
			loan = ht.get(Loan.class, loanId, LockMode.UPGRADE);
		}

		// 自动投标结束百分比
		Double endPercent = Double.parseDouble(configService
				.getConfigValue(ConfigConstants.AutoInvest.END_PERCENT));
		double rcPercent;
		try {
			rcPercent = loanCalculator
					.calculateRaiseCompletedRate(loan.getId());
		} catch (NoMatchingObjectsException e1) {
			throw new RuntimeException(e1);
		}
		// 执行自动投标

		// 距离到达募集上限的金额( (结束百分比-已经募集的百分比)/100*借款总金额 )
		Double remainRaisingMoney = ArithUtil.mul(
				ArithUtil.div(ArithUtil.sub(endPercent, rcPercent), 100),
				loan.getLoanMoney());
		if (log.isDebugEnabled()) {
			log.debug("autoInvest endPercent: " + endPercent
					+ " remainRaisingMoney: " + remainRaisingMoney);
		}
		// 如果借款满足自动投标的设定条件
		// 遍历所有自动投标用户
		List<AutoInvest> ais = ht.find("from AutoInvest ai where ai.status = '"
				+ InvestConstants.AutoInvest.Status.ON
				+ "' order by ai.lastAutoInvestTime asc, ai.seqNum asc");
		if (log.isDebugEnabled()) {
			log.debug("autoInvest queneSize: " + ais.size());
		}
		for (int i = 0; i < ais.size(); i++) {
			AutoInvest ai = ais.get(i);
			// 取得分最高的，进行投标
			// 判断此借款是否满足此人设置的投标条件（风险等级之类）
			if (log.isDebugEnabled()) {
				log.debug("autoInvest userId: " + ai.getUserId());
				log.debug("autoInvest investMoney: " + ai.getInvestMoney()
						+ "$minInvestMoney: " + loan.getMinInvestMoney()
						+ "$remainRaisingMoney: " + remainRaisingMoney);
				log.debug("autoInvest balance: "
						+ userBillBO.getBalance(ai.getUserId())
						+ "$remainMoney: " + ai.getRemainMoney());
				log.debug("autoInvest maxDeadline: " + ai.getMaxDeadline()
						+ "$minDeadline: " + ai.getMinDeadline()
						+ "$loanDeadline: " + loan.getDeadline());
				log.debug("autoInvest maxRate: " + ai.getMaxRate()
						+ "$minRate: " + ai.getMinRate() + "$loanRate: "
						+ loan.getRate());
			}
			if (ai.getInvestMoney() >= loan.getMinInvestMoney()
					// 剩余募集金额小于自动投标的钱数
					// && ai.getInvestMoney() <= remainRaisingMoney
					// 自动投标还未募集满
					&& remainRaisingMoney > 0
					// 自动投标金额小于最大投资额
					// && ai.getInvestMoney() <= loan.getMaxInvestMoney()
					// 用户余额-保留余额>=用户每次投标金额
					&& userBillBO.getBalance(ai.getUserId())
							- ai.getRemainMoney() >= ai.getInvestMoney()
					// FIXME:需要判断loan的真正时长
					&& ai.getMaxDeadline() >= loan.getDeadline()
					&& ai.getMinDeadline() <= loan.getDeadline()
					&& ai.getMaxRate() >= loan.getRate()
					&& ai.getMinRate() <= loan.getRate()

					// 自动投标不能投自己的项目
					&& ai.getUser().getId() != loan.getUser().getId()) {
				// 投标
				Invest invest = new Invest();
				invest.setIsAutoInvest(true);
				invest.setLoan(loan);

				// 投标规则
				// 1、借款进入招标中{n}分钟后，系统开启自动投标。
				// 2、投标进度达到 {s}%时停止自动投标，
				// 若剩余自动投标金额小于用户设定的每次投标金额，也会进行投标，投资金额向下取该标剩余自动投标金额。
				// 3、单笔投标金额若超过该标单笔最大投资额，则向下取该标最大投资额。
				// 4、投标排序规则如下：
				// a）投标序列按照开启自动投标的时间先后进行排序。
				// b）每个用户每个标仅自动投标一次，投标后，排到队尾。
				// c）轮到用户投标时没有符合用户条件的标，该用户会继续保持在最前，只到投入。

				// 计算实际投资金额
				// 自动投资金额大于剩余可投金额
				if (ai.getInvestMoney() > remainRaisingMoney) {
					continue;
				}
				// 自动投标金额不满足最小、最大投资金额限制
				if (ai.getInvestMoney() > loan.getMaxInvestMoney()
						|| ai.getInvestMoney() < loan.getMinInvestMoney()) {
					continue;
				}
				// 自动投资金额不满足 倍数 限制
				if ((ai.getInvestMoney() - loan.getMinInvestMoney())
						% loan.getCardinalNumber() != 0) {
					continue;
				}
				Double investMoney = ai.getInvestMoney();
				// 取最大单笔最大投资金额和可投金额的最小额
				investMoney = loan.getMaxInvestMoney() <= investMoney ? loan
						.getMaxInvestMoney() : investMoney;

				invest.setInvestMoney(investMoney);
				invest.setMoney(investMoney);
				invest.setUser(ai.getUser());
				try {
					investService.create(invest);
					remainRaisingMoney = ArithUtil.sub(remainRaisingMoney,
							investMoney);
					if (log.isDebugEnabled()) {
						log.debug("autoInvest investId: " + invest.getId());
					}
				} catch (ExceedMoneyNeedRaised e) {
					break;
				} catch (Exception e) {
					if (log.isErrorEnabled()) {
						log.error(e.getLocalizedMessage(), e);
					}
				}
				// 扔到队尾
				ai.setLastAutoInvestTime(new Date());
				ai.setSeqNum(i + 1);
				ht.update(ai);
				/** caijinmin 如果投满项目，更改项目状态为 等待复核 201502021235 begin */
				try {
					// 处理借款募集完成
					loanService.dealRaiseComplete(loan.getId());
				} catch (NoMatchingObjectsException e) {
					throw new RuntimeException(e);
				}
				/** caijinmin 如果投满项目，更改项目状态为 等待复核 201502021235 end */
			}
		}
	}
}
