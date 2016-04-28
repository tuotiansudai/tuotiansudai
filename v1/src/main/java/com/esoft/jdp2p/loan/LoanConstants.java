package com.esoft.jdp2p.loan;

public class LoanConstants {
	/**
	 * Package name.
	 */
	public final static String Package = "com.esoft.archer.loan";

	public final static class View {
		// public final static String LOAN_LIST = "/admin/link/linkList";
	}

	/**
	 * 申请企业借款 状态
	 * 
	 * @author Administrator
	 * 
	 */
	public final static class ApplyEnterpriseLoanStatus {
		/**
		 * 等待审核
		 */
		public final static String WAITING_VERIFY = "waiting_verify";
		/**
		 * 已审核
		 */
		public final static String VERIFIED = "verified";
	}

	/**
	 * 借款状态
	 * 
	 * @author Administrator
	 * 
	 */
	public final static class LoanStatus {
		/**
		 * 等待审核
		 */
		public final static String WAITING_VERIFY = "waiting_verify";
		/**
		 * 审核后等待第三方确认
		 */
		public final static String WAITING_VERIFY_AFFIRM = "waiting_verify_affirm";
		/**
		 * 审核后等待用户确认
		 */
		public final static String WAITING_VERIFY_AFFIRM_USER = "waiting_verify_affirm_user";
		/**
		 * 贷前公示
		 */
		public final static String DQGS = "dqgs";
		/**
		 * 审核未通过
		 */
		public final static String VERIFY_FAIL = "verify_fail";
		/**
		 * 筹款中
		 */
		public final static String RAISING = "raising";
		/**
		 * 等待复核
		 */
		public final static String RECHECK = "recheck";
		/**
		 * 放款后，等待确认
		 */
		public final static String WAITING_RECHECK_VERIFY = "waiting_recheck_verify";
		/**
		 * 流标
		 */
		public final static String CANCEL = "cancel";

		/**
		 * 流标后等待第三方确认
		 */
		public final static String WAITING_CANCEL_AFFIRM = "waiting_cancel_affirm";

		/**
		 * 还款中
		 */
		public final static String REPAYING = "repaying";
		/**
		 * 等待还款确认
		 */
		public final static String WAIT_REPAY_VERIFY = "wait_repay_verify";
		/**
		 * 逾期
		 */
		public final static String OVERDUE = "overdue";
		/**
		 * 完成
		 */
		public final static String COMPLETE = "complete";
		/**
		 * 坏账
		 */
		public final static String BAD_DEBT = "bad_debt";
	};

	/**
	 * 还款状态
	 * 
	 * @author Administrator
	 * 
	 */
	public final static class RepayStatus {
		/**
		 * 还款中
		 */
		public final static String REPAYING = "repaying";

		/** 等待还款确认 */
		public final static String WAIT_REPAY_VERIFY = "wait_repay_verify";
		/**
		 * 逾期
		 */
		public final static String OVERDUE = "overdue";
		/**
		 * 完成
		 */
		public final static String COMPLETE = "complete";
		/**
		 * 坏账
		 */
		public final static String BAD_DEBT = "bad_debt";
	};

	/**
	 * 借款审核状态
	 * 
	 * @author Administrator
	 * 
	 */
	public final static class LoanVerifyStatus {
		/**
		 * 通过
		 */
		public final static String PASSED = "通过";
		/**
		 * 未通过
		 */
		public final static String FAILED = "未通过";
	};

	/**
	 * 标的活动状态
	 * 
	 * @author guoyw
	 * 
	 */
	public final static class LoanActivityType {
		/** 普通:"pt" */
		public final static String PT = "pt";
		/** 新手:"xs" (AutoInvestServiceImpl autoInvest 判断新手标) */
		public static final String XS = "xs";
		/** 定向:"dx" */
		public static final String DX = "dx";
		/** 加息:"jx" */
		public static final String JX = "jx";
	};

	/**
	 * 标的默认投标密码
	 * 
	 * @author guoyw
	 * 
	 */
	public final static class LoanInvestPassword {
		/** 默认密码:0(表示无) */
		public final static String PSWD = "0";
	};
}
