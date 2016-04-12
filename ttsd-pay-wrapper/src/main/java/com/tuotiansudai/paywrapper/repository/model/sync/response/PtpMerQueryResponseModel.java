package com.tuotiansudai.paywrapper.repository.model.sync.response;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.util.AmountConverter;

import java.util.Map;

public class PtpMerQueryResponseModel extends BaseSyncResponseModel {

    private static final Map<String, String> HUMAN_READABLE_ACCOUNT_TYPE = Maps.newHashMap(ImmutableMap.<String, String>builder()
            .put("01", "现金账户")
            .build());

    private static final Map<String, String> HUMAN_READABLE_ACCOUNT_STATE = Maps.newHashMap(ImmutableMap.<String, String>builder()
            .put("1", "正常")
            .put("2", "挂失")
            .put("3", "冻结")
            .put("4", "锁定")
            .put("9", "销户")
            .build());


    private String queryMerId;

    private String balance;

    private String accountType;

    private String accountState;

    @Override
    public void initializeModel(Map<String, String> resData) {
        super.initializeModel(resData);
        this.queryMerId = resData.get("query_mer_id");
        this.balance = resData.get("balance");
        this.accountType = resData.get("account_type");
        this.accountState = resData.get("account_state");
    }

    public Map<String, String> generateHumanReadableInfo() {
        return Maps.newLinkedHashMap(ImmutableMap.<String, String>builder()
                .put("商户号", Strings.isNullOrEmpty(this.queryMerId) ? "" : this.queryMerId)
                .put("账户余额", AmountConverter.convertCentToString(Long.parseLong(Strings.isNullOrEmpty(this.balance) ? "0" : this.balance)))
                .put("账户类型", Strings.isNullOrEmpty(this.accountType) ? "" : HUMAN_READABLE_ACCOUNT_TYPE.get(this.accountType))
                .put("账户状态", Strings.isNullOrEmpty(HUMAN_READABLE_ACCOUNT_STATE.get(this.accountState)) ? "" : HUMAN_READABLE_ACCOUNT_STATE.get(this.accountState))
                .build());
    }
}
