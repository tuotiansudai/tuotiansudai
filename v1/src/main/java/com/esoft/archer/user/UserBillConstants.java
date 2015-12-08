package com.esoft.archer.user;

public final class UserBillConstants {
	private UserBillConstants() {
	}
	
	/**
	 * 造成资金转移的操作类型信息
	 * @author Administrator
	 *
	 */
	public final static class OperatorInfo {
		/**
		 * 投资成功
		 */
		public final static String INVEST_SUCCESS = "invest_success";
		
		/**管理员干预*/	
		public final static String ADMIN_OPERATION = "admin_operation";
		
		/**充值成功*/
		public final static String RECHARGE_SUCCESS = "recharge_success";
		
		/**申请借款*/
		public final static String APPLY_LOAN = "apply_loan";
		
		/**借款申请未通过*/
		public final static String REFUSE_APPLY_LOAN = "refuse_apply_loan";
		
		/**申请提现*/
		public final static String APPLY_WITHDRAW = "apply_withdraw";

		/**提现申请未通过*/
		public final static String REFUSE_APPLY_WITHDRAW = "refuse_apply_withdraw";

		/**正常还款*/
		public final static String NORMAL_REPAY = "normal_repay";

		public final static String INVEST_FEE = "invest_fee";
		/**活动奖励*/
		public final static String ACTIVITY_REWARD = "activity_reward";

		/**提前还款*/
		public final static String ADVANCE_REPAY = "advance_repay";
		
		/**逾期还款*/
		public final static String OVERDUE_REPAY = "overdue_repay";

		/**借款流标*/
		public final static String CANCEL_LOAN = "cancel_loan";

		/**借款撤标*/
		public final static String WITHDRAW_LOAN = "withdraw_loan";
		
		/**借款放款*/
		public final static String GIVE_MONEY_TO_BORROWER = "give_money_to_borrower";
		
		/**提现成功*/
		public final static String WITHDRAW_SUCCESS = "withdraw_success";

		/**投资流标*/
		public static final String CANCEL_INVEST = "cancel_invest";
		
		/**caijinmin 增加债权转让成功状态 201501222046 begin*/
		/**债权转让成功*/
		public static final String TRANSFER = "transfer";
		
		/**债权购买成功*/
		public static final String TRANSFER_BUY = "transfer_buy";
		/**caijinmin 增加债权转让成功状态 201501222046 end*/
		
		/**众筹投资*/
		public static final String RAISE_INVEST = "raise_invest";
		
		/**众筹放款*/
		public static final String RAISE_GIVE_MONEY_TO_BORROWER = "raise_give_money_to_borrower";
		
		/**众筹流标*/
		public final static String RAISE_CANCEL_LOAN = "raise_cancel_loan";

	}

	
	public final static class Type{
		
		/**
		 * 冻结
		 */
		public final static String FREEZE = "freeze";

		/**
		 * 解冻
		 */
		public final static String UNFREEZE = "unfreeze";
		
		/**
		 * 从余额转出 transfer out from balance
		 */
		public final static String TO_BALANCE = "to_balance";

		/**
		 * 转入到余额 tansfer into balance
		 */
		public final static String TI_BALANCE = "ti_balance";
		
		/**
		 * 从冻结金额中转出 transfer out frome frozen money
		 */
		public final static String TO_FROZEN = "to_frozen";
		
	}
	
}
