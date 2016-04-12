package com.esoft.archer.user.controller;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.loan.model.WithdrawCash;

/**
 * 提现查询
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class WithdrawList extends EntityQuery<WithdrawCash> implements java.io.Serializable{
	
	private static final long serialVersionUID = 9057256750216810237L;
	
	@Logger static Log log ;
	private Date startTime ;
	private Date endTime ;
	
	public WithdrawList(){
		final String[] RESTRICTIONS = 
				{"id like #{withdrawList.example.id}",
				"user.username like #{withdrawList.example.user.username}",
				"time >= #{withdrawList.startTime}",
				"time <= #{withdrawList.endTime}",
				"source = #{withdrawList.example.source}",
				"status like #{withdrawList.example.status}"};
				
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		
	}
	
	@Override
	protected void initExample() {
		super.initExample();
		getExample().setUser(new User());
	}
	
	public Object getSumMoney(){
		final String hql = parseHql("Select sum(money),sum(fee) from WithdrawCash");
		@SuppressWarnings("unchecked")
		List<Object> resultList = getHt().execute(new HibernateCallback<List<Object>>() {

			public List<Object> doInHibernate(Session session)
			
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

}
