package com.esoft.jdp2p.coupon;

import java.util.HashMap;
import java.util.Map;


public class CouponConstants {
	/**
	 * 代金券类型
	 * @author Administrator
	 *
	 */
	public static class Type{
		/**
		 * 投资代金券
		 */
		public static final String INVEST = "invest";
	}
	
	/**
	 * 用户代金券状态
	 * @author Administrator
	 *
	 */
	public static class UserCouponStatus{
		/**
		 * 未使用
		 */
		public static final String UNUSED = "unused";
		/**
		 * 已使用
		 */
		public static final String USED = "used";
		/**
		 * 不可用
		 */
		public static final String DISABLE = "disable";
		
		
		//hch start
		/**
		 * 根据类型，查找对应的名称
		 */
		public static Map<String,String> couponTypeMap;//类型map集合,key:对应值；value:对应值
		static{
			if(couponTypeMap==null||couponTypeMap.size()==0){
				couponTypeMap=new HashMap<String,String>();
				couponTypeMap.put(UNUSED, "未使用");
				couponTypeMap.put(USED, "已使用");
				couponTypeMap.put(DISABLE, "不可用");
			}
		}
		//hch end
	}
	
	/**
	 * 代金券状态
	 * @author Administrator
	 *
	 */
	public static class CouponStatus{
		/**
		 * 可用
		 */
		public static final String ENABLE = "enable";
		/**
		 * 不可用
		 */
		public static final String DISABLE = "disable";
	}
	
	
}
