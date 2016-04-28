package com.esoft.jdp2p.invest;


public class InvestConstants {
	/**
	 * Package name.
	 */
	public final static String Package = "com.esoft.archer.investment";

	public final static class View {
		// public final static String INVESTMENT_LIST = "/admin/link/linkList";
	}

	/**
	 * 债权转让状态
	 * 
	 * @author Administrator
	 * 
	 */
	public final static class TransferStatus {
		/**
		 * 转让中
		 */
		public final static String TRANSFERING = "transfering";
		/**
		 * 待确认(资金托管，发送购买请求至确认之间的状态)
		 */
		public final static String WAITCONFIRM = "wait_confirm";
		/**
		 * 转让成功
		 */
		public final static String TRANSFED = "transfered";
		/**
		 * 流标
		 */
		public final static String CANCEL = "cancel";
	};

	/**
	 * 投资状态
	 * 
	 * @author Administrator
	 * 
	 */
	public final static class InvestStatus {
		/**
		 * 竞标中
		 */
//		public final static String BIDDING = "竞标中";
		/**
		 * 第三方资金托管确认中
		 */
		public final static String WAIT_AFFIRM = "wait_affirm";
		/**
		 * 竞标失败
		 */
//		public final static String BID_FAILED = "竞标失败";
		/**
		 * 投标成功
		 */
		public final static String BID_SUCCESS = "bid_success";
		/**
		 * 放款后等待第三方确认
		 */
		public final static String WAIT_LOANING_VERIFY = "wait_loaning_verify";
		/**
		 * 流标
		 */
		public final static String CANCEL = "cancel";
		/**
		 * 还款中
		 */
		public final static String REPAYING = "repaying";
		/**
		 * 债权转让
		 */
//		public final static String TRANSFER = "债权转让";
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
		/**
		 * 交易取消
		 */
		public final static String UNFINISHED = "unfinished";

		/**
		 * 内部测试
		 */
		public final static String TEST = "test";
	};

	/**
	 * 投资类型
	 * 
	 * @author Administrator
	 * 
	 */
	public final static class InvestType {
		/**
		 * 无
		 */
//		public final static String NONE = "无";
		/**
		 * 本息保障
		 */
//		public final static String PRINCIPAL_PROTECTION = "本息保障";
		// /**
		// * 本息保障
		// */
		// public final static String PRINCIPAL_INTEREST_PROTECTION = "本息保障";
	};

	/**
	 * 自动投标
	 * 
	 * @author Administrator
	 * 
	 */
	public final static class AutoInvest {
		/**
		 * 状态
		 * 
		 * @author Administrator
		 * 
		 */
		public final static class Status {
			/**
			 * 开启
			 */
			public final static String ON = "on";
			/**
			 * 关闭
			 */
			public final static String OFF = "off";
		}
	};

}
