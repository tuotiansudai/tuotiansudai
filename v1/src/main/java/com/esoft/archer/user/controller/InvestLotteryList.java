package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.ReferGradeProfitSys;
import com.esoft.archer.user.model.ReferrerInvest;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserBill;
import com.esoft.core.annotations.ScopeType;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ttsd.special.model.InvestLottery;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Scope(ScopeType.VIEW)
public class InvestLotteryList extends EntityQuery<InvestLottery> implements Serializable {

	private Date startTime;

	private Date endTime;

	public InvestLotteryList(){
		final String[] RESTRICTIONS = {
				"user = #{investLotteryList.example.user}",
				"valid = #{investLotteryList.example.valid}",
				"awardTime >= #{investLotteryList.startTime}",
				"awardTime <= #{investLotteryList.endTime}"};
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Override
	protected void initExample() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		if(Strings.isNullOrEmpty(url) && url.indexOf("admin") > -1){
			super.initExample();
		}else{
			InvestLottery example = new InvestLottery();
			setExample(example);
			example.setUser(new User());
		}
	}

}
