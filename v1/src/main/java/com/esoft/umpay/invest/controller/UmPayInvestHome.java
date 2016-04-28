package com.esoft.umpay.invest.controller;

import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.invest.controller.InvestHome;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.invest.service.InvestService;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.umpay.invest.service.impl.UmPayInvestOeration;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.ttsd.api.dto.AccessSource;
import org.apache.commons.logging.Log;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Date;

@SuppressWarnings("serial")
public class UmPayInvestHome extends InvestHome {

	@Resource
	UmPayInvestOeration umPayInvestOeration;

	@Resource
	LoginUserInfo loginUserInfo;

	@Resource
	private InvestService investService;

	@Logger
	Log log;

	/**
	 * 投资
	 */
	@Override
	public String save() {
		try {
			Loan loan = getBaseService().get(Loan.class,
					getInstance().getLoan().getId());
			if(loan.getInvestBeginTime()!=null && new Date().before(loan.getInvestBeginTime())){
				FacesUtil.addInfoMessage("暂未开标！");
				return null;
			}
			if (loan.getUser().getId().equals(loginUserInfo.getLoginUserId())) {
				FacesUtil.addInfoMessage("你不能投自己的项目！");
				return null;
			} /*else if(loan.getActualRate()){
				this.getInstance().setUser(
						new User(loginUserInfo.getLoginUserId()));
				this.getInstance().setIsAutoInvest(false);
				umPayInvestOeration.createOperation(getInstance(),
						FacesContext.getCurrentInstance());
			}*/else {
				this.getInstance().setUser(
						new User(loginUserInfo.getLoginUserId()));
				this.getInstance().setIsAutoInvest(false);
				this.getInstance().setSource(AccessSource.WEB.name());
				umPayInvestOeration.createOperation(getInstance(),
						FacesContext.getCurrentInstance());
			}
		} catch (UmPayOperationException e) {
			FacesUtil.addErrorMessage(e.getMessage());
			if (e.getMessage().equals("账户余额不足，请充值！")) {
				return "user/recharge";
			} else {
				return null;
			}
		} catch (IOException e) {
			FacesUtil.addErrorMessage("当前借款不可投资");
			return null;
		}
		FacesUtil.addInfoMessage("投资成功！");
		return null;
	}

	/**
	 * 投资-定向标
	 */
	public String saveDXB() {
		try {
			Loan loan = (Loan) getBaseService().get(Loan.class,
					((Invest) getInstance()).getLoan().getId());
			if(loan.getInvestBeginTime()!=null && new Date().before(loan.getInvestBeginTime())){
				FacesUtil.addInfoMessage("暂未开标！");
				return null;
			}
			if (loan.getUser().getId()
					.equals(this.loginUserInfo.getLoginUserId())) {
				FacesUtil.addInfoMessage("您不能投自己的项目！");
				return null;
			}
			if (null == loan.getInvestPassword()) {
				FacesUtil.addInfoMessage("请输入投资密码！");
				return null;
			}
			if (!loan.getInvestPassword().equals(
					((Invest) getInstance()).getUserInvestPass())) {
				FacesUtil.addInfoMessage("投资密码不匹配,请重试！");
				return null;
			}
			((Invest) getInstance()).setUser(new User(this.loginUserInfo
					.getLoginUserId()));
			((Invest) getInstance()).setIsAutoInvest(Boolean.valueOf(false));
			((Invest) this.getInstance()).setSource(AccessSource.WEB.name());
			this.umPayInvestOeration.createOperation((Invest) getInstance(),
					FacesContext.getCurrentInstance());
		} catch (UmPayOperationException e) {
			FacesUtil.addErrorMessage(e.getMessage());
			if (e.getMessage().equals("账户余额不足，请充值！")) {
				return "user/recharge";
			} else {
				return null;
			}
		} catch (IOException e) {
			FacesUtil.addErrorMessage("当前借款不可投资");
			return null;
		}
		FacesUtil.addInfoMessage("投资成功！");
		return null;
	}

	@Override
	public Class<Invest> getEntityClass() {
		return Invest.class;
	}

}
