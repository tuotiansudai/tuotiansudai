package com.tuotiansudai.paywrapper.repository.model.sync.response;

import java.util.Map;

/**
 * Created by tuotian on 15/8/17.
 */
public class MerBindProjectResponseModel extends BaseSyncResponseModel{
    private String signType;
    private String sign;
    private String merchantId;
    private String version;
    private String projectAccountId;
    private String projectState;
    private String merCheckDate;

    public void initializeModel(Map<String, String> resData) {
        this.setResponseData(resData.toString());
        this.setRetCode(resData.get("ret_code"));
        this.setRetMsg(resData.get("ret_msg"));
        this.setSignType(resData.get("sign_type"));
        this.setSign(resData.get("sign"));
        this.setMerchantId(resData.get("mer_id"));
        this.setVersion(resData.get("version"));
        this.setProjectAccountId(resData.get("project_account_id"));
        this.setProjectState(resData.get("project_state"));
        this.setMerCheckDate(resData.get("mer_check_date"));
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProjectAccountId() {
        return projectAccountId;
    }

    public void setProjectAccountId(String projectAccountId) {
        this.projectAccountId = projectAccountId;
    }

    public String getProjectState() {
        return projectState;
    }

    public void setProjectState(String projectState) {
        this.projectState = projectState;
    }

    public String getMerCheckDate() {
        return merCheckDate;
    }

    public void setMerCheckDate(String merCheckDate) {
        this.merCheckDate = merCheckDate;
    }
}
