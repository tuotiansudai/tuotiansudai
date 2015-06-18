package com.esoft.umpay.withdraw.service.impl;

import java.text.MessageFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.hibernate.LockMode;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.UserBillConstants.OperatorInfo;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.UserConstants.WithdrawStatus;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.WithdrawCash;
import com.esoft.jdp2p.risk.service.SystemBillService;
import com.esoft.jdp2p.risk.service.impl.FeeConfigBO;
import com.esoft.jdp2p.user.service.impl.WithdrawCashServiceImpl;

public class UmPayWithdrawCashServiceImpl extends WithdrawCashServiceImpl {

	@Resource
	private FeeConfigBO feeConfigBO;
	@Resource
	UserBillBO userBillBO;
	@Resource
	SystemBillService sbs;
	@Resource
	HibernateTemplate ht;

	@Logger
	private static Log log;

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void applyWithdrawCash(WithdrawCash withdraw)
			throws InsufficientBalance {
		// FIXME:缺验证
		if (withdraw.getMoney() + withdraw.getFee() <= userBillBO.getBalance(withdraw.getUser().getId())) {
			withdraw.setFee(calculateFee(withdraw.getMoney()));
			withdraw.setCashFine(0D);

			withdraw.setId(generateId());
			withdraw.setTime(new Date());

			// userBillBO.freezeMoney(withdraw.getUser().getId(),
			// withdraw.getMoney(), OperatorInfo.APPLY_WITHDRAW,
			// "申请提现，冻结提现金额, 提现编号:" + withdraw.getId());
			// userBillBO.freezeMoney(withdraw.getUser().getId(),
			// withdraw.getFee(), OperatorInfo.APPLY_WITHDRAW,
			// "申请提现，冻结提现手续费, 提现编号:" + withdraw.getId());

			// 等待审核
			withdraw.setStatus(UserConstants.WithdrawStatus.WAIT_VERIFY);
			ht.save(withdraw);
		} else {
			throw new InsufficientBalance();
		}

	}

	/**
	 * 修改提现资金不冻结
	 */
	@SuppressWarnings("deprecation")
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
				String operationDetailTemplate = "提现申请通过，提现金额{0}元（含{1}元手续费）， 提现编号:{2}";
				userBillBO.transferOutFromFrozenForWithdraw(wdc.getUser().getId(),
						wdc.getMoney() + wdc.getFee(),
						OperatorInfo.WITHDRAW_SUCCESS,
						MessageFormat.format(operationDetailTemplate, wdc.getMoney() + wdc.getFee(), wdc.getFee(), wdc.getId()));
			} catch (InsufficientBalance e) {
				throw new RuntimeException(e);
			}
			if (log.isInfoEnabled())
				log.info("提现审核复核通过，提现编号：" + wdc.getId());
		}
		// super.passWithdrawCashRecheck(withdrawCash);
	}

	/**
	 * 提现失败不解冻金额(就没有冻结)
	 */
	@SuppressWarnings("deprecation")
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
			wdc.setStatus(WithdrawStatus.RECHECK_FAIL);
			ht.merge(wdc);

			// try {
			// userBillBO.unfreezeMoney(wdc.getUser().getId(), wdc.getMoney(),
			// OperatorInfo.REFUSE_APPLY_WITHDRAW,
			// "提现申请被拒绝，解冻提现金额, 提现ID:" + withdrawCash.getId());
			// userBillBO.unfreezeMoney(wdc.getUser().getId(), wdc.getFee(),
			// OperatorInfo.REFUSE_APPLY_WITHDRAW,
			// "提现申请被拒绝，解冻手续费, 提现ID:" + withdrawCash.getId());
			// } catch (InsufficientBalance e) {
			// throw new RuntimeException(e);
			// }

			if (log.isInfoEnabled())
				log.info("提现审核未通过，提现编号：" + wdc.getId());
		}
	}

}
