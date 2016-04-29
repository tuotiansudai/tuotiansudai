package com.esoft.archer.message;

public class MessageConstants {
	
	public final static class Status{
		public final static String ON = "开启";
		public final static String OFF = "关闭";
	}

	public final static class MessageGroup {

		public final static String EMAIL = "email";

		public final static String PRIVATE_MESSAGE = "private_message";

		public final static String MOBILE = "mobile";

	}

	public final static class MessageType {
		// 投资功能开通
		public final static String INVESTED_OPNE = "invest_open";
		// 充值成功
		public final static String RECHARGE_SUCCESS = "recharge_success";
		// 成功投资一笔
		public final static String INVESTED__SUCCESS = "invest_success";

		// 必须
		public final static String REQUIRED = "Y";
		// 可选
		public final static String NOREQUIRED = "N";
	}

	public final static class InBoxConstants {

		public final static String NOREAD = "0";//未读

		public final static String ISREAD = "1";//已读
	}

	public final static class FriendsConstants {
		
		public final static String F = "f";// 好友
		
		public final static String H = "H";// 黑名单
	}

	public final static class ApplyFriendConstants {
		
		public final static String NOAGREE = "0";// 未同意

		public final static String AGREE = "1";// 同意

		public final static String REFUSE = "2";// 拒绝
	}
}
