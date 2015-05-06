package com.esoft.jdp2p.risk;

public final class SystemBillConstants {

	public static class SystemOperatorInfo {
		/**
		 * 管理员划账(普通转账免密)
		 */
		public static final String ADMIN_REMIT_ACCOUNT_OPERATION = "admin_remit_account_operation";
	}

	public static class Type {
		/**
		 * 转入
		 */
		public static final String IN = "in";
		/**
		 * 转出
		 */
		public static final String OUT = "out";
		/**
		 * 转出(支付推荐奖励机制收益)
		 */
		public static final String OUT_AWARD_REMIT_ACCOUNT = "out_award_remit_account";
	}
}
