package com.esoft.umpay.query.model;


public class TransferDateil {
	private String date;	//查询日期
	private Double amount;	//金额
	private String dcMark;	//
	private String transType;
	private String transState;
	private Double balance;
	private String orderId;
	private String tranTime;
	private String amtType;
	private String orderDate;
	private String pageNum;	//页数
	
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getAmtType() {
		return amtType;
	}
	public void setAmtType(String amtType) {
		this.amtType = amtType;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDcMark() {
		return dcMark;
	}
	public void setDcMark(String dcMark) {
		this.dcMark = dcMark;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getTransState() {
		return transState;
	}
	public void setTransState(String transState) {
		this.transState = transState;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTranTime() {
		return tranTime;
	}
	public void setTranTime(String tranTime) {
		this.tranTime = tranTime;
	}
	public String getPageNum() {
		return pageNum;
	}
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
}
