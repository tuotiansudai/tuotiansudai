package com.esoft.jdp2p.risk;

public class FeeConfigConstants {

	/**
	 * 费用节点
	 */
	public static class FeePoint {
		/** 发起借款 */
		public static final String APPLY_LOAN = "apply_loan";
		/** 提现 */
		public static final String WITHDRAW = "withdraw";
		/** 充值 */
		public static final String RECHARGE = "recharge";
		/** 债权转让 */
		public static final String TRANSFER = "transfer";
		/** 提前还款-给投资人 */
		public static final String ADVANCE_REPAY_INVESTOR = "advance_repay_investor";
		/** 提前还款-给系统 */
		public static final String ADVANCE_REPAY_SYSTEM = "advance_repay_system";
		/** 逾期还款-给投资人 */
		public static final String OVERDUE_REPAY_INVESTOR = "overdue_repay_investor";
		/** 逾期还款-给系统 */
		public static final String OVERDUE_REPAY_SYSTEM = "overdue_repay_system";
	}

	/**
	 * 费用类型
	 */
	public static class FeeType {
		/** 保证金 */
		public static final String CASH_DEPOSIT = "cash_deposit";
		/** 手续费 */
		public static final String FACTORAGE = "factorage";
		/** 罚金 */
		public static final String PENALTY = "penalty";
	}

	/** 费类型（固定值或者费率） */
	public static class OperateMode {
		/** 率 */
		public static final String RATE = "rate";
		/** 固定值 */
		public static final String FIXED = "fixed";
	}

}
