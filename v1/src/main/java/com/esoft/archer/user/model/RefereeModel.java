package com.esoft.archer.user.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Hch 推荐人模型
 * 
 */
public class RefereeModel implements Serializable {

	private static final long serialVersionUID = 8833629605460866433L;
	private String referee;// 推荐人名称
	private Date startTime, endTime;// 日期
	private Double sumMoney;// 总金额

	public RefereeModel() {
	}

	public RefereeModel(String referee, Double sumMoney, Date startTime,
			Date endTime) {
		this.referee = referee;
		this.sumMoney = sumMoney;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getReferee() {
		return referee;
	}

	public void setReferee(String referee) {
		this.referee = referee;
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

	public Double getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(Double sumMoney) {
		this.sumMoney = sumMoney;
	}

}