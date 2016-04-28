/**
 * Filename:AutoInvestHome.java
 * Description:TODO
 * Copyright: Copyright(c)2013
 * Company:jdp2p
 * @author:gongph
 * version:1.0
 * Create at: 2014-3-11 上午11:38:06
 */
package com.esoft.jdp2p.invest.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.model.AutoInvest;
import com.esoft.jdp2p.invest.service.AutoInvestService;

/**
 * @author Administrator
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class AutoInvestHome extends EntityHome<AutoInvest> {

	@Resource
	private AutoInvestService autoInvestService;
	
	@Resource
	private LoginUserInfo loginUserInfo;

	/* (non-Javadoc)
	 * @see com.esoft.archer.common.controller.EntityHome#initInstance()
	 */
	@Override
	protected void initInstance() {
		AutoInvest ai = getBaseService().get(AutoInvest.class, loginUserInfo.getLoginUserId());
		if (ai != null) {
			this.setInstance(ai);
		} else {
			this.setInstance(createInstance());
		}
	}
	
	@Override
	protected AutoInvest createInstance() {
		AutoInvest ai = new AutoInvest();
		ai.setUser(new User());
//		ai.setMinRiskRank(new RiskRank());
//		ai.setMaxRiskRank(new RiskRank());
		return ai;
	}

	/**
	 * 保存自动投标
	 */
	public String saveAutoInvest() {

		// 先从数据库中查找当前用户最早自动投标的最大序号
		String uname = loginUserInfo.getLoginUserId();
		// current user name
		getInstance().setUser(new User(uname));
		// auto invest time
		getInstance().setStatus(InvestConstants.AutoInvest.Status.ON);

		// autoInvest start
		autoInvestService.settingAutoInvest(getInstance());
		// return savesuccess info to client
		FacesUtil.addInfoMessage("开启自动投标成功！");
		
		this.clearInstance();
		return "pretty:autoInvest";
	}

	/**
	 * 取消自动投标
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String cancleAutoInvest() {
		
		String uname = loginUserInfo.getLoginUserId();
		
		List<AutoInvest> alist = getBaseService().find(
				"from AutoInvest ai where ai.user.id =?",
				uname);
		
		AutoInvest ai = alist.get(0);
		
		//set autoinvest status
		ai.setStatus(InvestConstants.AutoInvest.Status.OFF);
		//close autoinvest
		autoInvestService.settingAutoInvest(ai);
		
		this.setInstance(ai);
		// return savesuccess info to client
		FacesUtil.addInfoMessage("关闭自动投标成功！");
		return "pretty:autoInvest";
	}
	
	public long getOrderByUserId(String userId){
		/*String hql = "select count(ai) from AutoInvest ai where ai.status =?" +
				" and ai.lastAutoInvestTime<=(select ai2.lastAutoInvestTime from AutoInvest ai2 where ai2.user.id=?)" +
				" and ai.seqNum<(select ai2.seqNum from AutoInvest ai2 where ai2.user.id=?)";*/
//		Object o = getBaseService().find(hql, new String[]{InvestConstants.AutoInvest.Status.ON, userId,userId}).get(0);
//		if (o == null) {
//			return 0L;
//		}
		//FIXME: 需要解决性能问题
		
		long index = 0 ; 
		// 遍历所有自动投标用户
		List<AutoInvest> ais = getBaseService().find("from AutoInvest ai where ai.status = '"
				+ InvestConstants.AutoInvest.Status.ON
				+ "' order by ai.lastAutoInvestTime asc, ai.seqNum asc");
		
		for (int i = 0; i < ais.size(); i++) {
			if( StringUtils.equals(ais.get(i).getUserId(), userId) ){
				break ;
			}
			index ++ ;
		}
		
		
		return (Long) index+1;
	}
	
}
