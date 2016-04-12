package com.esoft.umpay.withdraw.controller;

import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.controller.WithdrawHome;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.user.service.WithdrawCashService;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.esoft.umpay.withdraw.service.impl.UmPayWithdrawOperation;
import com.ttsd.api.dto.AccessSource;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Description :提现
 * 
 * @author zt
 * @data 2015-3-12上午8:50:31
 */

public class UmPayWithdrawHome extends WithdrawHome {

	@Resource
	WithdrawCashService wcs;

	@Resource
	UserBillBO userBillBO;

	@Resource
	LoginUserInfo loginUserInfo;

	@Resource
	UmPayWithdrawOperation umPayWithdrawOperation;

	@Override
	public String withdraw() {
		try {
			String userId = loginUserInfo.getLoginUserId();
			this.getInstance().setUser(new User(userId));
			this.getInstance().setSource(AccessSource.WEB.name());
			wcs.applyWithdrawCash(this.getInstance());
			umPayWithdrawOperation.createOperation(this.getInstance(),
					FacesContext.getCurrentInstance());
		} catch (InsufficientBalance e) {
			FacesUtil.addErrorMessage("余额不足！");
			return null;
		} catch (IOException e) {
			FacesUtil.addErrorMessage("提现出错！");
			e.printStackTrace();
		} catch (UmPayOperationException e) {
			FacesUtil.addErrorMessage("提现出错：" + e.getMessage());
		}
		return null;
	}

}
