package com.esoft.jdp2p.invest.service.impl;

import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.config.service.ConfigService;
import com.esoft.archer.user.UserBillConstants.OperatorInfo;
import com.esoft.archer.user.service.UserBillService;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.util.ArithUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.jdp2p.coupon.CouponConstants;
import com.esoft.jdp2p.coupon.exception.ExceedDeadlineException;
import com.esoft.jdp2p.coupon.exception.UnreachedMoneyLimitException;
import com.esoft.jdp2p.coupon.service.UserCouponService;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.exception.ExceedMaxAcceptableRate;
import com.esoft.jdp2p.invest.exception.ExceedMoneyNeedRaised;
import com.esoft.jdp2p.invest.exception.IllegalLoanStatusException;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.invest.service.InvestService;
import com.esoft.jdp2p.loan.LoanConstants.LoanStatus;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanCalculator;
import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.jdp2p.repay.service.RepayService;
import org.apache.commons.lang.StringUtils;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 *
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-22 上午10:48:02
 *
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-22 wangzhi 1.0
 */
@Service("investService")
public class InvestServiceImpl implements InvestService {

	@Resource
	HibernateTemplate ht;

	@Resource
	UserBillService ubs;

	@Resource
	ConfigService cs;

	@Resource
	LoanService loanService;

	@Resource
	LoanCalculator loanCalculator;

	@Resource
	RepayService repayService;

	@Resource
	UserBillBO userBillBO;

	@Resource
	UserCouponService userCouponService;

	// @Resource
	// AutoInvestService autoInvestService;

	@Override
	public String generateId(String loanId) {
		String gid = DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD);

