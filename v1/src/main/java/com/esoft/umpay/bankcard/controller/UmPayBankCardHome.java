package com.esoft.umpay.bankcard.controller;

import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.bankcard.controller.BankCardHome;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.jdp2p.bankcard.service.BankCardService;
import com.esoft.jdp2p.user.service.RechargeService;
import com.esoft.umpay.bankcard.service.impl.UmPayBindingBankCardOperation;
import com.esoft.umpay.bankcard.service.impl.UmPayReplaceBankCardOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Date;

public class UmPayBankCardHome extends BankCardHome {

	private static final long serialVersionUID = 8879824209863500790L;
	@Resource
	HibernateTemplate ht;
	@Resource
	private LoginUserInfo loginUserInfo;
	@Resource
	UmPayBindingBankCardOperation umPayBindingBankCardOperation;
	@Resource
	UmPayReplaceBankCardOperation umPayReplaceBankCardOperation;
	@Resource
	private RechargeService rechargeService;
	@Resource
	private BankCardService bankCardService;
	@Logger
	private static Log log;

	/**
	 * 绑定银行卡
	 */
	@Transactional(readOnly = false)
	public void bindingCardTrusteeship() {
		User loginUser = getBaseService().get(User.class, loginUserInfo.getLoginUserId());
		if (loginUser == null) {
			FacesUtil.addErrorMessage("用户未登录");
			return;
		}
		if (StringUtils.isNotEmpty(this.getInstance().getCardNo()) && this.bankCardService.isCardNoBinding(this.getInstance().getCardNo())) {
			FacesUtil.addErrorMessage("此银行卡已经被绑定！！！！");
			return;
		}
		if (StringUtils.isEmpty(this.getInstance().getId())) {
			getInstance().setId(IdGenerator.randomUUID());
			getInstance().setUser(loginUser);
			getInstance().setStatus("uncheck");
		} else {
			this.setId(getInstance().getId());
		}
		getInstance().setTime(new Date());
		super.save(false);
		try {
			umPayBindingBankCardOperation.createOperation(getInstance(), FacesContext.getCurrentInstance());
		} catch (IOException e) {
			FacesUtil.addErrorMessage("绑定银行卡失败!");
			log.error(e);
		} finally {
			this.setInstance(null);
		}
	}

	/**
	 * 保存一张银行卡信息
	 */
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
			getInstance().setStatus("uncheck");
			getInstance().setBank(
					rechargeService.getBankNameByNo(getInstance().getBankNo()));
		} else {
			this.setId(getInstance().getId());
		}
		getInstance().setTime(new Date());
		super.save(false);
		this.setInstance(null);
		FacesUtil.addInfoMessage("保存银行卡成功！");
		if (StringUtils.isNotEmpty(super.getSaveView())) {
			return super.getSaveView();
		}
		return "pretty:bankCardList";
	}

	/**
	 * 删除银行卡
	 */
	@Transactional(readOnly = false)
	public String delete(String bankCardId) {
		BankCard bc = getBaseService().get(BankCard.class, bankCardId);
		if (bc == null) {
			FacesUtil.addErrorMessage("未找到编号为" + bankCardId + "的银行卡！");
		} else {
			// 银行卡标记为删除状态
			this.setInstance(bc);
			if (bc.getStatus().equals("passed")) {
				this.getInstance().setStatus("delete_for_replace");
			} else {
				this.getInstance().setStatus("delete");
			}
			getBaseService().update(this.getInstance());
			this.setInstance(null);
		}
		return "pretty:withdraw";
	}

	@Override
	public Class<BankCard> getEntityClass() {
		return BankCard.class;
	}

}
