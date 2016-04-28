package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.RechargeBankCard;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.loan.model.Recharge;
import com.esoft.jdp2p.user.service.RechargeService;
import org.apache.commons.logging.Log;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 充值查询
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class RechargeList extends EntityQuery<Recharge> implements
		java.io.Serializable {

	private static final long serialVersionUID = 9057256750216810237L;

	@Logger
	static Log log;

	@Resource
	private RechargeService rechargeService;

	private List<RechargeBankCard> rechargeBankCards;

	private List<String> allChannelList;

	private boolean isOpenFastPayment;

	private List<RechargeBankCard> rechargeBankCardQuickPays;
	
	private Date startTime ;
	private Date endTime ;

	public RechargeList() {
		final String[] RESTRICTIONS = { "id like #{rechargeList.example.id}",
				"time >= #{rechargeList.startTime}",
				"time <= #{rechargeList.endTime}",
				"status = #{rechargeList.example.status}",
				"source = #{rechargeList.example.source}",
				"rechargeWay like #{rechargeList.example.rechargeWay}",
				"channel = #{rechargeList.example.channel}",
				"user.username like #{rechargeList.example.user.username}" };

		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
//		addOrder("time", super.DIR_DESC);
	}

	@Override
	protected void initExample() {
		super.initExample();
		getExample().setUser(new User());
		initAllChannelList();
	}

	public List<RechargeBankCard> getRechargeBankCards() {
		if (this.isOpenFastPayment) {
			this.rechargeBankCards = rechargeService.getFastPayBankCardsList();
		} else {
			this.rechargeBankCards = rechargeService.getBankCardsList();
		}
		return this.rechargeBankCards;
	}

	private void initAllChannelList(){
		this.allChannelList = rechargeService.getAllChannelName();
	}

	public Double getSumActualMoney(){
		final String hql = parseHql("Select sum(actualMoney) from Recharge");
		@SuppressWarnings("unchecked")
		List<Double> resultList = getHt().execute(new HibernateCallback<List<Double>>() {

			public List<Double> doInHibernate(Session session)
			
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				// 从第0行开始
				query.setFirstResult(0);
				query.setMaxResults(5);
				for (int i = 0; i < getParameterValues().length; i++) {
					query.setParameter(i, getParameterValues()[i]);
				}
				return query.list();
			}

		});
		
		if(resultList != null && resultList.get(0) != null){
			return resultList.get(0);
		}
		return 0D;
	}
	
	//~~~~~~~~~~~~~~~~
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public boolean getIsOpenFastPayment() {
		return isOpenFastPayment;
	}

	public void setIsOpenFastPayment(boolean isOpenFastPayment) {
		this.isOpenFastPayment = isOpenFastPayment;
	}

	public List<String> getAllChannelList() {
		return allChannelList;
	}

	public void setAllChannelList(List<String> allChannelList) {
		this.allChannelList = allChannelList;
	}
}
