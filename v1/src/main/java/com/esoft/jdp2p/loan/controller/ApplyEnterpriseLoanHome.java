package com.esoft.jdp2p.loan.controller;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.loan.model.ApplyEnterpriseLoan;
import com.esoft.jdp2p.loan.service.LoanService;

/**
 * Description:企业借款申请Home Copyright: Copyright (c)2013 Company: jdp2p
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-11 下午4:28:49
 * 
 *           Modification History: Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-11 wangzhi 1.0 Version
 */
@Component
@Scope(ScopeType.VIEW)
public class ApplyEnterpriseLoanHome extends EntityHome<ApplyEnterpriseLoan> implements java.io.Serializable {
	@Resource
	LoginUserInfo loginUserInfo;

	@Resource
	LoanService loanService;

	/**
	 * 企业借款申请
	 */
	@Override
	public String save() {
		//获取登录用户
		User loginUser = getBaseService().get(User.class, loginUserInfo.getLoginUserId());
		if (loginUser == null) {
			FacesUtil.addErrorMessage("请先登陆");
		}
		this.getInstance().setUser(loginUser);
		loanService.applyEnterpriseLoan(this.getInstance());
		FacesUtil.addInfoMessage("您的融资申请已经提交，请等待管理员的审核！");
		return "pretty:user_loan_applying-p2c";
	}

	/**
	 * 审核企业借款
	 */
	public void verify(ApplyEnterpriseLoan ael) {
		loanService.verifyEnterpriseLoan(ael);
	}
	
	@Override
	public Class<ApplyEnterpriseLoan> getEntityClass() {
		return ApplyEnterpriseLoan.class;
	}

}
