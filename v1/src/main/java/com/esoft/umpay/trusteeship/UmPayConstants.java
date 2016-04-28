package com.esoft.umpay.trusteeship;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class UmPayConstants {

	private static Properties props;
	static {
		props = new Properties();
		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("umpay.properties"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("找不到umpay.properties文件", e);
		} catch (IOException e) {
			throw new RuntimeException("读取umpay.properties文件出错", e);
		}
	}

	public static final class Config {
		/** 默认的签名版本号 */
		public static String VERSION = props.getProperty("version");
		/**
		 * 商户ID 测试私钥为7099089 正式私钥为7091089
		 */
		public static String MER_CODE = props.getProperty("mer_id");

		public static String SIGN_TYPE = props.getProperty("sign_type");
	}

	/**
	 * 用于前端展示的资源视图
	 */
	public static final class SourceViewType{
		/** 手机页面资源视图 */
		public static final String SOURCE_V = "HTML5";
	}

	/**
	 * 同步回调地址前缀（PC端）
	 */
	public static final class ResponseWebUrl {
		public static final String PRE_RESPONSE_URL = props
				.getProperty("resopnse.webUrl");
	}

	/**
	 * 同步回调地址前缀（移动端）
	 */
	public static final class ResponseMobUrl {
		public static final String PRE_RESPONSE_URL = props
				.getProperty("resopnse.mobUrl");
	}

	/**
	 * 异步回调地址前缀
	 */
	public static final class ResponseS2SUrl {
		public static final String PRE_RESPONSE_URL = props
				.getProperty("resopnse.s2sUrl");
	}

	/**
	 * 回调地址操作类型
	 */
	public static final class ResponseUrlType {
		/** 投资 */
		public static final String PROJECT_TRANSFER_INVEST = "project_transfer_invest";
		/** 放款 */
		public static final String PROJECT_TRANSFER_GIVE_MONEY_TO_BORROWER = "project_transfer_give_money_to_borrower";
		/** 流标 */
		public static final String PROJECT_TRANSFER_FAIL_BY_MANAGER = "project_transfer_fail_by_manager";
		/** 正常还款 */
		public static final String PROJECT_TRANSFER_NORMAL_REPAY = "project_transfer_normal_repay";
		/** 提前还款 */
		public static final String PROJECT_TRANSFER_ADVANCE_REPAY = "project_transfer_advance_repay";
		/** 逾期还款 */
		public static final String PROJECT_TRANSFER_OVERDUE_REPAY = "project_transfer_overdue_repay";
	}

	/**
	 * 操作接口类型
	 */
	public static final class OperationType {
		/** 资金接口名称 */
		public static String UMPAY = "umpay";
		/** 开户 */
		public static final String MER_REGISTER_PERSON = "mer_register_person";
		/** 绑定银行卡 */
		public static final String PTP_MER_BIND_CARD = "ptp_mer_bind_card";

		public static final String PTP_MER_REPLACE_CARD = "ptp_mer_replace_card";

		public static final String MER_BIND_CARD_NOTIFY = "mer_bind_card_notify";
		/** 签约协议 */
		public static final String PTP_MER_BIND_AGREEMENT = "ptp_mer_bind_agreement";
		/** 个人账户充值 */
		public static final String MER_RECHARGE_PERSON = "mer_recharge_person";
		/** 个人账户提现 */
		public static final String CUST_WITHDRAWALS = "cust_withdrawals";
		/** 发标 */
		public static final String MER_BIND_PROJECT = "mer_bind_project";
		/** 标的更新 */
		public static final String MER_UPDATE_PROJECT = "mer_update_project";
		/** 标的转账(投资,转让等等) */
		public static final String PROJECT_TRANSFER = "project_transfer";

		public static final String TRANSFER = "transfer";

		public static final String TRANSFER_ASYN = "transfer_asyn";

		/** 用户查询 */
		public static final String USER_SEARCH = "user_search";
		/** 标的查询 */
		public static final String PROJECT_ACCOUNT_SEARC = "project_account_search";
		/** 交易查询 */
		public static final String TRANSFER_SEARCH = "transfer_search";
		/** 商户查询 */
		public static final String PTP_MER_QUERY = "ptp_mer_query";
		/** 流水查询 */
		public static final String TRANSEQ_SEARCH = "transeq_search";
	}

	/**
	 * Description :更新标的
	 * 
	 * @author zt
	 * @data 2015-3-10下午2:31:28
	 */
	public static final class UpdateProjectStatus {
		/** 01：更新标的 */
		public static final String CHANGE_TYPE_UPDATE_PRIJECT = "01";
		/** 02：标的融资人 即为借款人，借款人不一定是资金使用方（注意：仅限建标后，开标前可以修改。） */
		public static final String CHANGE_TYPE_BORROWER = "02";
		/** 03：标的代偿方 */
		public static final String CHANGE_TYPE_UPDATE_WARRANTY = "03";
		/** 04：标的资金使用方 即为标的资金使用方，目前仅支持个人 */
		public static final String CHANGE_TYPE_UPDATE_RECEIVE = "04";

		/**
		 * 对应的操作
		 */
		/** 开标 */
		public static final String PROJECT_STATE_PASSED = "0";
		/** 投标中 */
		public static final String PROJECT_STATE_RAISING = "1";
		/** 还款中 */
		public static final String PROJECT_STATE_REPAYING = "2";
		/** 已还款 */
		public static final String PROJECT_STATE_COMPLETE = "3";
		/** 结束 */
		public static final String PROJECT_STATE_FINISH = "4";

		/**
		 * 操作借款人、担保方、资金使用方时，必传
		 */
		/** 新增 */
		public static final String OPTION_TYPE_ADD = "0";
		/** 删除 */
		public static final String OPTION_TYPE_DELETE = "1";

	}

	/**
	 * 标的转账
	 */
	public static final class TransferProjectStatus {
		/** serv_type 业务类型 01:投标 */
		public static final String SERV_TYPE_INVEST = "01";
		/** serv_type 业务类型 02:债权购买 */
		public static final String SERV_TYPE_TRANSFER = "02";
		/** serv_type 业务类型 03:还款 */
		public static final String SERV_TYPE_REPAY = "03";
		/** serv_type 业务类型 04:偿付 */
		public static final String SERV_TYPE_PAYBACK = "04";
		/** serv_type 业务类型 05:贴现 */
		public static final String SERV_TYPE_DISCOUNT = "05";
		/** serv_type 业务类型 51:流标后返款 */
		public static final String SERV_TYPE_FAIL_BY_MANAGER = "51";
		/** serv_type 业务类型 52:平台收费 */
		public static final String SERV_TYPE_PLATFORM_FEE = "52";
		/** serv_type 业务类型 53:放款 */
		public static final String SERV_TYPE_GIVE_MONEY_TO_BORROWER = "53";
		/** serv_type 业务类型 54:还款后返款 */
		public static final String SERV_TYPE_REPAY_BACK = "54";
		/** serv_type 业务类型 55:偿付后返款 */
		public static final String SERV_TYPE_PAYBACK_BACK = "55";
		/** serv_type 业务类型 56:债权转让的返款 */
		public static final String SERV_TYPE_TRANSFER_BACK = "56";
		/** serv_type 业务类型 57:撤资后的返款 */
		public static final String SERV_TYPE_WITHDRAW_BACK = "57";

		/** trans_action 转账方向 01:标的转入 */
		public static final String TRANS_ACTION_IN = "01";
		/** trans_action 转账方向 02:标的转出 */
		public static final String TRANS_ACTION_OUT = "02";

		/** partic_type 转账方类型 01:投资者 */
		public static final String PARTIC_TYPE_INVESTOR = "01";
		/** partic_type 转账方类型 02:融资人 */
		public static final String PARTIC_TYPE_LOANER = "02";
		/** partic_type 转账方类型 03:P2P平台 */
		public static final String PARTIC_TYPE_P2P = "03";
		/** partic_type 转账方类型 04:担保方 */
		public static final String PARTIC_TYPE_GUARANTEE = "04";
		/** partic_type 转账方类型 05:使用方 */
		public static final String PARTIC_TYPE_USE = "05";

		/** partic_acc_type 转账方账户类型 01 个人 */
		public static final String PARTIC_ACC_TYPE_PERSON = "01";
		/** partic_acc_type 转账方账户类型 02 商户 */
		public static final String PARTIC_ACC_TYPE_MER = "02";

	}

	/**
	 * 转账(划账)
	 */
	public static final class businessTransferStatus {
		/** partic_acc_type 转账方账户类型 01 个人用户 */
		public static final String PARTIC_ACC_TYPE_PERSON = "01";
		/** partic_acc_type 转账方账户类型 02 企业用户 */
		public static final String PARTIC_ACC_TYPE_COMPANY = "02";

		/** trans_action 转账方向 01:转账方 向 P2P平台转账 */
		public static final String TRANS_ACTION_IN = "01";
		/** trans_action 转账方向 02:P2P平台转账 向  转账方*/
		public static final String TRANS_ACTION_OUT = "02";
	}

	/**
	 * 查询接口
	 */

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
