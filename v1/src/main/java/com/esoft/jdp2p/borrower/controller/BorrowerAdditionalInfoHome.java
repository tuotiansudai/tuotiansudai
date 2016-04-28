package com.esoft.jdp2p.borrower.controller;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.borrower.BorrowerConstant;
import com.esoft.jdp2p.borrower.model.BorrowerAdditionalInfo;
import com.esoft.jdp2p.borrower.service.BorrowerService;

/**  
 * Description:   
 * Copyright:   Copyright (c)2013 
 * Company:    jdp2p 
 * @author:     yinjunlu  
 * @version:    1.0  
 * Create at:   2014-1-21 下午9:02:57  
 *  
 * Modification History:  
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-1-21    yinjunlu             1.0        1.0 Version  
 */

@Component
@Scope(ScopeType.VIEW)
public class BorrowerAdditionalInfoHome extends EntityHome<BorrowerAdditionalInfo> {

	private boolean ispass = false;
	private String verifyMessage;
	
	@Resource
	private LoginUserInfo loginUser;
	
	@Resource
	private BorrowerService borrowService;
   
	/**
	 * 保存BorrowerAdditionalInfo，借款人工作财务信息
	 */
	@Override
	public String save() {
		//保存BorrowerAdditionalInfo，借款人工作财务信息
		borrowService.saveOrUpdateBorrowerAdditionalInfo(getInstance());
//		FacesUtil.addInfoMessage("保存成功，请等待审核。");
		return "pretty:loanerAuthentication";
	}

	// 初始化登录用户财务信息
	@Override
	protected void initInstance() {
		BorrowerAdditionalInfo lai = new BorrowerAdditionalInfo();
		try {
			lai = borrowService.initBorrowerAdditionalInfo(loginUser.getLoginUserId());
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		this.setInstance(lai);
	}
	
	/**
	 * 审核借款用户补充信息
	 * @return
	 */
	public String verify(BorrowerAdditionalInfo borrowerAdditionalInfo){
		borrowService.verifyBorrowerAdditionalInfo(borrowerAdditionalInfo.getUserId(),ispass,getInstance().getVerifiedMessage(),loginUser.getLoginUserId());
		FacesUtil.addInfoMessage("保存成功");
		return null;
	}

	public void initVerify(BorrowerAdditionalInfo borrowerAdditionalInfo){
		this.setInstance(borrowerAdditionalInfo);
		if((BorrowerConstant.Verify.passed).equals(this.getInstance().getVerified())){
			ispass = true;
		}
	}
	
	public boolean isIspass() {
		return ispass;
	}

	public void setIspass(boolean ispass) {
		this.ispass = ispass;
	}

	public String getVerifyMessage() {
		return verifyMessage;
	}

	public void setVerifyMessage(String verifyMessage) {
		this.verifyMessage = verifyMessage;
	}
}
