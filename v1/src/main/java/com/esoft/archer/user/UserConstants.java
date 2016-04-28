package com.esoft.archer.user;

import java.util.HashMap;
import java.util.Map;

public class UserConstants {

	/**
	 * Package name. com.esoft.archer.user .
	 */
	public static String Package = "com.esoft.archer.user";

	// public static final String SESSION_KEY_LOGIN_USER = "login_user";

	public static final String SESSION_REGISTER_MOBILE_VALIDATE_CODE = "mobile_validate";

	public final static class UserStatus {

		public final static String ENABLE = "1";

		public final static String DISABLE = "0";

		public final static String NOACTIVE = "2";

	}

	public final static class UserLoginLog {
		public final static String SUCCESS = "1";

		public final static String FAIL = "0";
	}

	public final static class AuthenticationManager {
		/**
		 * 登录失败次数
		 */
		public static final String LOGIN_FAIL_TIME = "login_fail_time";
		/**
		 * 是否需要验证码
		 */
		public static final String NEED_VALIDATE_CODE = "need_validate_code";

		/**
		 * 用户锁
		 */
		public static final String USER_LOCK = "user_lock";
	}

	public final static class UservalidateCodeType {
		public final static String EMAICODE_TYPE = "0";
		public final static String PHONECODE_TYPE = "1";
		// 普通手机验证码类型
		public final static String MOBILEPHONE = "M";
		// 绑定手机验证码类型
		public final static String MOBILEPHONEBINDING = "MB";
	}

	public final static class Errormsg {
		public final static String ERRORPHONE = "手机号码不正确！";
		public final static String USERVALIDATECODEERRORMSG = "验证码错误！请重新输入";
		public final static String USERMOBILERANDCODEERRORMGS = "验证码输入错误,注册失败！";
		public final static String PASSANDCASHPASSSAME = "提现密码和登录密码不能相同！";
		public final static String CANONTCHINESETESUFUHAO = "用户名不能含有中文或特殊符号！";
	}

	public final static class ClientIds {
		public final static String VALIDATECODECLIENTID = "user-register-form:inputvalidatecode";
		public final static String DYNAMICCODECLIENTID = "user-register-form2:dynamicCode";
		public final static String MOBILENUMCLIENTID = "user-register-form2:mobileNum";
		public final static String USERNAMECLIENTID = "user-register-form:username";
		public final static String EMAILCLIENTID = "user-register-form:email";
	}

	/**
	 * 提现状态
	 * 
	 * @author Administrator
	 * 
	 */
	public final static class WithdrawStatus {
		/**
		 * 等待审核
		 */
		public static final String WAIT_VERIFY = "wait_verify";
		
		/**
		 * 等待复核
		 */
		public final static String RECHECK = "recheck";
		
		/**
		 * 提现成功
		 */
		public static final String SUCCESS = "success";
		/**
		 * 审核未通过
		 */
		public static final String VERIFY_FAIL = "verify_fail";

		/**
		 * 复审未通过
		 */
		public static final String RECHECK_FAIL = "recheck_fail";
	}

	/**
	 * 充值状态
	 * 
	 * @author Administrator
	 * 
	 */
	public final static class RechargeStatus {
		/**
		 * 等待付款
		 */
		public static final String WAIT_PAY = "wait_pay";
		/**
		 * 充值成功
		 */
		public static final String SUCCESS = "success";
		/**
		 * 充值失败
		 */
		public static final String FAIL = "fail";
	}
	
	
	public static class View {
		
		public final static String POINT_VIEW_DIR = "/admin/user";

		/**
		 * 积分列表页面
		 */
		public static final String POINT_LIST = POINT_VIEW_DIR + "/userPointList";
		

		/**
		 * 积分历史列表页
		 */
		public static final String POINT_HISTORY_LIST = POINT_VIEW_DIR + "/userPointHistoryList";
	}

	/**
	 * 用户积分操作类型（增加还是扣除积分）
	 * @author 
	 *
	 */
	public final static class UserPointOperateType{
		/**
		 * 增加积分
		 */
		public static final String ADD = "add";
		/**
		 * 扣除积分
		 */
		public static final String MINUS = "minus";
	}
	
	/**
	 * 用户积分类型
	 */
	public final static class UserPointType {
		/**
		 * 升级积分
		 */
		public static final String LEVEL = "level";
		/**
		 * 消费积分
		 */
		public static final String COST = "cost";
		//hch start
		/**
		 * 根据对应的类型找到对应的名称
		 */
		public static Map<String,String> historyTypeMap;//类型map集合,key:对应值；value:对应值
		static{
			if(historyTypeMap==null||historyTypeMap.size()==0){
				historyTypeMap=new HashMap<String,String>();
				historyTypeMap.put(COST, "消费积分");
				historyTypeMap.put(LEVEL, "升级积分");
			}
		}
		//hch end
	}
}
