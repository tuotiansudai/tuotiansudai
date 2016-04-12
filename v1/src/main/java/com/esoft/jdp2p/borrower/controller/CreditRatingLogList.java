package com.esoft.jdp2p.borrower.controller;

import java.util.Arrays;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.borrower.model.CreditRatingLog;

@Component
@Scope(ScopeType.REQUEST)
public class CreditRatingLogList extends EntityQuery<CreditRatingLog> {

	private Date startTime;
	private Date endTime;
	public CreditRatingLogList(){
		final String[] RESTRICTIONS = {"userId like #{loanerInfoList.example.userId}",
				"user = #{creditRatingLogList.example.user}",
				"operator >= #{creditRatingLogList.example.operator}",
				"time >= #{creditRatingLogList.startTime}",
				"time <= #{creditRatingLogList.endTime}"};
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
	
}
