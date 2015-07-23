package com.esoft.jdp2p.bankcard.controller;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.bankcard.BankCardConstants;
import com.esoft.jdp2p.bankcard.BankCardConstants.BankCardStatus;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.jdp2p.bankcard.service.BankCardService;
import com.esoft.jdp2p.user.service.RechargeService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Component
@Scope(ScopeType.VIEW)
public class BankCardHome extends EntityHome<BankCard> implements java.io.Serializable{

	@Logger
	static Log log;
	@Resource
	HibernateTemplate ht;

	@Resource
	private LoginUserInfo loginUserInfo;
	
	@Resource
	private RechargeService rechargeService ;

	@Resource
	private BankCardService bankCardService;

	private boolean isUpdateBankCard;

	@Override
	@Transactional(readOnly = false)
	public String save() {
		User loginUser = getBaseService().get(User.class,
				loginUserInfo.getLoginUserId());
		if (loginUser == null) {
			FacesUtil.addErrorMessage("用户未登录");
			return null;
		}
		if (StringUtils.isEmpty(this.getInstance().getId())) {
			getInstance().setId(IdGenerator.randomUUID());
			getInstance().setUser(loginUser);
			getInstance().setStatus(BankCardConstants.BankCardStatus.BINDING);		
			getInstance().setBank(rechargeService.getBankNameByNo(getInstance().getBankNo()));
		} else {
			this.setId(getInstance().getId());
		}
		getInstance().setTime(new Date());
		super.save(false);
		this.setInstance(null);
		FacesUtil.addInfoMessage("保存银行卡成功！");
		if(StringUtils.isNotEmpty(super.getSaveView())){
			return super.getSaveView();
		}
		return "pretty:bankCardList";
	}

	@Override
	@Transactional(readOnly = false)
	public String delete() {
		// 银行卡标记为删除状态
		this.getInstance().setStatus(BankCardStatus.DELETED);
		getBaseService().update(this.getInstance());
		return "pretty:bankCardList";
	}

	@Transactional(readOnly = false)
	public String delete(String bankCardId) {
		BankCard bc = getBaseService().get(BankCard.class, bankCardId);
		if (bc == null) {
			FacesUtil.addErrorMessage("未找到编号为" + bankCardId + "的银行卡！");
		} else {
			// 银行卡标记为删除状态
			this.setInstance(bc);
			this.getInstance().setStatus(BankCardStatus.DELETED);
			getBaseService().update(this.getInstance());
			this.setInstance(null);
		}
		return "pretty:bankCardList";
	}

	public void updateBankCard(BankCard bankCard) {
		super.setInstance(bankCard);
		this.isUpdateBankCard = true;
	}

	public void resetBankCard() {
		super.setInstance(null);
		this.isUpdateBankCard = false;
	}

	public boolean getIsUpdateBankCard() {
		return isUpdateBankCard;
	}

	public void setIsUpdateBankCard(boolean isUpdateBankCard) {
		this.isUpdateBankCard = isUpdateBankCard;
	}

	/**
	 * 删除银行卡，资金托管
	 * 
	 * @return
	 */
	public String deleteTrusteeship() {
		throw new RuntimeException("you must override this method!");
	}

	public boolean isBoundBankCard(String userId) {
		return bankCardService.isBoundBankCard(userId);
	}

	public boolean isOpenFastPay(String userId) {
		return this.bankCardService.isOpenFastPayment(userId);
	}

	public boolean isFastPaymentBank(String bankNo) {
		return this.rechargeService.isFastPaymentBank(bankNo);
	}

	public boolean isExistsBankPhoto(String bankNo){
		return this.bankCardService.isExistsBankPhoto(bankNo);
	}

}
