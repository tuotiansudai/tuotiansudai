package com.tuotiansudai.paywrapper.repository.model.sync.response;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.util.AmountConverter;

import java.util.Map;

public class UserSearchResponseModel extends BaseSyncResponseModel {

    private static final Map<String, String> HUMAN_READABLE_IDENTITY_TYPE = Maps.newHashMap(ImmutableMap.<String, String>builder()
            .put("1", "身份证")
            .build());

    private static final Map<String, String> HUMAN_READABLE_ACCOUNT_STATE = Maps.newHashMap(ImmutableMap.<String, String>builder()
            .put("1", "正常")
            .put("2", "挂失")
            .put("3", "冻结")
            .put("4", "锁定")
            .put("9", "销户")
            .build());

    private static final Map<String, String> HUMAN_READABLE_AGREEMENT = Maps.newHashMap(ImmutableMap.<String, String>builder()
            .put("ZCZP0800", "无密快捷协议")
            .put("ZTBB0G00", "无密投资协议")
            .put("ZHKB0H01", "无密还款协议")
            .put("ZKJP0700", "借记卡快捷协议")
            .build());

    private String platUserId;

    private String accountId;

    private String custName;

    private String identityType;

    private String identityCode;

    private String contactMobile;

    private String mailAddr;

    private String accountState;

    private String balance;

    private String cardId;

    private String gateId;

    private String userBindAgreementList;

    @Override
    public void initializeModel(Map<String, String> resData) {
        super.initializeModel(resData);
        this.platUserId = resData.get("plat_user_id");
        this.accountId = resData.get("account_id");
        this.custName = resData.get("cust_name");
        this.identityType = resData.get("identity_type");
        this.identityCode = resData.get("identity_code");
        this.contactMobile = resData.get("contact_mobile");
        this.mailAddr = resData.get("mail_addr");
        this.accountState = resData.get("account_state");
        this.balance = resData.get("balance");
        this.cardId = resData.get("card_id");
        this.gateId = resData.get("gate_id");
        this.userBindAgreementList = resData.get("user_bind_agreement_list");
    }

    public Map<String, String> generateHumanReadableInfo() {
        return Maps.newLinkedHashMap(ImmutableMap.<String, String>builder()
                .put("资金账户托管平台用户号", Strings.isNullOrEmpty(this.platUserId) ? "" : this.platUserId)
                .put("资金账户托管平台的账户号", Strings.isNullOrEmpty(this.accountId) ? "" : this.accountId)
                .put("姓名", Strings.isNullOrEmpty(this.custName) ? "" : this.custName)
                .put("证件类型", Strings.isNullOrEmpty(HUMAN_READABLE_IDENTITY_TYPE.get(this.identityType)) ? "" : HUMAN_READABLE_IDENTITY_TYPE.get(this.identityType))
                .put("证件号", Strings.isNullOrEmpty(this.identityCode) ? "" : this.identityCode)
                .put("手机", Strings.isNullOrEmpty(this.contactMobile) ? "" : this.contactMobile)
                .put("邮箱", Strings.isNullOrEmpty(this.mailAddr) ? "" : this.mailAddr)
                .put("账户状态", Strings.isNullOrEmpty(HUMAN_READABLE_ACCOUNT_STATE.get(this.accountState)) ? "" : HUMAN_READABLE_ACCOUNT_STATE.get(this.accountState))
                .put("账户余额", AmountConverter.convertCentToString(Long.parseLong(Strings.isNullOrEmpty(this.balance) ? "0" : this.balance)))
                .put("发卡银行编号", Strings.isNullOrEmpty(this.gateId) ? "" : this.gateId)
                .put("提现银行卡", Strings.isNullOrEmpty(this.cardId) ? "" : this.cardId)
                .put("用户签约约的协议列表信息", Joiner.on(" ").join(Lists.transform(Strings.isNullOrEmpty(this.userBindAgreementList) ? Lists.<String>newArrayList() : Lists.newArrayList(this.userBindAgreementList.split("\\|")), new Function<String, String>() {
                            @Override
                            public String apply(String input) {
                                return HUMAN_READABLE_AGREEMENT.get(input);
                            }
                        }
                )))
                .build());
    }

    public Map<String, String> generateHumanReadableBalanceInfo() {
        return Maps.newLinkedHashMap(ImmutableMap.<String, String>builder()
                .put("balance", String.valueOf(Long.parseLong(Strings.isNullOrEmpty(this.balance) ? "0" : this.balance)))
                .build());
    }

}
