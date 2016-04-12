package com.esoft.archer.user.model;

import com.esoft.archer.user.model.RechargeBankCard;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 银行直连，银行bean
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-2-13 上午11:41:38
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-2-13 wangzhi 1.0
 */
public class RechargeBankCardImpl implements RechargeBankCard {

	private String no;
	private String bankName;
	private String cardAlias;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardAlias() {
		return cardAlias;
	}

	public void setCardAlias(String cardAlias) {
		this.cardAlias = cardAlias;
	}

	public RechargeBankCardImpl(String no, String bankName) {
		super();
		this.no = no;
		this.bankName = bankName;
	}

}