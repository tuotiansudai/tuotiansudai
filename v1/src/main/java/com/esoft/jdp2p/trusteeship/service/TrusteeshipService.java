package com.esoft.jdp2p.trusteeship.service;


/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 资金托管service
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-3-31 下午3:49:10
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-3-31 wangzhi 1.0
 */
public interface TrusteeshipService {
	/**
	 * 主动查询发往第三方资金托管的请求的状态，并根据结果做相应处理。
	 */
	public void handleSendedOperations();
}
