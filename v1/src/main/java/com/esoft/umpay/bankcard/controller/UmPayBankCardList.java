package com.esoft.umpay.bankcard.controller;

import java.util.List;

import javax.annotation.Resource;

import com.esoft.jdp2p.bankcard.service.BankCardService;
import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.bankcard.controller.BankCardList;
import com.esoft.jdp2p.bankcard.model.BankCard;

public class UmPayBankCardList  extends BankCardList {

	@Resource
	private LoginUserInfo loginUserInfo;

	@Resource
	private BankCardService bankCardService;

	@Logger
	Log log;

	private List<BankCard> bankCardListbyLoginUser;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public List<BankCard> getBankCardListbyLoginUser() {
		
		User loginUser = getHt().get(User.class, loginUserInfo.getLoginUserId());
		if (loginUser == null) {
			FacesUtil.addErrorMessage("用户没有登录");
			return null;
		}
		bankCardListbyLoginUser = getHt().find(
				"from BankCard where user.id =? and status!=? and status!=?", new String[]{loginUser.getId(), "delete" , "delete_for_replace"});
		return bankCardListbyLoginUser;
	}

	public List<BankCard> getBoundBankCardsByUserId(String userId) {
		return bankCardService.getBoundBankCardsByUserId(userId);
	}

	@Override
	public Class<BankCard> getEntityClass() {
		return BankCard.class;
	}

	
	
}
