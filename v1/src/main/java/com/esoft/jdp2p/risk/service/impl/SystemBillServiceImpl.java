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
import com.esoft.jdp2p.risk.SystemBillConstants;
import com.esoft.jdp2p.risk.model.SystemBill;
import com.esoft.jdp2p.risk.service.SystemBillService;

@Service(value = "systemBillService")
@SuppressWarnings("unchecked")
public class SystemBillServiceImpl implements SystemBillService {

	@Resource
	private HibernateTemplate ht;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	private SystemBill getLastestBill() {
		DetachedCriteria criteria = DetachedCriteria.forClass(SystemBill.class);
		criteria.setLockMode(LockMode.UPGRADE);
		criteria.addOrder(Order.desc("seqNum"));
		List<SystemBill> ibs = ht.findByCriteria(criteria, 0, 1);
		if (ibs.size() > 0) {
			SystemBill sb = ibs.get(0);
			if (sb.getBalance() == null) {
				double in = getSumByType(SystemBillConstants.Type.IN);
				double out = getSumByType(SystemBillConstants.Type.OUT);
				sb.setBalance(ArithUtil.sub(in, out));
				ht.update(sb);
			}
			return sb;
		}
		return null;
	}

	@Override
	public double getBalance() {
		// double in = getSumByType(SystemBillConstants.Type.IN);
		// double out = getSumByType(SystemBillConstants.Type.OUT);
		// return ArithUtil.sub(in, out);
		SystemBill sb = getLastestBill();
		return sb == null ? 0D : sb.getBalance();
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void transferOut(double money, String reason, String detail)
			throws InsufficientBalance {
		if (money < 0) {
			throw new RuntimeException("money cannot be less than zero!");
		}
		SystemBill ibLastest = getLastestBill();
		double balance = getBalance();
		SystemBill ib = new SystemBill();
		if (balance < money) {
			throw new InsufficientBalance("transfer out money:" + money
					+ ", balance:" + balance);
		} else {
			ib.setId(IdGenerator.randomUUID());
			ib.setMoney(money);
			ib.setTime(new Date());
			ib.setDetail(detail);
			ib.setReason(reason);
			ib.setType(SystemBillConstants.Type.OUT);
			if (ibLastest == null) {
				// 第一条数据
				ib.setSeqNum(1L);
				// 余额=0
				ib.setBalance(0D);
			} else {
				// 余额=上一条余额-取出的钱
				ib.setBalance(ArithUtil.sub(ibLastest.getBalance(), money));
				ib.setSeqNum(ibLastest.getSeqNum() + 1);
			}
			ht.save(ib);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void transferInto(double money, String reason, String detail) {
		if (money < 0) {
			throw new RuntimeException("money cannot be less than zero!");
		}
		SystemBill ibLastest = getLastestBill();
		SystemBill lb = new SystemBill();
		lb.setId(IdGenerator.randomUUID());
		lb.setMoney(money);
		lb.setTime(new Date());
		lb.setDetail(detail);
		lb.setType(SystemBillConstants.Type.IN);
		lb.setReason(reason);

		if (ibLastest == null) {
			// 第一条数据
			lb.setSeqNum(1L);
			// 余额=money
			lb.setBalance(money);
		} else {
			lb.setSeqNum(ibLastest.getSeqNum() + 1);
			// 余额=上一条余额+money
			lb.setBalance(ArithUtil.add(ibLastest.getBalance(), money));
		}
		ht.save(lb);
	}

	private double getSumByType(String type) {
		String hql = "select sum(sb.money) from SystemBill sb where sb.type =?";
		Double sum = (Double) ht.find(hql, type).get(0);
		if (sum == null) {
			return 0;
		}
		return sum.doubleValue();
	}

}

SELECT u.id AS id,u.referrer AS referrer,u.register_time AS register_time,ta.create_time AS create_time,re.time AS recharge_time FROM user u JOIN trusteeship_account ta ON u.id = ta.user_id AND (u.register_time BETWEEN '2015-07-14 23:00:00' AND '2015-07-14 23:59:59')AND (ta.create_time BETWEEN '2015-07-14 23:00:00' AND '2015-07-14 23:59:59')JOIN recharge re ON u.id = re.user_id AND re.status = 'success'AND (re.time BETWEEN '2015-07-14 23:00:00' AND '2015-07-14 23:59:59')WHERE EXISTS( SELECT 1 FROM bank_card bc WHERE u.id = bc.user_id AND bc.status = 'passed'AND (bc.time BETWEEN '2015-07-14 23:00:00' AND '2015-07-14 23:59:59'))AND EXISTS( SELECT 1 FROM bank_card rbc WHERE u.referrer = rbc.user_id AND rbc.status = 'passed'AND rbc.time < '2015-07-14 23:59:59');