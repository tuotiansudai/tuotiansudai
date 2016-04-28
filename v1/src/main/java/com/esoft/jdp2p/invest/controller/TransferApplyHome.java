package com.esoft.jdp2p.invest.controller;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.config.service.ConfigService;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.invest.exception.ExceedInvestTransferMoney;
import com.esoft.jdp2p.invest.exception.InvestTransferException;
import com.esoft.jdp2p.invest.model.TransferApply;
import com.esoft.jdp2p.invest.service.TransferService;

/**
 * Filename: InvestHome.java Description: Copyright: Copyright (c)2013 Company:
 * jdp2p
 * 
 * @author: yinjunlu
 * @version: 1.0 Create at: 2014-1-11 下午4:26:10
 * 
 *           Modification History: Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-11 yinjunlu 1.0 1.0 Version
 */
@Component
@Scope(ScopeType.VIEW)
public class TransferApplyHome extends EntityHome<TransferApply> {

	@Resource
	private TransferService transferService;

	@Resource
	private LoginUserInfo loginUserInfo;

	@Resource
	private ConfigService configService;

	/**
	 * 申请债权转让
	 * 
	 * @param investId
	 *            被转让的还款Id
	 * @param money
	 *            转让的本金
	 * @param transferMoney
	 *            债权转让价格
	 * @param transferRate
	 *            债权转让的利率
	 * @param deadline
	 *            债权转让到期时间
	 */
	public String applyInvestTransfer() {
		TransferApply ta = this.getInstance();

		try {
			transferService.canTransfer(ta.getInvest().getId());
			transferService.applyInvestTransfer(ta);
			FacesUtil.addInfoMessage("债权转让申请成功！");
			return "pretty:user-transfer-transfering";
		} catch (ExceedInvestTransferMoney e) {
			FacesUtil.addErrorMessage(e.getMessage());
		} catch (InvestTransferException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
		return null;

	}

	/**
	 * 取消债权
	 * 
	 * @param investTransferId
	 *            债权Id
	 */
	public void cancel(String investTransferId) {
		transferService.cancel(investTransferId);
		FacesUtil.addInfoMessage("取消债权成功");
	}

	public boolean canTransfer(String investId) {
		try {
			return transferService.canTransfer(investId);
		} catch (InvestTransferException e) {
			//TODO:此处可以log.debug返回false的原因，便于查找为嘛无法进行债权转让
			return false;
		}
	}

}
