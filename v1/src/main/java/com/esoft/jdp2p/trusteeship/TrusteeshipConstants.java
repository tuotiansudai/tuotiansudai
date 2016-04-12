package com.esoft.jdp2p.trusteeship;

/**
 * 资金托管 constants.
 * 
 */
public class TrusteeshipConstants {

	public static final String Package = "com.esoft.jdp2p.trusteeship";

	public static final class Status {
		/**
		 * 等待发送
		 */
		public static final String UN_SEND = "un_send";
		/**
		 * 已发送
		 */
		public static final String SENDED = "sended";
		/**
		 * 通过
		 */
		public static final String PASSED = "passed";
		/**
		 * 未通过
		 */
		public static final String REFUSED = "refused";
		/**
		 * 无响应
		 */
		public static final String NO_RESPONSE = "no_response";
		/**
		 * 主动流标
		 */
		public static final String CANCEL = "cancel";
	}

	public static final class OperationType {
		/**
		 * 开户
		 */
		public static final String CREATE_ACCOUNT = "create_account";

		/**
		 * 充值
		 */
		public static final String RECHARGE = "recharge";

		/**
		 * 投标
		 */
		public static final String INVEST = "invest";
		
		/**
		 * 流标
		 */
		public static final String CANCEL_LOAN = "cancel_loan";
		
		/**
		 * 众筹流标
		 */
		public static final String RAISE_CANCEL_LOAN = "raise_cancel_loan";
		/**
		 * 放款
		 */
		public static final String GIVE_MOENY_TO_BORROWER = "give_moeny_to_borrower";

		/**
		 * 还款
		 */
		public static final String REPAY = "repay";
		
		/**
		 * 提前还款
		 */
		public static final String ADVANCE_REPAY = "advance_repay";
		
		/**
		 * 逾期还款
		 */
		public static final String OVERDUE_REPAY = "overdue_repay";

		/**
		 * 提现
		 */
		public static final String WITHDRAW_CASH = "withdraw_cash";
		
		/**
		 * 自动投标授权
		 */
		public static final String AUTO_INVEST_SIGN = "auto_invest_sign";
		
		/**
		 * 自动投标
		 */
		public static final String AUTO_INVEST = "auto_invest";
		
		/**
		 * 债权转让
		 */
		public static final String TRANSFER = "transfer";
		
		
	}

}
