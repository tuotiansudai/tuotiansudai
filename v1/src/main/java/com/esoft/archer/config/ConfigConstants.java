package com.esoft.archer.config;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * config constants.
 * 
 * @author zuogj
 * 
 */
public class ConfigConstants {

	/**
	 * Package name.
	 */
	public final static String Package = "com.esoft.archer.config";

	public static final class RepayAlert {
		/** 提前几天还款提醒 */
		public static final String DAYS_BEFORE = "repay_alert.days_before";
	}

	/**
	 * 关于网站信息的一些配置，诸如：网站域名，网站名称，网站口号
	 * 
	 * @author wanghm
	 * 
	 */
	public static class Website {
		/***
		 * 域名
		 */
		public static final String SITE_DNS = "site_dns";

		/**
		 * 站点标题
		 */
		public static final String SITE_NAME = "site_name";

		/**
		 * 站点口号
		 */
		public static final String SITE_SLOGAN = "site_slogan";

	}

	public static class Mail {
		/**
		 * 邮件用户名
		 */
		public static final String MAIL_USER_NAME = "mail_username";
		/**
		 * 邮件密码
		 */
		public static final String MAIL_PASSWORD = "mail_password";
		/**
		 * 发送邮件服务器
		 */
		public static final String MAIL_SMTP = "mail_smtp";
		/**
		 * 发件人称呼
		 */
		public static final String MAIL_PERSONAL = "mail_personal";
	}

	/**
	 * 关于系统账号安全的一些配置
	 * 
	 * @author wanghm
	 * 
	 */
	public final static class UserSafe {
		/**
		 * 连续登录失败的次数
		 */
		public static final String LOGIN_FAIL_MAX_TIMES = "login_fail_max_times";
		/**
		 * 用户连续输入错误密码的最大次数
		 */
		public static final String PASSWORD_FAIL_MAX_TIMES = "password_fail_max_times";
	}

	/**
	 * 关于水印
	 */
	public final static class WaterMark {
		/**
		 * 水印ID
		 */
		public final static String IF_OPEN_WATERMARK = "open_watermark";
		/**
		 * 开启水印
		 */
		public final static String OPEN_WATERMARK = "1";
		/**
		 * 关闭水印
		 */
		public final static String UN_OPEN_WATERMARK = "0";
	}

	/**
	 * 关于访问记录
	 */
	public final static class Watchdog {
		/**
		 * 访问记录ID
		 */
		public final static String IF_OPEN_WATCHDOG = "open_watchdog";
		/**
		 * 开启访问记录
		 */
		public final static String OPEN_WATCHDOG = "1";
		/**
		 * 关闭访问记录
		 */
		public final static String UN_OPEN_WATCHDOG = "0";
	}

	public static class View {
		/**
		 * 配置类型列表
		 */
		public static final String CONFIG_TYPE_LIST = "/admin/config/configTypeList";

		/**
		 * 配置属性列表
		 */
		public static final String CONFIG_LIST = "/admin/config/configList";
	}

	/**
	 * 上传路径的配置
	 * 
	 * @author Administrator
	 * 
	 */
	public final static class Upload {
		public final static String FILE_UPLOAD_PATH = "";
	}

	/**
	 * 利率协调配置
	 */
	public final static class CoordinateConfig {
		/**
		 * 助理利率协调人至少更新利率天数
		 */
		public final static String ASSITANT_COORDINATE_MAXDAYS = "assitant_coordinate_maxdays";
		/**
		 * 
		 * 利率协调人至少更新利率天数
		 */
		public final static String COORDINATE_MAXDAYS = "coordinate_maxdays";
		/**
		 * 助理利率协调人升级天数
		 */
		public final static String TO_COORDINATE_MAXDAYS = "to_coordinate_maxdays";
		/**
		 * 助理利率协调人权重
		 */
		public final static String ASSISTANT_COORDINATEIONER_WEIGHT = "assistant_coordinationer_weight";
		/**
		 * 利率协调人权重
		 */
		public final static String COORDINATEIONER_WEIGHT = "elde_coordinationer_weight";
		/**
		 * 资深利率协调人权重
		 */
		public final static String ELDE_COORDINATEIONER_WEIGHT = "elde_coordinationer_weight";

	}

