package com.esoft.jdp2p.bankcard.model;

// default package

import com.esoft.archer.user.model.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * BankCard entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "bank_card")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class BankCard implements java.io.Serializable {

	// Fields

	private String id;
	private User user;
	// 开户行
	private String name;
	// 银行名称
	private String bank;
	/** 银行账户服务类型，对公、对私 */
	private String bankServiceType;
	// 银行编号
	private String bankNo;
	// 银行所在省份
	private String bankProvince;
	// 银行所在城市
	private String bankCity;
	// 账户类型
	private String bankCardType;
	// 开户行地址
	private String bankArea;
	// 银行卡账户名称
	private String accountName;
	// 银行卡号
	private String cardNo;
	// 绑定金额
	private Double bindingprice;
	private Date time;
	private String status;

	private boolean isOpenFastPayment;

	@Column(name = "is_open_fastPayment")
	public boolean getIsOpenFastPayment() {
		return isOpenFastPayment;
	}

	public void setIsOpenFastPayment(boolean isOpenFastPayment) {
		this.isOpenFastPayment = isOpenFastPayment;
	}
	// Constructors

	/** default constructor */
	public BankCard() {
		this.bankCardType = "DEBIT";
	}

	/** full constructor */
	public BankCard(String id, User user, String name, String bank,
			String bankArea, String cardNo, Date time, String status) {
		this.id = id;
		this.user = user;
		this.name = name;
		this.bank = bank;
		this.bankArea = bankArea;
		this.cardNo = cardNo;
		this.time = time;
		this.status = status;
	}

	@Column(name = "bank", length = 100)
	public String getBank() {
		return this.bank;
	}

	@Column(name = "bank_area", length = 512)
	public String getBankArea() {
		return this.bankArea;
	}

	@Column(name = "bank_city", length = 100)
	public String getBankCity() {
		return bankCity;
	}

	@Column(name = "bank_no", length = 128)
	public String getBankNo() {
		return bankNo;
	}

	@Column(name = "bank_province", length = 100)
	public String getBankProvince() {
		return bankProvince;
	}

	@Column(name = "bank_card_type", length = 100)
	public String getBankCardType() {
		if (this.bankCardType == null) {
			this.bankCardType = "DEBIT";
		}
		return bankCardType;
	}

	@Column(name = "account_name", length = 200)
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setBankCardType(String bankCardType) {
		this.bankCardType = bankCardType;
	}

	@Column(name = "binding_price")
	public Double getBindingprice() {
		return bindingprice;
	}

	@Column(name = "card_no", length = 100)
	public String getCardNo() {
		return this.cardNo;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	@Column(name = "name", length = 100)
	public String getName() {
		return this.name;
	}

	@Column(name = "status", nullable = false, length = 50)
	public String getStatus() {
		return this.status;
	}

	@Column(name = "time", nullable = false, length = 19)
	public Date getTime() {
		return this.time;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return this.user;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public void setBankArea(String bankArea) {
		this.bankArea = bankArea;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}

	public void setBindingprice(Double bindingprice) {
		this.bindingprice = bindingprice;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "bank_service_type", length = 100)
	public String getBankServiceType() {
		return bankServiceType;
	}

	public void setBankServiceType(String bankServiceType) {
		this.bankServiceType = bankServiceType;
	}

}