package com.esoft.jdp2p.risk.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.risk.SystemMoneyLogConstants;
import com.esoft.jdp2p.risk.model.SystemMoneyLog;
import com.esoft.jdp2p.risk.service.SystemMoneyLogService;

@Service(value = "systemMoneyLogService")
@SuppressWarnings("unchecked")
public class SystemMoneyLogServiceImpl implements SystemMoneyLogService {

	@Resource
	private HibernateTemplate ht;

	private SystemMoneyLog getLastestBill() {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SystemMoneyLog.class);
		criteria.setLockMode(LockMode.UPGRADE);
		criteria.addOrder(Order.desc("seqNum"));
		List<SystemMoneyLog> ibs = ht.findByCriteria(criteria, 0, 1);
		if (ibs.size() > 0) {
			return ibs.get(0);
		}
		return null;
	}

	@Override
	public double getBalance() {
		double in = getSumByType(SystemMoneyLogConstants.Type.IN);
		double out = getSumByType(SystemMoneyLogConstants.Type.OUT);
		return ArithUtil.sub(in, out);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void transferOut(double money, String reason, String detail,
			String fromAccount, String toAccount) throws InsufficientBalance {
		if (money<0) {
			throw new RuntimeException("money cannot be less than zero!");
		}
//		SystemMoneyLog ibLastest = getLastestBill();
//		SystemMoneyLog ib = new SystemMoneyLog();
//		double balance = getBalance();
//		if (ibLastest == null || balance < money) {
//			throw new InsufficientBalance("transfer out money:"+money+", balance:"+balance);
//		} else {
//			ib.setId(IdGenerator.randomUUID());
//			ib.setMoney(money);
//			ib.setTime(new Date());
//			ib.setDetail(detail);
//			ib.setReason(reason);
//			ib.setSeqNum(ibLastest.getSeqNum() + 1);
//			ib.setType(SystemMoneyLogConstants.Type.OUT);
//			ib.setFromAccount(fromAccount);
//			ib.setToAccount(toAccount);
//			ht.save(ib);
//		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void transferInto(double money, String reason, String detail,
			String fromAccount, String toAccount) {
		if (money<0) {
			throw new RuntimeException("money cannot be less than zero!");
		}
//		SystemMoneyLog ibLastest = getLastestBill();
//		SystemMoneyLog lb = new SystemMoneyLog();
//		lb.setId(IdGenerator.randomUUID());
//		lb.setMoney(money);
//		lb.setTime(new Date());
//		lb.setDetail(detail);
//		lb.setReason(reason);
//		lb.setType(SystemMoneyLogConstants.Type.IN);
//		lb.setFromAccount(fromAccount);
//		lb.setToAccount(toAccount);
//
//		if (ibLastest == null) {
//			// 第一条数据
//			lb.setSeqNum(1L);
//		} else {
//			lb.setSeqNum(ibLastest.getSeqNum() + 1);
//		}
//		ht.save(lb);
	}

	private double getSumByType(String type) {
		String hql = "select sum(sml.money) from SystemMoneyLog sml where sml.type =?";
		Double sum = (Double) ht.find(hql, type).get(0);
		if (sum == null) {
			return 0;
		}
		return sum.doubleValue();
	}

}