	/**
	 * 自动投标
	 * 
	 * @author Administrator
	 * 
	 */
	public final static class AutoInvest {
		/**
		 * 项目发布以后，多长时间(分钟)可以开始自动投标。
		 */
		public final static String DELAY_TIME = "auto_invest_delay_time";

		/**
		 * 项目发布收到投标到多少百分比以后，可以开始自动投标。
		 */
		// public final static String START_PERCENT = "start_percent";

		/**
		 * 项目发布时间和投标百分比 的关系，是同时满足触发，还是满足其中之一即可。
		 */
		// public final static String RELATION = "relation";

		/**
		 * 项目投标到百分之多少以后，禁止自动投标
		 */
		public static final String END_PERCENT = "auto_invest_end_percent";
		/**
		 * 是否开启自动投标功能(1:开启，0:关闭)
		 */
		public static final String IS_OPEN = "auto_invest_is_open";

	}

	/**
	 * 初始化调度
	 * 
	 * @author wangzhi
	 * 
	 */
	public final static class Schedule {
		/**
		 * 自动还款，是否开启
		 */
		public final static String ENABLE_AUTO_REPAYMENT = "schedule.enable_auto_repayment";
		/**
		 * 第三方资金托管，主动查询，是否开启
		 */
		public static final String ENABLE_REFRESH_TRUSTEESHIP = "schedule.enable_refresh_trusteeship";
		/**
		 * 还款提醒
		 */
		public static final String ENABLE_REPAY_ALERT = "schedule.enable_repay_alert";
	}

	/**
	 * 关于提现费用方式的配置
	 */
	public final static class WithDraw {
		/**
		 * 提现按照阶段收取手续费
		 */
		public final static String WITHDRAW_STAGE_FEE = "1";
	}

	/**
	 * 债权转让配置
	 * 
	 * @author wangxiao
	 * 
	 */
	public final static class InvestTransfer {
		/**
		 * 投资人持有该债权的最小时间(月),默认是1个月
		 */
		public static final String PAID_REPAY_COUNT_MIN = "invest_transfer.paid_repay_count_min";
		/**
		 * 要转让的债权剩余期数大于或等于几期可转让,默认是3个月
		 */
		public static final String REMAIN_REPAY_COUNT_MIN = "invest_transfer.remain_repay_count_min";
		/**
		 * 剩余本金大于或等于多少钱可以转让，默认1000
		 */
		public static final String REMAIN_CORPUS_MIN = "invest_transfer.remain_corpus_min";
		/**
		 * 债权转让申请有效期（天），默认是7天
		 */
		public static final String DEAD_LINE = "invest_transfer.dead_line";
		/**
		 * 还款日前几天，才能申请债权转让，默认7
		 */
		public static final String APPLY_BEFORE_REPAY_DAY = "invest_transfer.apply_before_repay_day";
		/**
		 * 债权转让金额是否允许大于债权本身金额(1:允许，0:不允许，空：不允许)
		 */
		public static final String CAN_GREATER_THAN_SELF_WORTH = "invest_transfer.can_greater_than_self_worth";
		/**
		 * 债权转让金额是否允许小于本身金额(1:允许，0:不可以，空：不允许)
		 */
		public static final String CAN_LESS_THAN_SELF_WORTH = "invest_transfer.can_less_than_self_worth";
		/**
		 * 债权转让金额是否允许等于债权本身金额(1:允许，0:不允许，空：允许)
		 */
		public static final String CAN_EQUAL_SELF_WORTH = "invest_transfer.can_equal_self_worth";

	}

	/**
	 * 积分节点的配置
	 */
	public final static class UserPointMonitor {
		/**
		 * 投资送积分比例(获得积分=投资额*比例)
		 */
		public final static String INVEST = "user_point_monitor.invest";
	}

}