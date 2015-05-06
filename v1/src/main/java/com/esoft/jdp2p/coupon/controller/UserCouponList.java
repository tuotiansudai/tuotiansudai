package com.esoft.jdp2p.coupon.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.coupon.CouponConstants;
import com.esoft.jdp2p.coupon.model.UserCoupon;
@Component
@Scope(ScopeType.VIEW)
public class UserCouponList extends EntityQuery<UserCoupon> {

	private List<UserCoupon> userInvestCoupons;

	
	@Resource
	private LoginUserInfo loginUserInfo;

	public UserCouponList() {
		final String[] RESTRICTIONS = { "userCoupon.user.id like #{userCouponList.example.user.id}" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	@Override
	protected void initExample() {
		UserCoupon uc = new UserCoupon();
		uc.setUser(new User());
		setExample(uc);
	}

	/**
	 * 通过用户id和优惠券类型获取该用户持有的优惠券
	 * 
	 * @param userId
	 * @param couponType
	 * @return
	 * @author wangzhi
	 * 
	 */
	public List<UserCoupon> getUserCouponsByUserIdAndCouponTypeAndCouponStatus(
			String userId, String couponType, String couponStatus) {
		String hql = "from UserCoupon uc where uc.user.id=? and uc.coupon.type=? and uc.status=?";
		return getHt().find(hql,
				new String[] { userId, couponType, couponStatus });
	}

	public List<UserCoupon> getUserInvestCoupons() {
		if (userInvestCoupons == null) {
			userInvestCoupons = getUserCouponsByUserIdAndCouponTypeAndCouponStatus(
					loginUserInfo.getLoginUserId(),
					CouponConstants.Type.INVEST,
					CouponConstants.UserCouponStatus.UNUSED);
		}
		return userInvestCoupons;
	}
	/**
	 * @author hch
	 * @param type
	 * @return 根据优惠劵类型找到对应的优惠劵类型名称
	 */
	public String getCouponTypeName(String type){
		return CouponConstants.UserCouponStatus.couponTypeMap.get(type);
	}
	/**
	 * @author hch
	 * @param value
	 * @return 为了获取字符串的长度，为了方便页面调用
	 */
	public int getStrLength(String value){
		if(value!=null){
			return value.length();
		}else{
			return 0;
		}
	}
	/**
	 * hch
	 * @param user_id
	 * @return 获取该用户的优惠劵总金额
	 */
	public double getUserSumMoney(String user_id){
		String hql="select sum(money) from Coupon where id in(select coupon.id from UserCoupon where user.id=?)";
		Double value=(Double) getHt().find(hql, new Object[]{user_id}).get(0);
		if(value==null){
			value=(double) 0;
		}
		return value;
	}
}
