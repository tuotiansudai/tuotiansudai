package com.esoft.jdp2p.repay;

public class RepayConstants {

	/**
	 * 开始计息时间节点
	 * 
	 * @author Administrator
	 * 
	 */
	public static class InterestPoint {

		/**
		 * 即投即生息
		 */
		public static final String INTEREST_BEGIN_ON_INVEST = "interest_begin_on_invest";

		/**
		 * 放款后生息
		 */
		public static final String INTEREST_BEGIN_ON_LOAN = "interest_begin_on_loan";
	}

	/**
	 * 还款方式单元（天s、月s等等）
	 * 
	 * @author Administrator
	 * 
	 */
	public static class RepayUnit {

		/**
		 * 天（以一天或者几天为单位还款）
		 */
		public static final String DAY = "day";

		/**
		 * 月（以一月或者几个月为单位还款）
		 */
		public static final String MONTH = "month";
	}

	/**
	 * 还款类型
	 * 
	 * @author Administrator
	 * 
	 */
	public static class RepayType {

		/**
		 * 等额本金constant amortization mortgage(CAM)
		 */
		public final static String CAM = "cam";

		/**
		 * 等额本息constant payment mortgage（CPM）
		 */
		public final static String CPM = "cpm";

		/**
		 * 按月付息到期还本金 先息后本
		 */
		public final static String RFCL = "rfcl";

		/**
		 * 到期还本付息 repay the loan and the accrued interest outright(RLIO)
		 */
		public final static String RLIO = "rlio";
	}

	/**
	 * 计息方式（按天计息、按月计息等等）
	 * 
	 * @author Administrator
	 * 
	 */
	public static class InterestType {

		/**
		 * 按天计息
		 */
		public static final String DAY = "day";

		/**
		 * 按月计息
		 */
		public static final String MONTH = "month";
	}

}
