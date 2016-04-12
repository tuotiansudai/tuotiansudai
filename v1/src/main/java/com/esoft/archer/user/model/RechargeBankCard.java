package com.esoft.archer.user.model;

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
public interface RechargeBankCard {

	public String getNo();

	public void setNo(String no);

	public String getBankName();

	public void setBankName(String bankName);

	public String getCardAlias();

	public void setCardAlias(String cardAlias);

}
