package com.esoft.umpay.loan.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;

import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.umpay.api.util.DataUtil;
import org.apache.commons.logging.Log;

import com.esoft.archer.notice.model.Notice;
import com.esoft.archer.notice.model.NoticePool;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.LoanConstants.LoanStatus;
import com.esoft.jdp2p.loan.controller.LoanHome;
import com.esoft.jdp2p.loan.exception.ExistWaitAffirmInvests;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanService;
import com.esoft.umpay.loan.service.impl.UmPayCancelLoanOperation;
import com.esoft.umpay.loan.service.impl.UmPayCreateLoanOperation;
import com.esoft.umpay.loan.service.impl.UmPayLoaingOperation;
import com.esoft.umpay.loan.service.impl.UmPayLoanStatusService;
import com.esoft.umpay.loan.service.impl.UmPayPassLoanApplyOperation;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description :
 * 
 * @author zt
 * @data 2015-3-9下午9:50:18
 */
@SuppressWarnings("serial")
public class UmPayLoanHome extends LoanHome {

	@Resource
	UmPayPassLoanApplyOperation umPayPassLoanApplyOperation;

	@Resource
	UmPayCreateLoanOperation umPayCreateLoanOperation;

	@Resource
	UmPayLoaingOperation umPayLoaingOperation;

	@Resource
	UmPayCancelLoanOperation umPayCancelLoanOperation;

	@Resource
	LoginUserInfo loginUserInfo;

	@Resource
	private UserService userService;

	@Resource
	LoanService loanService;

	@Resource
	NoticePool noticePool;

	@Resource
	UmPayLoanStatusService umPayLoanStatusService;

	@Logger
	Log log;

	@Resource
	HibernateTemplate ht;

	/**
	 * 管理员建立项目
	 */
	@Override
	public String createAdminLoan() {
		Loan loan = this.getInstance();
		if (!userService.hasRole(loan.getUser().getId(), "INVESTOR")
				&& !userService.hasRole(loan.getUser().getId(), "LOANER")) {
			FacesUtil.addErrorMessage("用户：" + loan.getUser().getId()
					+ "未实名认证，不能发起借款！");
			return null;
		}
		String userId = loan.getUser().getId();
		List<TrusteeshipAccount> taList = ht.find( "from TrusteeshipAccount t where t.user.id=? and status = 'passed' ",new String[]{userId});
		if (taList == null || taList.size() <= 0){
			FacesUtil.addErrorMessage("代理人：" + loan.getUser().getId()
					+ "未实名认证，不能发起借款！");
			return  null;
		}
		if(loan.getInvestBeginTime()!=null
				&& loan.getExpectTime()!=null
				&& loan.getInvestBeginTime().after(loan.getExpectTime())){
			FacesUtil.addInfoMessage("起投时间不能晚于预计执行时间");
			return null;
		}
		// 创建一个标的操作
		try {
			umPayCreateLoanOperation.createOperation(loan,
					FacesContext.getCurrentInstance());
		} catch (IOException e) {
			FacesUtil.addErrorMessage("发布借款失败");
			return null;
		} catch (UmPayOperationException e) {
			FacesUtil.addErrorMessage(e.getMessage());
			return null;
		}
		FacesUtil.addInfoMessage("发布借款成功");
		return FacesUtil.redirect(loanListUrl);
	}

