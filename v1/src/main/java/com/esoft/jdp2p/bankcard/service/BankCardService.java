package com.esoft.jdp2p.bankcard.service;

import java.util.List;

import com.esoft.jdp2p.bankcard.model.BankCard;


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
	public List<BankCard> getBankCardsByUserId(String userId);
}
