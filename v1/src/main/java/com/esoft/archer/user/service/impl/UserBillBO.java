package com.esoft.archer.user.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.SpringBeanUtil;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.UserBillConstants;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserBill;
import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;

@Service(value = "userBillBO")
public class UserBillBO {

	@Resource
	private HibernateTemplate ht;

	public UserBill getLastestBill(String userId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UserBill.class);
		criteria.addOrder(Order.desc("seqNum"));
		criteria.setLockMode(LockMode.UPGRADE);
		criteria.add(Restrictions.eq("user.id", userId));
		List<UserBill> ibs = ht.findByCriteria(criteria, 0, 1);
		if (ibs.size() > 0) {
			UserBill ub = ibs.get(0);
			if (ub.getBalance() == null || ub.getFrozenMoney() == null) {
				if (ub.getBalance() == null) {
					double freeze = getSumByType(userId,
							UserBillConstants.Type.FREEZE);
					double transferIntoBalance = getSumByType(userId,
							UserBillConstants.Type.TI_BALANCE);
					double transferOutFromBalance = getSumByType(userId,
							UserBillConstants.Type.TO_BALANCE);
					double unfreeze = getSumByType(userId,
							UserBillConstants.Type.UNFREEZE);
					ub.setBalance(ArithUtil.add(ArithUtil.sub(ArithUtil.sub(
							transferIntoBalance, transferOutFromBalance),
							freeze), unfreeze));
				}
				if (ub.getFrozenMoney() == null) {
					double freeze = getSumByType(userId,
							UserBillConstants.Type.FREEZE);
					double transferOutFromFrozen = getSumByType(userId,
							UserBillConstants.Type.TO_FROZEN);
					double unfreeze = getSumByType(userId,
							UserBillConstants.Type.UNFREEZE);
					ub.setFrozenMoney(ArithUtil.sub(
							ArithUtil.sub(freeze, unfreeze),
							transferOutFromFrozen));
				}
				ht.update(ub);
			}
			return ub;
		}
		return null;
	}

	/**
	 * 冻结金额
	 * 
	 * @param userId
	 *            用户的id
	 * @param money
	 *            金额
	 * @param operatorInfo
	 *            资金转移的操作类型
	 * @param operatorDetail
	 *            资金转移的详述
	 * @throws InsufficientBalance
	 *             抛出异常 余额不足以支付保证金
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void freezeMoney(String userId, double money, String operatorInfo,
			String operatorDetail) throws InsufficientBalance {
		if (money < 0) {
			throw new RuntimeException("money cannot be less than zero!");
		}
		UserBill ibLastest = getLastestBill(userId);
		UserBill ib = new UserBill();
		double balance = getBalance(userId);
		if (balance < money) {
			throw new InsufficientBalance("freeze money:" + money
					+ ", balance:" + balance);
		} else {
			ib.setId(IdGenerator.randomUUID());
			ib.setMoney(money);
			ib.setTime(new Date());
			ib.setDetail(operatorDetail);
			ib.setType(UserBillConstants.Type.FREEZE);
			ib.setTypeInfo(operatorInfo);
			ib.setUser(new User(userId));
			ib.setOperator(String.valueOf(FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}")));
			if (ibLastest == null) {
				ib.setSeqNum(1L);
				// 余额=0
				ib.setBalance(0D);
				// 最新冻结金额=0
				ib.setFrozenMoney(0D);
			} else {
				ib.setSeqNum(ibLastest.getSeqNum() + 1);
				// 余额=上一条余额-将要被冻结的金额
				ib.setBalance(ArithUtil.sub(ibLastest.getBalance(), money));
				// 最新冻结金额=上一条冻结+将要冻结
				ib.setFrozenMoney(ArithUtil.add(ibLastest.getFrozenMoney(),
						money));
			}
			ht.save(ib);
		}
	}

	/**
	 * 获取余额
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public double getBalance(String userId) {
		UserBill ub = getLastestBill(userId);
		return ub == null ? 0D : ub.getBalance();
		// double freeze = getSumByType(userId, UserBillConstants.Type.FREEZE);
		// double transferIntoBalance = getSumByType(userId,
		// UserBillConstants.Type.TI_BALANCE);
		// double transferOutFromBalance = getSumByType(userId,
		// UserBillConstants.Type.TO_BALANCE);
		// double transferOutFromFrozen =
		// getSumByType(UserBillConstants.Type.TO_FROZEN);
		// double unfreeze = getSumByType(userId,
		// UserBillConstants.Type.UNFREEZE);
		// return ArithUtil.add(ArithUtil.sub(
		// ArithUtil.sub(transferIntoBalance, transferOutFromBalance),
		// freeze), unfreeze);
	}

	/**
	 * 获取冻结金额
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public double getFrozenMoney(String userId) {
		UserBill ub = getLastestBill(userId);
		return ub == null ? 0D : ub.getFrozenMoney();
		// double freeze = getSumByType(userId, UserBillConstants.Type.FREEZE);
		// double transferOutFromFrozen = getSumByType(userId,
		// UserBillConstants.Type.TO_FROZEN);
		// double unfreeze = getSumByType(userId,
		// UserBillConstants.Type.UNFREEZE);
		// return ArithUtil.sub(ArithUtil.sub(freeze, unfreeze),
		// transferOutFromFrozen);
	}

	/**
	 * 从冻结金额中转出
	 * 
	 * @param userId
	 * @param money
	 * @param operatorInfo
	 * @param operatorDetail
	 * @throws InsufficientBalance
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void transferOutFromFrozen(String userId, double money,
			String operatorInfo, String operatorDetail)
			throws InsufficientBalance {
		if (money < 0) {
			throw new RuntimeException("money cannot be less than zero!");
		}
		UserBill ibLastest = getLastestBill(userId);
		UserBill ib = new UserBill();
		double frozen = getFrozenMoney(userId);
		if (frozen < money) {
			throw new InsufficientBalance("transfer from frozen money:" + money
					+ ", frozen money:" + frozen);
		}
		ib.setId(IdGenerator.randomUUID());
		ib.setMoney(money);
		ib.setTime(new Date());
		ib.setDetail(operatorDetail);
		ib.setType(UserBillConstants.Type.TO_FROZEN);
		ib.setTypeInfo(operatorInfo);
		ib.setUser(new User(userId));
		if (ibLastest == null) {
			ib.setSeqNum(1L);
			// 余额=0
			ib.setBalance(0D);
			// 最新冻结金额=0
			ib.setFrozenMoney(0D);
		} else {
			ib.setSeqNum(ibLastest.getSeqNum() + 1);
			// 余额=上一条余额
			ib.setBalance(ibLastest.getBalance());
			// 最新冻结金额=上一条冻结-取出的
			ib.setFrozenMoney(ArithUtil.sub(ibLastest.getFrozenMoney(), money));
		}
		ht.save(ib);
	}

	/**
	 * 解冻金额
	 * 
	 * @param userId
	 * @param money
	 * @param operatorInfo
	 * @param operatorDetail
	 * @throws InsufficientBalance
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void unfreezeMoney(String userId, double money, String operatorInfo,
			String operatorDetail) throws InsufficientBalance {
		if (money < 0) {
			throw new RuntimeException("money cannot be less than zero!");
		}
		UserBill ibLastest = getLastestBill(userId);
		UserBill ib = new UserBill();
		double frozen = getFrozenMoney(userId);
		if (frozen < money) {
			throw new InsufficientBalance("unfreeze money:" + money
					+ ", frozen money:" + frozen);
		} else {
			ib.setId(IdGenerator.randomUUID());
			ib.setMoney(money);
			ib.setTime(new Date());
			ib.setDetail(operatorDetail);
			ib.setType(UserBillConstants.Type.UNFREEZE);
			ib.setTypeInfo(operatorInfo);
			ib.setUser(new User(userId));
			ib.setOperator(String.valueOf(FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}")));

			if (ibLastest == null) {
				ib.setSeqNum(1L);
				// 余额=0
				ib.setBalance(0D);
				// 最新冻结金额=0
				ib.setFrozenMoney(0D);
			} else {
				ib.setSeqNum(ibLastest.getSeqNum() + 1);
				// 余额=上一条余额+解冻的金额
				ib.setBalance(ArithUtil.add(ibLastest.getBalance(), money));
				// 最新冻结金额=上一条冻结-解冻的金额
				ib.setFrozenMoney(ArithUtil.sub(ibLastest.getFrozenMoney(),
						money));
			}
			ht.save(ib);
		}
	}

	/**
	 * 从余额转出
	 * 
	 * @param userId
	 * @param money
	 * @param operatorInfo
	 * @param operatorDetail
	 * @throws InsufficientBalance
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void transferOutFromBalance(String userId, double money,
			String operatorInfo, String operatorDetail)
			throws InsufficientBalance {
		if (money < 0) {
			throw new RuntimeException("money cannot be less than zero!");
		}
		UserBill ibLastest = getLastestBill(userId);
		UserBill ib = new UserBill();
		double balance = getBalance(userId);
		if (balance < money) {
			throw new InsufficientBalance("transfer out money:" + money
					+ ",balance:" + balance);
		} else {
			ib.setId(IdGenerator.randomUUID());
			ib.setMoney(money);
			ib.setTime(new Date());
			ib.setDetail(operatorDetail);
			ib.setType(UserBillConstants.Type.TO_BALANCE);
			ib.setTypeInfo(operatorInfo);
			ib.setUser(new User(userId));
			ib.setOperator(String.valueOf(FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}")));

			if (ibLastest == null) {
				ib.setSeqNum(1L);
				// 余额=0
				ib.setBalance(0D);
				// 最新冻结金额=0
				ib.setFrozenMoney(0D);
			} else {
				ib.setSeqNum(ibLastest.getSeqNum() + 1);
				// 余额=上一条余额-money
				ib.setBalance(ArithUtil.sub(ibLastest.getBalance(), money));
				// 最新冻结金额=上一条冻结
				ib.setFrozenMoney(ibLastest.getFrozenMoney());
			}
			ht.save(ib);
		}
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void transferOutFromFrozenForWithdraw(String userId,
												 double money,
												 String operatorInfo,
												 String operatorDetail) throws InsufficientBalance {
		if (money < 0) {
			throw new RuntimeException("money cannot be less than zero!");
		}
		UserBill ibLatest = getLastestBill(userId);
		UserBill ib = new UserBill();
		double frozenMoney = ibLatest == null ? 0D : ibLatest.getFrozenMoney();
		if (frozenMoney < money) {
			throw new InsufficientBalance("transfer out money:" + money + ", frozenMoney:" + frozenMoney);
		} else {
			ib.setId(IdGenerator.randomUUID());
			ib.setMoney(money);
			ib.setTime(new Date());
			ib.setDetail(operatorDetail);
			ib.setType(UserBillConstants.Type.TO_BALANCE);
			ib.setTypeInfo(operatorInfo);
			ib.setUser(new User(userId));
			if (ibLatest == null) {
				ib.setSeqNum(1L);
				// 余额=0
				ib.setBalance(0D);
				// 最新冻结金额=0
				ib.setFrozenMoney(0D);
			} else {
				ib.setSeqNum(ibLatest.getSeqNum() + 1);
				ib.setBalance(ibLatest.getBalance());
				ib.setFrozenMoney(ArithUtil.sub(ibLatest.getFrozenMoney(), money));
			}
			ht.save(ib);
		}
	}

	/**
	 * 转入到余额
	 * 
	 * @param userId
	 * @param money
	 * @param operatorInfo
	 * @param operatorDetail
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void transferIntoBalance(String userId, double money,
			String operatorInfo, String operatorDetail) {
		if (money < 0) {
			throw new RuntimeException("money cannot be less than zero!");
		}
		UserBill ibLastest = getLastestBill(userId);
		UserBill lb = new UserBill();
		lb.setId(IdGenerator.randomUUID());
		lb.setMoney(money);
		lb.setTime(new Date());
		lb.setDetail(operatorDetail);
		lb.setType(UserBillConstants.Type.TI_BALANCE);
		lb.setTypeInfo(operatorInfo);
		lb.setUser(new User(userId));
		if(FacesUtil.getCurrentInstance() != null){
			lb.setOperator(String.valueOf(FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}")));
		}else{
			lb.setOperator(null);
		}

		if (ibLastest == null) {
			lb.setSeqNum(1L);
			// 余额=money
			lb.setBalance(money);
			// 最新冻结金额=上一条冻结-取出的
			lb.setFrozenMoney(0D);
		} else {
			lb.setSeqNum(ibLastest.getSeqNum() + 1);
			// 余额=上一条余额+money
			lb.setBalance(ArithUtil.add(ibLastest.getBalance(), money));
			// 最新冻结金额=上一条冻结
			lb.setFrozenMoney(ibLastest.getFrozenMoney());
		}
		ht.save(lb);
	}

	private double getSumByType(String userId, String type) {
		String hql = "select sum(ub.money) from UserBill ub where ub.user.id =? and ub.type=?";
		Double sum = (Double) ht.find(hql, new String[] { userId, type })
				.get(0);
		if (sum == null) {
			return 0;
		}
		return ArithUtil.round(sum.doubleValue(), 2);
	}

}
