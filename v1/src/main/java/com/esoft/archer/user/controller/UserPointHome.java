package com.esoft.archer.user.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.config.service.ConfigService;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserPoint;
import com.esoft.archer.user.model.UserPointHistory;
import com.esoft.archer.user.service.UserPointService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;
import com.esoft.jdp2p.loan.exception.InsufficientBalance;

@Component
@Scope(ScopeType.VIEW)
public class UserPointHome extends EntityHome<UserPoint> {

	@Logger
	private static Log log;
	@Resource
	private UserPointService userPointService;
	@Resource
	private LoginUserInfo loginUserInfo;
	
	private String operatorType ;

	private UserPointHistory userPointHistory;// 用户积分历史

	public UserPointHistory getUserPointHistory() {
		return userPointHistory;
	}

	public void setUserPointHistory(UserPointHistory userPointHistory) {
		this.userPointHistory = userPointHistory;
	}

	public UserPointHome() {
		userPointHistory = new UserPointHistory();

	}

	@Override
	protected UserPoint createInstance() {
		UserPoint point = new UserPoint();
		point.setUser(new User());
		return point;
	}
	
	
	public String modifyPointByAdmin(){
		if(StringUtils.isEmpty(getInstance().getUser().getId())){
			FacesUtil.addErrorMessage("用户编号不能为空！");
			return null ;
		}
		
		if(StringUtils.equals(operatorType, "ADD")){//FIXME：改为系统可以配置
			return addPointByAdmin();
		}else if(StringUtils.equals(operatorType, "MINUS")){
			return minusPointByAdmin();
		}else{
			FacesUtil.addErrorMessage("修改用户积分时，未知的操作类型，操作类型应该为增加或者减少");
			return null ;
		}
		
		
	}

	/**
	 * 管理员从后台给用户增加积分操作
	 * 
	 * @return
	 */
	public String addPointByAdmin() {
		if (log.isInfoEnabled())
			log.info(userPointHistory.getType().concat(",")
					.concat(userPointHistory.getRemark()));
		userPointService.add(getInstance().getUser().getId(),
				userPointHistory.getPoint(), userPointHistory.getType(),
				userPointHistory.getType(), userPointHistory.getRemark());
		FacesUtil.addInfoMessage("增加积分成功！");
		return FacesUtil.redirect(UserConstants.View.POINT_HISTORY_LIST);
	}

	/**
	 * 管理员从后台减少用户的积分操作
	 * 
	 * @return
	 */
	public String minusPointByAdmin() {
		if (log.isInfoEnabled())
			log.info(userPointHistory.getType().concat(",")
					.concat(userPointHistory.getRemark()));
		try {
			userPointService.minus(getInstance().getUser().getId(),
					userPointHistory.getPoint(), userPointHistory.getType(),
					userPointHistory.getType(), userPointHistory.getRemark());
			FacesUtil.addInfoMessage("减少积分成功！");
		} catch (InsufficientBalance e) {
			FacesUtil.addErrorMessage("积分不足！");
		}
		return FacesUtil.redirect(UserConstants.View.POINT_HISTORY_LIST);
	}

	/**
	 * 根据用户的编号和类型来获取用户积分
	 * 
	 * @param type
	 *            （升级积分、消费积分）
	 * @return 用户积分对象
	 */
	public UserPoint getPointByUserId(String type) {
		return userPointService.getPointByUserId(
				loginUserInfo.getLoginUserId(), type);
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

}
