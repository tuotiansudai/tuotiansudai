package com.esoft.archer.config.service;


/**  
 * Company:     jdp2p <br/> 
 * Copyright:   Copyright (c)2013 <br/>
 * Description:  
 * @author:     wangzhi  
 * @version:    1.0
 * Create at:   2014-1-17 上午10:20:41  
 *  
 * Modification History: <br/>
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-1-17      wangzhi      1.0          
 */
public interface ConfigService {
	/**
	 * 通过配置编号得到配置的值
	 * 
	 * @param configId
	 * @return
	 */
	public String getConfigValue(String configId);
}