		String hql = "select im from Invest im where im.id = (select max(imM.id) from Invest imM where imM.id like ?)";
		List<Invest> contractList = ht.find(hql, gid + "%");
		Integer itemp = 0;
		if (contractList.size() == 1) {
			Invest im = contractList.get(0);
			ht.lock(im, LockMode.UPGRADE);
			Session session = null;
			try {
				session = ht.getSessionFactory().openSession();
				List<Invest> investList = session.createQuery(hql).setParameter(0, gid + "%").list();
				String temp = investList.get(0).getId();
				temp = temp.substring(temp.length() - 6);
				itemp = Integer.valueOf(temp);
			} finally {
				if (session != null) {
					session.close();
				}
			}

		}
		itemp++;
		gid += String.format("%06d", itemp);
		return gid;
	}

	/**
	 * 投资
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void create(Invest invest) throws InsufficientBalance,
			ExceedMoneyNeedRaised, ExceedMaxAcceptableRate,
			ExceedDeadlineException, UnreachedMoneyLimitException,
			IllegalLoanStatusException {

		String loanId = invest.getLoan().getId();
		invest.setInvestMoney(invest.getMoney());
		// 防止并发出现
		Loan loan = ht.get(Loan.class, loanId);
		ht.evict(loan);
		loan = ht.get(Loan.class, loanId, LockMode.UPGRADE);
		// 如果借款不是在筹款中，抛出异常
		if (!loan.getStatus().equals(LoanStatus.RAISING)) {
			throw new IllegalLoanStatusException(loan.getStatus());
		}

		// 判断项目尚未认购的金额，如果用户想认购的金额大于此金额，则。。。
		double remainMoney;
		try {
			remainMoney = loanCalculator.calculateMoneyNeedRaised(loan.getId());
		} catch (NoMatchingObjectsException e) {
			throw new RuntimeException(e);
		}
		// 输入的投资金额 大于 可投资的金额（尚未认购的金额） 抛出异常
		if (invest.getMoney() > remainMoney) {
			throw new ExceedMoneyNeedRaised();
		}

		// 判断是否有代金券；判断能否用代金券
		if (invest.getUserCoupon() != null) {
			// 代金券不是未使用状态，抛出异常
			if (!invest.getUserCoupon().getStatus()
					.equals(CouponConstants.UserCouponStatus.UNUSED)) {
				throw new ExceedDeadlineException();
			}
			// 判断代金券是否达到使用条件
			if (invest.getMoney() < invest.getUserCoupon().getCoupon()
					.getLowerLimitMoney()) {
				throw new UnreachedMoneyLimitException();
			}
			// 用户填写认购的钱数以后，判断余额，如果余额不够，不能提交，弹出新页面让用户充值
			// investMoney>代金券金额+余额
			if (invest.getMoney() > invest.getUserCoupon().getCoupon()
					.getMoney()
					+ ubs.getBalance(invest.getUser().getId())) {
				throw new InsufficientBalance();
			}
			// investMoney > 可用余额，抛异常
		} else if (invest.getMoney() > ubs.getBalance(invest.getUser().getId())) {
			throw new InsufficientBalance();
		}

		invest.setStatus(InvestConstants.InvestStatus.BID_SUCCESS);
		invest.setRate(loan.getRate());
		invest.setTime(new Date());

		// 投资成功以后，判断项目是否有尚未认购的金额，如果没有，则更改项目状态。
		invest.setId(generateId(invest.getLoan().getId()));
		if (invest.getTransferApply() == null
				|| StringUtils.isEmpty(invest.getTransferApply().getId())) {
			invest.setTransferApply(null);
		}
		ht.save(invest);
		try {
			// 处理借款募集完成
			loanService.dealRaiseComplete(loan.getId());
		} catch (NoMatchingObjectsException e) {
			throw new RuntimeException(e);
		}
		// 将金额冻结，借款项目执行时，把钱打给借款者
		if (invest.getUserCoupon() != null) {
			// 代金券已使用，冻结：investMoney-代金券金额
			userCouponService.userUserCoupon(invest.getUserCoupon().getId());
			// 实际 冻结金额=investMoney-代金券
			double realMoney = ArithUtil.sub(invest.getMoney(), invest
					.getUserCoupon().getCoupon().getMoney());
			if (realMoney < 0) {
				realMoney = 0;
			}
			ubs.freezeMoney(
					invest.getUser().getId(),
					realMoney,
					OperatorInfo.INVEST_SUCCESS,
					"投资成功：冻结金额。借款ID:" + loan.getId() + "  投资id:"
							+ invest.getId());
		} else {
			ubs.freezeMoney(
					invest.getUser().getId(),
					invest.getMoney(),
					OperatorInfo.INVEST_SUCCESS,
					"投资成功：冻结金额。借款ID:" + loan.getId() + "  投资id:"
							+ invest.getId());
		}

	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public long getUserInvestCount(String userId) {
		String hql = "select count(invest) from Invest invest where invest.status not in (?,?) and invest.user.id=?";
		List<Object> oos = ht.find(hql, new String[] {
				InvestConstants.InvestStatus.WAIT_AFFIRM,
				InvestConstants.InvestStatus.CANCEL, userId });
		if (null == oos.get(0)) {
			return 0;
		}
		return (Long) oos.get(0);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public long getUserInvestXSCount(String userId) {
		String hql = "select count(invest) from Invest invest join invest.loan loan where loan.loanActivityType='xs' and invest.status not in (?, ?, ?, ?) and invest.user.id=?";
		List<Object> oos = ht.find(hql, new String[] {
				InvestConstants.InvestStatus.UNFINISHED,
				InvestConstants.InvestStatus.TEST,
				InvestConstants.InvestStatus.WAIT_AFFIRM,
				InvestConstants.InvestStatus.CANCEL,
				userId });
		if (null == oos.get(0)) {
			return 0;
		}
		return (Long) oos.get(0);
	}

	@Override
	public List<String> getAllChannelName() {
		String sql = "select distinct channel from invest where channel is not NULL";
		Session session = ht.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql);
		return query.list();
	}

}
