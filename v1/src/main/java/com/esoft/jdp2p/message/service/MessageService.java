package com.esoft.jdp2p.message.service;

import java.util.List;
import java.util.Map;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.jdp2p.message.model.UserMessage;
import com.esoft.jdp2p.message.model.UserMessageTemplate;

/**
 * Filename: MessageService.java Description: 消息service<br/>
 * Copyright: Copyright (c)2013 Company:jdp2p<br/>
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-4 下午3:36:30
 * 
 *           Modification History: Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-4 wangzhi 1.0
 */
public interface MessageService {


	/**
	 * 发送站内信
	 * 
	 * @param userId 接收用户id
	 * @param title 站内信标题
	 * @param msg 站内信内容
	 */
	public abstract void sendStationMsg(String reveiverId, String senderId, String title,
			String msg) throws UserNotFoundException;

	/**
	 * 发送手机短信
	 * 
	 * @param userId 接收用户id
	 * @param msg 短信内容
	 */
	public abstract void sendMobileMsg(String userId, String msg) throws UserNotFoundException;

	/**
	 * 发送邮件
	 * 
	 * @param userId 接收用户id
	 * @param title 邮件标题
	 * @param content 邮件内容
	 */
	public abstract void sendEmailMsg(String userId, String title, String content) throws UserNotFoundException;
	
	/**
	 * 查询用户所有启用的消息模板
	 * @param userId 用户编号
	 * @return 该用户启用的消息模板
	 */
	public List<UserMessageTemplate> getUserMessagesTemplatesByUserId(String userId);
	
	/**
	 * 保存用户消息设置
	 * @param userId 用户编号
	 * @param umts 所选定的消息模板
	 */
	public void saveUserMessages(String userId, List<UserMessageTemplate> umts);

}