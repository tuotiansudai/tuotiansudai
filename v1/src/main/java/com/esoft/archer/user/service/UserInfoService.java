package com.esoft.archer.user.service;

import java.util.List;

import com.esoft.archer.user.model.User;

/**  
 * Filename:    UserInfoService.java  
 * Description:   
 * Copyright:   Copyright (c)2013 
 * Company:    jdp2p 
 * @author:     yinjunlu  
 * @version:    1.0  
 * Create at:   2014-1-4 下午12:39:01  
 *  
 * Modification History:  
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-1-4    yinjunlu             1.0        1.0 Version  
 */
public interface UserInfoService {

	/**
	 * 用户名是否存在
	 * 存在返回true 否则返回false
	 * @param username 用户名
	 * @return boolean 
	 */
	public boolean isUsernameExist(String username);
	
	/**
	 * 邮箱是否存在 
	 * 存在返回true 否则返回false
	 * @param email 邮箱
	 * @return boolean
	 */
	public boolean isEmailExist(String email);
	
	/**
	 * 手机号是否存在
	 * 存在返回true 否则返回false
	 * @param mobileNumber 手机号
	 * @return boolean
	 */
	public boolean isMobileNumberExist(String mobileNumber);
	
	/**
	 * 重置交易密码
	 * @param userId 用户id
	 */
	public void resetCashPassword(String userId);
	
	/**
	 * 修改交易密码
	 * @param userId 用户id
	 * @param newPassword 新密码
	 */
	public void modifyCashPassWord(String userId , String newPassword);
	
	/**
	 * 验证旧交易密码
	 * 正确返回true，错误返回false
	 * @param userId 用户id
	 * @param oldPassword 旧密码
	 * @return boolean
	 */
	public boolean verifyOldPassword(String userId , String oldPassword);
	
	/**
	 * 用户是否为VIP
	 * @param userId 用户id
	 * @return boolean
	 */
	public boolean isVip(String userId);
	
	/**
	 * 修改绑定邮箱
	 * @param userId 用户id
	 * @param newEmail 新邮箱
	 */
	public void modifyEmail(String userId,String newEmail);
	
	/**
	 * 查询当前用户的消息设置
	 * @param 用户id
	 * @return list
	 */
	public List getUserMessage(String uid);
	
	/**
	 * 保存用户消息设置
	 * @param 用户id
	 * @param 消息列表
	 * @return void
	 */
	public void saveMessageSet(String uid,List list);
	
	/**
	 * 是否发送消息给用户
	 * @param 用户id
	 * @param 当前节点id
	 * @return boolean
	 */
	public boolean isSendMessageToUser(String uid,String nodeId);
	
	/**
	 * 用户风险等级计算
	 * @param 用户id
	 * @return String
	 */
	public String userRiskRankCalculation(String uid);
}