	/**
	 * 申请借款
	 */
	@Override
	public String save() {
		try {
			User user = this.userService.getUserById(this.loginUserInfo
					.getLoginUserId());

			((Loan) getInstance()).setUser(user);
			this.loanService.applyLoan((Loan) getInstance());

			// 用户在前台发起借款 通知第三方发标的
			this.umPayLoanStatusService.createLoan((Loan) getInstance());
			FacesUtil.addInfoMessage("发布借款成功，请填写个人基本信息。");
			return "pretty:loanerPersonInfo";
		} catch (InsufficientBalance e) {
			FacesUtil.addErrorMessage(e.getMessage());
			this.log.debug("余额不足，无法支付借款保证金! 标号: "
					+ ((Loan) getInstance()).getId());
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录!");
			this.log.debug("用户未登录! 标号: " + ((Loan) getInstance()).getId());
			e.printStackTrace();
		} catch (ReqDataException e) {
			FacesUtil.addErrorMessage("发标创建请求数据失败! 标号: "
					+ ((Loan) getInstance()).getId());
			this.log.debug("发标创建请求数据失败!标号:" + ((Loan) getInstance()).getId());
			e.printStackTrace();
		} catch (RetDataException e) {
			FacesUtil.addErrorMessage("发标解析请求回调数据失败! 标号: "
					+ ((Loan) getInstance()).getId());
			this.log.debug("发标解析请求回调数据失败!标号" + ((Loan) getInstance()).getId());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 审核借款信息
	 */
	@Override
	public String verify() {
		if (getIspass()) {
			if (this.getInstance().getExpectTime() == null) {
				FacesUtil.addErrorMessage("请填写预计执行时间。");
				setIspass(false);
				return null;
			}
			if(this.getInstance().getInvestBeginTime()!=null
					&& this.getInstance().getExpectTime()!=null
					&& this.getInstance().getInvestBeginTime().after(this.getInstance().getExpectTime())){
				FacesUtil.addInfoMessage("起投时间不能晚于预计执行时间");
				setIspass(false);
				return null;
			}
			try {
				this.getInstance().setVerifyUser(
						new User(loginUserInfo.getLoginUserId()));
				umPayPassLoanApplyOperation.createOperation(getInstance(),
						FacesContext.getCurrentInstance());
				FacesUtil.addInfoMessage("通过借款申请");
				noticePool.add(new Notice("借款：" + getInstance().getId()
						+ "通过申请"));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UmPayOperationException e) {
				FacesUtil.addErrorMessage(e.getMessage());
			}
		} else {
			loanService.refuseApply(this.getInstance().getId(), this
					.getInstance().getVerifyMessage(), loginUserInfo
					.getLoginUserId());
			FacesUtil.addInfoMessage("拒绝借款申请");
			noticePool.add(new Notice("借款：" + getInstance().getId() + "申请被拒绝"));
		}
		return FacesUtil.redirect(loanListUrl);
	}

	/**
	 * 放款
	 */
	@Override
	public String recheck() {
		try {
			loanService.changeInvestFromWaitAffirmToUnfinished(getInstance().getId());
			Loan loan = getBaseService().get(Loan.class, getInstance().getId());
			umPayLoaingOperation.createOperation(loan, FacesContext.getCurrentInstance());
			FacesUtil.addInfoMessage("放款成功");
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
			FacesUtil.addErrorMessage(e.getMessage());
		} catch (ExistWaitAffirmInvests e) {
			log.error(e.getStackTrace());
			FacesUtil.addErrorMessage(e.getMessage());
		}
		return FacesUtil.redirect(loanListUrl);
	}

	/**
	 * 流标
	 */
	@Override
	@Transactional
	public String failByManager() {
		Date now = new Date();
		long thirtyMinutes = 1000 * 60 * 30;
		Loan loan = getBaseService().get(Loan.class, getInstance().getId());
		List<Invest> invests = loan.getInvests();
		for (Invest invest : invests) {
			if (invest.getStatus().equals(InvestConstants.InvestStatus.WAIT_AFFIRM)) {
				Date investTime = invest.getTime();
				if (now.getTime() - investTime.getTime() < thirtyMinutes) {
					FacesUtil.addInfoMessage("放款失败，存在等待第三方资金托管确认的投资。");
					return null;
				}

			}
		}
		for (Invest invest : invests) {
			if (invest.getStatus().equals(InvestConstants.InvestStatus.WAIT_AFFIRM)) {
				invest.setStatus(InvestConstants.InvestStatus.UNFINISHED);
			}
		}
		if (loan.getStatus().equals(LoanStatus.WAITING_VERIFY)) {
			return super.failByManager();
		}
		try {
			for (Invest invest : invests) {
				if (invest.getStatus().equals(
						InvestConstants.InvestStatus.WAIT_AFFIRM)) {
					// 流标时候，需要检查是否要等待确认的投资，如果有，则不让放款。
					throw new ExistWaitAffirmInvests("investID:" + invest.getId());
				}
			}
			umPayCancelLoanOperation.createOperation(loan, FacesContext.getCurrentInstance());
		} catch (IOException e) {
			FacesUtil.addErrorMessage("流标失败");
			return null;
		} catch (UmPayOperationException e) {
			FacesUtil.addErrorMessage(e.getMessage());
			return null;
		} catch (ExistWaitAffirmInvests e) {
			FacesUtil.addErrorMessage("流标失败，存在等待第三方资金托管确认的投资。");
			return null;
		}
		FacesUtil.addInfoMessage("流标成功");
		return FacesUtil.redirect(loanListUrl);
	}

	@Override
	public Class<Loan> getEntityClass() {
		return Loan.class;
	}

	/**
	 * 等待第三方确认信息,暂时未做与第三方同步的处理 保存项目 还款状态的项目直接修改不与第三方交互,其中的三项不允许修改
	 * 
	 * @Override public String update() { try { //正在还款的项目直接操作
	 *           if(LoanStatus.REPAYING.equals(getInstance().getStatus()) ||
	 *           LoanStatus.COMPLETE.equals(getInstance().getStatus())) {
	 *           umPayEditLoanOperation.createOperation(getInstance(),
	 *           FacesContext.getCurrentInstance()); }else{
	 *           loanService.update(getInstance()); } } catch (IOException e) {
	 *           e.printStackTrace(); FacesUtil.addInfoMessage("项目修改失败！"); }
	 *           FacesUtil.addInfoMessage("项目修改成功！"); return
	 *           FacesUtil.redirect(loanListUrl); }
	 */

	public void redirectToHomeWhenLoanIsNotExist(Loan loan) throws IOException {
		if (loan == null) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/");
		}
	}

}
