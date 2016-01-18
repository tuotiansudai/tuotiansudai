package com.tuotiansudai.paywrapper.repository.model.sync.response;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.util.AmountConverter;

import java.util.Map;

public class ProjectAccountSearchResponseModel extends BaseSyncResponseModel {

    private static final Map<String, String> HUMAN_READABLE_PROJECT_ACCOUNT_STATE = Maps.newHashMap(ImmutableMap.<String, String>builder()
            .put("01", "正常")
            .put("02", "冻结")
            .build());

    private static final Map<String, String> HUMAN_READABLE_PROJECT_STATE = Maps.newHashMap(ImmutableMap.<String, String>builder()
            .put("-1", "取消")
            .put("90", "初始")
            .put("91", "建标中")
            .put("92", "建标成功")
            .put("93", "建标失败")
            .put("94", "建标锁定")
            .put("0", "开标")
            .put("1", "投资中")
            .put("2", "还款中")
            .put("3", "已还款")
            .put("4", "结束")
            .build());

    private String projectId;

    private String projectAccountId;

    private String projectAccountState;

    private String projectState;

    private String balance;

    @Override
    public void initializeModel(Map<String, String> resData) {
        super.initializeModel(resData);
        this.projectId = resData.get("project_id");
        this.projectAccountId = resData.get("project_account_id");
        this.projectAccountState = resData.get("project_account_state");
        this.projectState = resData.get("project_state");
        this.balance = resData.get("balance");
    }

    public String getBalance() {
        return balance;
    }

    public Map<String, String> generateHumanReadableInfo() {
        return Maps.newLinkedHashMap(ImmutableMap.<String, String>builder()
                .put("标的号", Strings.isNullOrEmpty(this.projectId) ? "" : this.projectId)
                .put("标的账户号", Strings.isNullOrEmpty(this.projectAccountId) ? "" : this.projectAccountId)
                .put("标的账户状态", Strings.isNullOrEmpty(HUMAN_READABLE_PROJECT_ACCOUNT_STATE.get(this.projectAccountState)) ? "" : HUMAN_READABLE_PROJECT_ACCOUNT_STATE.get(this.projectAccountState))
                .put("标的状态", Strings.isNullOrEmpty(HUMAN_READABLE_PROJECT_STATE.get(this.projectState)) ? "" : HUMAN_READABLE_PROJECT_STATE.get(this.projectState))
                .put("标的余额", AmountConverter.convertCentToString(Long.parseLong(Strings.isNullOrEmpty(this.balance) ? "0" : this.balance)))
                .build());
    }
}
