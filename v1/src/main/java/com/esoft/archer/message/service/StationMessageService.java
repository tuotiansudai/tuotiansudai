package com.esoft.archer.message.service;
/**  
 * Filename:    StationMessage.java  
 * Description: 站内信模块接口  
 * Copyright:   Copyright (c)2013 
 * Company:    jdp2p 
 * @author:     bizhibo  
 * @version:    1.0  
 * Create at:   2014-1-4 上午10:55:02  
 *  
 * Modification History:  
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-1-4    bizhibo             1.0        1.0 Version  
 */
public interface StationMessageService {

	/**
	 * 删除一条站内消息
	 * @param messageId 消息ID
	 */
	public void deleteStationMsg(String messageId);
	
	
	/**
	 * 将站内信息标记为已读
	 * @param messageId 消息ID
	 */
	public void markRead(String messageId);
}
