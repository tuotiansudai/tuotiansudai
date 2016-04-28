package com.esoft.jdp2p.coupon.service;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 优惠券servcie
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-7-16 下午2:27:23
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-7-16 wangzhi 1.0
 */
public interface UserCouponService {
	/**
	 * 给用户发放优惠券
	 * 
	 * @param userId
	 *            用户id
	 * @param couponId
	 *            优惠券id
	 *            @param periodOfValidity 有效期（秒），如果为空，则为优惠券本身有效期
	 */
	public void giveUserCoupon(String userId, String couponId, Integer periodOfValidity, String description);

	/**
	 * 使用优惠券
	 * 
	 * @param userCouponId
	 *            用户优惠券id
	 */
	public void userUserCoupon(String userCouponId);

	/**
	 * 优惠券失效
	 * 
	 * @param userCouponId
	 *            用户优惠券id
	 */
	public void disable(String userCouponId);
	
	/**
	 * 优惠券过期
	 * @param userCouponId 用户优惠券id
	 */
	public void exceedTimeLimit(String userCouponId);

}
