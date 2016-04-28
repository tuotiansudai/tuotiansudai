package com.esoft.jdp2p.community.service;

/**
 * Filename: SiteFriendsManage.java Description: 站内好友管理接口 Copyright: Copyright
 * (c)2013 Company: jdp2p
 * 
 * @author: bizhibo
 * @version: 1.0 Create at: 2014-1-4 上午11:08:49
 * 
 *           Modification History: Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-4 bizhibo 1.0 1.0 Version
 */
public interface SiteFriendsService {

	/**
	 * 申请加其他用户为好友
	 * 
	 * @param curUserId
	 *            当前用户id
	 * @param friendUserId
	 *            要加好友的用户Id
	 * @param remarks
	 *            好友请求备注信息
	 */

	public void applyForFriend(String curUserId, String friendUserId,
			String remarks);

	/**
	 * 同意好友请求
	 * 
	 * @param curUserId
	 *            当前用户id
	 * @param friendUserId
	 *            好友id
	 */
	public void agreeFriendApply(String curUserId, String friendUserId);

	/**
	 * 拒绝好友请求
	 * 
	 * @param curUserId
	 *            当前用户id
	 * @param friendUserId
	 *            好友id
	 * @param remarks
	 *            拒绝请求的备注信息
	 */
	public void refuseFriendApply(String curUserId, String friendUserId,
			String remarks);

	/**
	 * 判断用户是否在黑名单里
	 * 
	 * @param curUserId
	 *            当前用户id
	 * @param friendUserId
	 *            好友用户id
	 * @return 布尔值 true为属于黑名单 false为不属于黑名单
	 */
	public boolean isUserInBlacklist(String curUserId, String friendUserId);

	/**
	 * 从黑名单里删除用户
	 * 
	 * @param curUserId
	 *            当前用户id
	 * @param friendUserId
	 *            好友用户id
	 */
	public void deleteUserFromBlacklist(String curUserId, String friendUserId);

	/**
	 * 把某个用户加入到黑名单
	 * 
	 * @param curUserId
	 *            当前用户id
	 * @param friendUserId
	 *            要加入到黑名单的用户id
	 */
	public void addUserToBlacklist(String curUserId, String friendUserId);
}
