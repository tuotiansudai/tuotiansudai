package com.esoft.archer.notice.model;

import java.util.Date;

/**
 * 通知
 * 
 * @author Administrator
 * 
 */
public class Notice {

	/**
	 * 消息内容
	 */
	private String message;
	/**
	 * 产生时间
	 */
	private Date occurTime;

	public Notice() {
	}

	public Notice(String message) {
		this.message = message;
		this.occurTime = new Date();
	}

	@Override
	public String toString() {
		return this.message + this.occurTime.getTime();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getOccurTime() {
		return occurTime;
	}

	public void setOccurTime(Date occurTime) {
		this.occurTime = occurTime;
	}

}
