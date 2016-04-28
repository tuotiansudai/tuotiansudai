package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.model.UserInfoLog;
import com.esoft.archer.user.model.UserLoginLog;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.util.StringManager;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Component
@Scope(ScopeType.VIEW)
public class UserInfoLogList extends EntityQuery<UserInfoLog> {

	private static StringManager sm = StringManager
			.getManager(UserConstants.Package);

	@Logger
	private static Log log ;

	public UserInfoLogList() {
		final String[] RESTRICTIONS = {
				"objId like #{userInfoLogList.example.objId}",
				"userId like #{userInfoLogList.example.userId}",
				"ip like #{userInfoLogList.example.ip}",
				"success = #{userInfoLogList.example.success}",
				"operateTime >= #{userInfoLogList.operateTimeStart}",
				"operateTime <= #{userInfoLogList.operateTimeEnd}"};
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	private Date operateTimeStart;
	private Date operateTimeEnd;

	public Date getOperateTimeStart() {
		return operateTimeStart;
	}
	public void setOperateTimeStart(Date operateTimeStart) {
		this.operateTimeStart = operateTimeStart;
	}
	public Date getOperateTimeEnd() {
		return operateTimeEnd;
	}
	public void setOperateTimeEnd(Date operateTimeEnd) {
		this.operateTimeEnd = operateTimeEnd;
	}

	
	


}
