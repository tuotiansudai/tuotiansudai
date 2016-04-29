package com.esoft.jdp2p.user.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.config.service.ConfigService;
import com.esoft.archer.user.UserBillConstants.OperatorInfo;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.UserConstants.WithdrawStatus;
import com.esoft.archer.user.model.UserBill;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.WithdrawCash;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeePoint;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeeType;
import com.esoft.jdp2p.risk.service.SystemBillService;
import com.esoft.jdp2p.risk.service.impl.FeeConfigBO;
import com.esoft.jdp2p.user.service.WithdrawCashService;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-24 下午3:25:48
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-24 wangzhi 1.0
 */
@Service(value = "withdrawCashService")
public class WithdrawCashServiceImpl implements WithdrawCashService {

	@Logger
	private static Log log;

	@Resource
	HibernateTemplate ht;

	@Resource
	private FeeConfigBO feeConfigBO;

	@Resource
	UserBillBO userBillBO;

	@Resource
	SystemBillService sbs;

	@Resource
	ConfigService configService;

	@Override
	public double calculateFee(double amount) {
		return feeConfigBO.getFee(FeePoint.WITHDRAW, FeeType.FACTORAGE, null,
				null, amount);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void passWithdrawCashApply(WithdrawCash withdrawCash) {
		// 更新提现审核状态，到等待复核状态
		WithdrawCash wdc = ht.get(WithdrawCash.class, withdrawCash.getId());
		ht.evict(wdc);
		wdc = ht.get(WithdrawCash.class, wdc.getId(), LockMode.UPGRADE);
		if (wdc.getStatus().equals(WithdrawStatus.WAIT_VERIFY)) {
			wdc.setVerifyTime(new Date());
			wdc.setStatus(UserConstants.WithdrawStatus.RECHECK);
			wdc.setVerifyMessage(withdrawCash.getVerifyMessage());
			wdc.setVerifyUser(withdrawCash.getVerifyUser());
			ht.merge(wdc);

			if (log.isInfoEnabled())
				log.info("提现审核初审通过，提现编号：" + wdc.getId() + "，审核人："
						+ withdrawCash.getVerifyUser().getId() + "，审核时间:"
						+ wdc.getVerifyTime());
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void passWithdrawCashRecheck(WithdrawCash withdrawCash) {
		// 从冻结金额中取，系统账户也要记录
		WithdrawCash wdc = ht.get(WithdrawCash.class, withdrawCash.getId());
		ht.evict(wdc);
		wdc = ht.get(WithdrawCash.class, wdc.getId(), LockMode.UPGRADE);
		if (wdc.getStatus().equals(WithdrawStatus.RECHECK)
				|| wdc.getStatus().equals(WithdrawStatus.WAIT_VERIFY)) {
			wdc.setRecheckTime(new Date());
			wdc.setStatus(UserConstants.WithdrawStatus.SUCCESS);
			wdc.setRecheckMessage(withdrawCash.getRecheckMessage());
			wdc.setRecheckUser(withdrawCash.getRecheckUser());
			ht.merge(wdc);
			try {
				String freezeMoney = "";
				try {
					freezeMoney = configService.getConfigValue("freeze_money");
				} catch (ObjectNotFoundException e) {
					freezeMoney = "";
				}
				// 不冻结金额
				if ("0".equals(freezeMoney)) {
					userBillBO.transferOutFromBalance(wdc.getUser().getId(),
							wdc.getMoney(), OperatorInfo.WITHDRAW_SUCCESS,
							"提现申请通过，转出金额, 提现ID:" + withdrawCash.getId());
					userBillBO.transferOutFromBalance(wdc.getUser().getId(),
							wdc.getFee(), OperatorInfo.WITHDRAW_SUCCESS,
							"提现申请通过，转出手续费, 提现ID:" + withdrawCash.getId());
				} else {
					userBillBO.transferOutFromFrozen(wdc.getUser().getId(),
							wdc.getMoney(), OperatorInfo.WITHDRAW_SUCCESS,
							"提现申请通过，取出冻结金额, 提现ID:" + withdrawCash.getId());
					userBillBO.transferOutFromFrozen(wdc.getUser().getId(),
							wdc.getFee(), OperatorInfo.WITHDRAW_SUCCESS,
							"提现申请通过，取出冻结手续费, 提现ID:" + withdrawCash.getId());
				}
				sbs.transferInto(wdc.getFee(), OperatorInfo.WITHDRAW_SUCCESS,
						"提现申请通过, 扣除手续费。提现ID:" + withdrawCash.getId());
			} catch (InsufficientBalance e) {
				throw new RuntimeException(e);
			}

			if (log.isInfoEnabled())
				log.info("提现审核复核通过，提现编号：" + wdc.getId());
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void refuseWithdrawCashApply(WithdrawCash withdrawCash) {
		// 解冻申请时候冻结的金额
		WithdrawCash wdc = ht.get(WithdrawCash.class, withdrawCash.getId());
		ht.evict(wdc);
		wdc = ht.get(WithdrawCash.class, wdc.getId(), LockMode.UPGRADE);
		if (wdc.getStatus().equals(WithdrawStatus.RECHECK)
				|| wdc.getStatus().equals(WithdrawStatus.WAIT_VERIFY)) {
			if (wdc.getStatus().equals(WithdrawStatus.RECHECK)) {
				wdc.setRecheckMessage(withdrawCash.getRecheckMessage());
				wdc.setRecheckUser(withdrawCash.getRecheckUser());
				wdc.setRecheckTime(new Date());
			} else {
				wdc.setVerifyMessage(withdrawCash.getVerifyMessage());
				wdc.setVerifyUser(withdrawCash.getVerifyUser());
				wdc.setVerifyTime(new Date());
			}
			wdc.setStatus(UserConstants.WithdrawStatus.VERIFY_FAIL);
			ht.merge(wdc);
			try {
				String freezeMoney = "";
				try {
					freezeMoney = configService.getConfigValue("freeze_money");
				} catch (ObjectNotFoundException e) {
					freezeMoney = "";
				}
				// 不冻结金额
				if (!"0".equals(freezeMoney)) {
					userBillBO.unfreezeMoney(wdc.getUser().getId(),
							wdc.getMoney(), OperatorInfo.REFUSE_APPLY_WITHDRAW,
							"提现申请被拒绝，解冻提现金额, 提现ID:" + withdrawCash.getId());
					userBillBO.unfreezeMoney(wdc.getUser().getId(),
							wdc.getFee(), OperatorInfo.REFUSE_APPLY_WITHDRAW,
							"提现申请被拒绝，解冻手续费, 提现ID:" + withdrawCash.getId());
				}
			} catch (InsufficientBalance e) {
				throw new RuntimeException(e);
			}
			if (log.isInfoEnabled())
				log.info("提现审核未通过，提现编号：" + wdc.getId());
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void applyWithdrawCash(WithdrawCash withdraw)
			throws InsufficientBalance {
		// FIXME:缺验证
		validateUserBalance(withdraw);
		withdraw.setFee(calculateFee(withdraw.getMoney()));
		withdraw.setCashFine(0D);

		withdraw.setId(generateId());
		withdraw.setTime(new Date());
		userBillBO.freezeMoney(withdraw.getUser().getId(), withdraw.getMoney(),
				OperatorInfo.APPLY_WITHDRAW,
				"申请提现，冻结提现金额, 提现编号:" + withdraw.getId());
		userBillBO.freezeMoney(withdraw.getUser().getId(), withdraw.getFee(),
				OperatorInfo.APPLY_WITHDRAW,
				"申请提现，冻结提现手续费, 提现编号:" + withdraw.getId());
		// 等待审核
		withdraw.setStatus(UserConstants.WithdrawStatus.WAIT_VERIFY);
		ht.save(withdraw);
	}

	private void validateUserBalance(WithdrawCash withdraw) throws InsufficientBalance {
		double userBillBOBalance = userBillBO.getBalance(withdraw.getUser().getId());
		if (userBillBOBalance < withdraw.getMoney() + withdraw.getFee()) {
			throw new InsufficientBalance();
		}
	}

	/**
	 * 生成id
	 * 
	 * @return
	 */
	public String generateId() {
		String gid = DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD);
		String hql = "select withdraw from WithdrawCash withdraw where withdraw.id = (select max(withdrawM.id) from WithdrawCash withdrawM where withdrawM.id like ?)";
		List<WithdrawCash> contractList = ht.find(hql, gid + "%");
		Integer itemp = 0;
		if (contractList.size() == 1) {
			WithdrawCash withdrawCash = contractList.get(0);
			ht.lock(withdrawCash, LockMode.UPGRADE);
			Session session = null;
			try {
				session = ht.getSessionFactory().openSession();
				List<WithdrawCash> withdrawCaseList = session.createQuery(hql).setParameter(0, gid + "%").list();
				String temp = withdrawCaseList.get(0).getId();
				temp = temp.substring(temp.length() - 6);
				itemp = Integer.valueOf(temp);
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}
		itemp++;
		gid += String.format("%08d", itemp);
		return gid;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void withdrawByAdmin(UserBill ub) throws InsufficientBalance {
		WithdrawCash wc = new WithdrawCash();
		wc.setCashFine(0D);
		wc.setFee(0D);
		wc.setId(generateId());
		wc.setIsWithdrawByAdmin(true);
		wc.setMoney(ub.getMoney());
		wc.setStatus(WithdrawStatus.SUCCESS);
		wc.setTime(new Date());
		wc.setUser(ub.getUser());
		ht.save(wc);
		userBillBO.transferOutFromBalance(ub.getUser().getId(), ub.getMoney(),
				OperatorInfo.ADMIN_OPERATION, ub.getDetail());
	}

	@Override
	public List<WithdrawCash> queryUserWithdrawLogs(String userId, int offset, int limit) {
		String hql = "select withdraw from WithdrawCash withdraw where withdraw.user.id=:userId order by withdraw.time desc";
		Query query = ht.getSessionFactory().getCurrentSession().createQuery(hql);
		query.setParameter("userId",userId);
		query.setMaxResults(limit);
		query.setFirstResult(offset);
		return query.list();
	}

	@Override
	public int queryUserWithdrawLogsCount(String userId) {
		String hql = "select count(*) from WithdrawCash withdraw where withdraw.user.id=:userId";
		Query query = ht.getSessionFactory().getCurrentSession().createQuery(hql);
		query.setParameter("userId",userId);
		int count = ((Number)query.uniqueResult()).intValue();
		return count;
	}

}
