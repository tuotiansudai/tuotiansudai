package com.esoft.umpay.query.controlller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.core.annotations.ScopeType;
import com.esoft.umpay.query.model.TransferDateil;
import com.esoft.umpay.query.service.impl.UmPayQueryLoanStatusOperation;
import com.esoft.umpay.query.service.impl.UmPayQueryTransferOperation;
import com.esoft.umpay.query.service.impl.UmPayQueryUserOperation;
import com.esoft.umpay.query.service.impl.UmpayQueryMerOperation;
import com.esoft.umpay.query.service.impl.UmpayQueryTransfeqOperation;


/**
 * Description : 
 * @author zt
 * @data 2015-4-10上午11:01:41
 */
@Component
@Scope(ScopeType.VIEW)
public class UmPayQueryHome {

	
	
	@Resource
	UmPayQueryUserOperation umPayQueryUserOperation;
	
	@Resource
	UmPayQueryLoanStatusOperation umPayQueryLoanStatusOperation;
	
	@Resource
	UmPayQueryTransferOperation umPayQueryTransferOperation;
	
	@Resource
	UmpayQueryMerOperation umpayQueryMerOperation;
	
	@Resource
	UmpayQueryTransfeqOperation umpayQueryTransfeqOperation;
	
	private String platformUserNo;
	private String orderId;
	private String orderType;
	private Date orderDate;
	private Date startDate;
	private Date endDate;
	private String pageNum;
	
	private List<TransferDateil> transferDetaList;
	/**
	 * 查询用户状态
	 */
	public void queryUserStatus(){
		umPayQueryUserOperation.handleSendedOperation(platformUserNo);
	}
	
	/**
	 *查询标的状态 
	 */
	public void queryLoanStatus(){
		umPayQueryLoanStatusOperation.handleSendedOperation(orderId);
	}
	
	/**
	 * 查询交易状态
	 */
	public void queryTransferStatus(){
		umPayQueryTransferOperation.handleSendedOperation(orderId, orderType , orderDate);
	}
	
	/**
	 * 查询商户信息
	 */
	public void queryMerInfo(){
		umpayQueryMerOperation.handleSendedOperation(orderId);
	}
	
	/**
	 * 查询账单流水
	 */
	public List<TransferDateil> getTransferDetaList(){
		transferDetaList = umpayQueryTransfeqOperation.handleSendedOperation(startDate, endDate, orderType, platformUserNo , pageNum);
		return transferDetaList;
	}
	
	
	public String getPlatformUserNo() {
		return platformUserNo;
	}
	public void setPlatformUserNo(String platformUserNo) {
		this.platformUserNo = platformUserNo;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getPageNum() {
		return pageNum;
	}
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	
}
