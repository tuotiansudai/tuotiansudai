package com.esoft.jdp2p.bankcard.service;

import com.esoft.jdp2p.bankcard.model.BankCard;

import java.util.List;


/**  
 * Filename:    BankService.java <br/>  
 * Company:     jdp2p <br/> 
 * Copyright:   Copyright (c)2013 <br/>
 * Description:  
 * @author:     wangzhi  
 * @version:    1.0
 * Create at:   2014-1-6 下午10:54:14  
 *  
 * Modification History: <br/>
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-1-6      wangzhi      1.0          
 */
public interface BankCardService {
	/**
	 * 获取当前用户绑定的银行卡
	 * @param userId 用户id
	 * @return
	 */
	List<BankCard> getBoundBankCardsByUserId(String userId);

	boolean isBoundBankCard(String userId);

	public boolean isOpenFastPayment(String userId);

	public boolean isExistsBankPhoto(String bankNo);

	public boolean isCardNoBinding(String cardNo);

}
