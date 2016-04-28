package com.esoft.jdp2p.invest.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.jdp2p.invest.model.AutoInvest;

/**  
 * Company:     jdp2p <br/> 
 * Copyright:   Copyright (c)2013 <br/>
 * Description:  
 * @author:     wangzhi  
 * @version:    1.0
 * Create at:   2014-3-8 下午4:19:42  
 *  
 * Modification History: <br/>
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-3-8      wangzhi      1.0          
 */
public interface AutoInvestService {

	/**
	 * 用户设置自动投标
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public abstract void settingAutoInvest(AutoInvest ai);

	/**
	 * 自动投标
	 * @param loanId
	 */
	@Async
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public abstract void autoInvest(String loanId);

}