package com.esoft.jdp2p.user.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import javax.annotation.Resource;

import com.esoft.jdp2p.bankcard.model.BankCard;
import org.apache.commons.lang.StringUtils;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.user.UserBillConstants.OperatorInfo;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.UserConstants.RechargeStatus;
import com.esoft.archer.user.model.RechargeBankCard;
import com.esoft.archer.user.model.RechargeBankCardImpl;
import com.esoft.archer.user.model.UserBill;
import com.esoft.archer.user.service.UserBillService;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.jdp2p.coupon.model.UserCoupon;
import com.esoft.jdp2p.loan.model.Recharge;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeePoint;
import com.esoft.jdp2p.risk.FeeConfigConstants.FeeType;
import com.esoft.jdp2p.risk.service.impl.FeeConfigBO;
import com.esoft.jdp2p.user.service.RechargeService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-27 上午10:28:55
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-27 wangzhi 1.0
 */
@Service("rechargeService")
public class RechargeServiceImpl implements RechargeService {

	@Resource
	HibernateTemplate ht;

	@Resource
	RechargeBO rechargeBO;

	@Resource
	private UserBillService userBillService;

	@Resource
	private FeeConfigBO feeConfigBO;

	@Override
	public double calculateFee(double amount) {
		return feeConfigBO.getFee(FeePoint.RECHARGE, FeeType.FACTORAGE, null,
				null, amount);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void rechargePaySuccess(String rechargeId) {
		Recharge recharge = ht.get(Recharge.class, rechargeId);
		ht.evict(recharge);
		recharge = ht.get(Recharge.class, rechargeId, LockMode.UPGRADE);
		if (recharge != null
				&& recharge.getStatus().equals(
						UserConstants.RechargeStatus.WAIT_PAY)) {
			rechargeBO.rechargeSuccess(recharge);
		}

	}

	/**
	 * 生成充值id
	 * 
	 * @return
	 */
	private String generateId() {
		String gid = DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD);
		String hql = "select recharge from Recharge recharge where recharge.id = (select max(rechargeM.id) from Recharge rechargeM where rechargeM.id like ?)";
		List<Recharge> contractList = ht.find(hql, gid + "%");
		Integer itemp = 0;
		if (contractList.size() == 1) {
			Recharge recharge = contractList.get(0);
			ht.lock(recharge, LockMode.UPGRADE);
			Session session = null;
			try {
				session = ht.getSessionFactory().openSession();
				List<Recharge> rechargeList = session.createQuery(hql).setParameter(0, gid + "%").list();
				String temp = rechargeList.get(0).getId();
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
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String createOfflineRechargeOrder(Recharge recharge) {
		// 往recharge中插入值。
		recharge.setId(generateId());
		recharge.setFee(calculateFee(recharge.getActualMoney()));
		// 用rechargeWay进行判断，判断是要跳转到银行卡还是支付平台
		// recharge.setRechargeWay("借记卡");
		recharge.setIsRechargedByAdmin(false);
		recharge.setTime(new Date());
		recharge.setStatus(UserConstants.RechargeStatus.WAIT_PAY);
		ht.save(recharge);

		return "success";
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String createRechargeOrder(Recharge recharge, HttpServletRequest request) {
		// 往recharge中插入值。
		recharge.setId(generateId());
		// 用rechargeWay进行判断，判断是要跳转到银行卡还是支付平台
		// recharge.setRechargeWay("借记卡");
		if (recharge.getFee() == null){
			recharge.setFee(0.00);
		}
		if (recharge.getCoupon() != null) {
			// 优惠券
			if (StringUtils.isEmpty(recharge.getCoupon().getId())) {
				recharge.setCoupon(null);
			} else {
				recharge.setCoupon(ht.get(UserCoupon.class, recharge
						.getCoupon().getId()));
				recharge.getCoupon().setStatus("已使用");
			}
		}

		recharge.setIsRechargedByAdmin(false);
		recharge.setTime(new Date());
		recharge.setStatus(UserConstants.RechargeStatus.WAIT_PAY);
		ht.save(recharge);
		String requestPath = "";
		if (request != null){
			requestPath = request.getRequestURI();
		}else {
			requestPath = FacesUtil.getHttpServletRequest().getContextPath();
		}
		return requestPath + "/to_recharge/" + recharge.getId();
	}

	@Override
	public String getBankNameByNo(final String bankNo) {
		List<RechargeBankCard> banks = getAllBankCards();
		for (RechargeBankCard bank : banks) {
			if (StringUtils.equals(bank.getNo(), bankNo)) {
				return bank.getBankName();
			}
		}
		return "Not found Bank";
	}

	@Override
	public boolean isRealNameBank(String bankNo) {
		List<RechargeBankCard> realNameList = this.getRealNameBankList();
		for (RechargeBankCard rechargeBankCard : realNameList){
			if (StringUtils.equals(rechargeBankCard.getNo(), bankNo)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isFastPaymentBank(String bankNo) {
		List<RechargeBankCard> fastPayList = this.getFastPayBankCardsList();
		for (RechargeBankCard rechargeBankCard : fastPayList){
			if (StringUtils.equals(rechargeBankCard.getNo(), bankNo)){
				return true;
			}
		}
		return false;
	}

	private static Properties props = new Properties();
	static {
		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("banks.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<RechargeBankCard> getFastPayBankCardsList() {
		List<RechargeBankCard> bcs = new ArrayList<RechargeBankCard>();
		bcs.add(new RechargeBankCardImpl("ICBC", "中国工商银行"));
		bcs.add(new RechargeBankCardImpl("ABC", "中国农业银行"));
		bcs.add(new RechargeBankCardImpl("CCB", "中国建设银行"));
		bcs.add(new RechargeBankCardImpl("BOC", "中国银行"));
		bcs.add(new RechargeBankCardImpl("CEB", "光大银行"));
		bcs.add(new RechargeBankCardImpl("CIB", "兴业银行"));
		bcs.add(new RechargeBankCardImpl("CMBC", "中国民生银行"));
		return bcs;
	}

	@Override
	public List<RechargeBankCard> getRealNameBankList(){
		List<RechargeBankCard> bcs = new ArrayList<RechargeBankCard>();
		bcs.add(new RechargeBankCardImpl("BOC", "中国银行"));
		bcs.add(new RechargeBankCardImpl("ABC", "中国农业银行"));
		bcs.add(new RechargeBankCardImpl("CCB", "中国建设银行"));
		bcs.add(new RechargeBankCardImpl("CEB", "光大银行"));
		bcs.add(new RechargeBankCardImpl("CIB", "兴业银行"));
		bcs.add(new RechargeBankCardImpl("CITIC", "中信银行"));
		return bcs;
	}

	@Override
	public List<RechargeBankCard> getBankCardsList() {
		List<RechargeBankCard> bcs = new ArrayList<RechargeBankCard>();
		bcs.add(new RechargeBankCardImpl("BOC", "中国银行"));
		bcs.add(new RechargeBankCardImpl("ABC", "中国农业银行"));
		bcs.add(new RechargeBankCardImpl("ICBC", "中国工商银行"));
		bcs.add(new RechargeBankCardImpl("CCB", "中国建设银行"));
		bcs.add(new RechargeBankCardImpl("PSBC", "邮储银行"));
		bcs.add(new RechargeBankCardImpl("CMBC", "中国民生银行"));
		bcs.add(new RechargeBankCardImpl("CMB", "招商银行"));
		bcs.add(new RechargeBankCardImpl("SPDB", "浦发银行"));
		bcs.add(new RechargeBankCardImpl("GDB", "广发银行"));
		bcs.add(new RechargeBankCardImpl("HXB", "华夏银行"));
		bcs.add(new RechargeBankCardImpl("CEB", "光大银行"));
		bcs.add(new RechargeBankCardImpl("BEA", "东亚银行"));
		bcs.add(new RechargeBankCardImpl("CIB", "兴业银行"));
		bcs.add(new RechargeBankCardImpl("COMM", "交通银行"));
		bcs.add(new RechargeBankCardImpl("CITIC", "中信银行"));
		bcs.add(new RechargeBankCardImpl("BJBANK", "北京银行"));
		bcs.add(new RechargeBankCardImpl("SHRCB", "上海农商银行"));
		bcs.add(new RechargeBankCardImpl("WZCB", "温州银行"));
		return bcs;
	}

	private List<RechargeBankCard> getAllBankCards() {
		String sql = "select * from bank_list";
		List<RechargeBankCard> bcs = new ArrayList<RechargeBankCard>();
		Query query = ht.getSessionFactory().getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String,Object>> list = query.list();
		for (int i=0;i<list.size();i++){
			bcs.add(new RechargeBankCardImpl(list.get(i).get("bankno").toString(),list.get(i).get("bankname").toString()));
		}
		return bcs;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void rechargeByAdmin(UserBill userBill) {
		Recharge r = new Recharge();
		r.setActualMoney(userBill.getMoney());
		r.setCoupon(null);
		r.setFee(0D);
		r.setId(generateId());
		r.setIsRechargedByAdmin(true);
		r.setRechargeWay("admin");
		r.setStatus(RechargeStatus.SUCCESS);
		r.setSuccessTime(new Date());
		r.setTime(new Date());
		r.setUser(userBill.getUser());
		ht.save(r);
		userBillService.transferIntoBalance(userBill.getUser().getId(),
				userBill.getMoney(), OperatorInfo.ADMIN_OPERATION,
				userBill.getDetail());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void rechargeByAdmin(String rechargeId) {
		rechargePaySuccess(rechargeId);
		Recharge r = ht.get(Recharge.class, rechargeId);
		r.setIsRechargedByAdmin(true);
		ht.update(r);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void rechargePayFail(String rechargeId) {
		Recharge recharge = ht.get(Recharge.class, rechargeId);
		ht.evict(recharge);
		recharge = ht.get(Recharge.class, rechargeId, LockMode.UPGRADE);
		if (recharge != null
				&& recharge.getStatus().equals(
						UserConstants.RechargeStatus.WAIT_PAY)) {
			recharge.setStatus(UserConstants.RechargeStatus.FAIL);
			ht.merge(recharge);
		}
	}
	@Transactional(rollbackFor = Exception.class)
	public String  getRechangeWay(String userId){

		String hql = "from BankCard where user.id =? and isOpenFastPayment =? and status=?";

		List<BankCard> bankCard= ht.find(hql, new Object[]{userId, true, "passed"});

		if(bankCard != null&& bankCard.size() > 0){
			return bankCard.get(0).getBankNo();
		}

		return null;


	}

	@Override
	public List<Recharge> findUserRecharge(String userId, Integer offset, Integer limit) {
		String hql = "from Recharge recharge where recharge.user.id=:userId order by recharge.time desc";
		Query query = ht.getSessionFactory().getCurrentSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setMaxResults(limit);
		query.setFirstResult(offset);
		return query.list();
	}

	@Override
	public Integer findUserRechargeCount(String userId) {
		String hql = "select count(*) from Recharge recharge where recharge.user.id=:userId";
		Query query = ht.getSessionFactory().getCurrentSession().createQuery(hql);
		query.setParameter("userId",userId);
		return ((Number)query.uniqueResult()).intValue();
	}

	@Override
	public List<String> getAllChannelName() {
		String sql = "select distinct channel from recharge where channel is not NULL";
		Session session = ht.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql);
		return query.list();
	}

}
