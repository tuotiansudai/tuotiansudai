package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class PersonalInfoResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "用户名", example = "wangtuotian")
    private String userId;

    @ApiModelProperty(value = "登录名", example = "19910002213")
    private String userName;

    @ApiModelProperty(value = "手机号码", example = "19910002213")
    private String phoneNum;

    @ApiModelProperty(value = "实名认证状态", example = "true")
    private boolean certificationFlag;

    @ApiModelProperty(value = "真实姓名", example = "王拓天")
    private String realName;

    @ApiModelProperty(value = "身份证", example = "37XXXXXXXXXXXXXXXX")
    private String idCard;

    @ApiModelProperty(value = "绑卡状态", example = "true")
    private boolean isBoundBankCard;

    @ApiModelProperty(value = "快捷支付状态", example = "true")
    private boolean isFastPayment;

    @ApiModelProperty(value = "银行卡号", example = "6222****1134")
    private String bankCardNo;

    @ApiModelProperty(value = "银行", example = "ICBC")
    private String bankId;

    @ApiModelProperty(value = "支持快捷支付状态", example = "true")
    private boolean fastPaymentEnable;

    @ApiModelProperty(value = "电子邮箱", example = "eeee@sina.com")
    private String email;

    @ApiModelProperty(value = "省", example = "51")
    private String districtCode;

    @ApiModelProperty(value = "自动投标", example = "true")
    private boolean autoInvest;

    @ApiModelProperty(value = "银行名称", example = "中国工商银行")
    private String bankName;

    @ApiModelProperty(value = "安心签状态", example = "true")
    private boolean anxinUser;

    @ApiModelProperty(value = "免验", example = "true")
    private boolean skipAuth;

    @ApiModelProperty(value = "product_code", example = "83aa9818615d46c2a2bbceb3ce1bd6ef")
    private boolean hasAuthed;

    @ApiModelProperty(value = "是否可投新手标", example = "true")
    private boolean isNewbieEnable;

    @ApiModelProperty(value = "是否可投新手体验标", example = "true")
    private boolean isExperienceEnable;

    @ApiModelProperty(value = "投资偏好", example = "进取型")
    private String riskEstimate;

    @ApiModelProperty(value = "投资偏好说明", example = "进取型")
    private String riskEstimateDesc;

    @ApiModelProperty(value = "风险评估等级")
    private Integer estimateLevel;

    @ApiModelProperty(value = "风险评估等级限额")
    private long estimateLimit;


    public boolean getCertificationFlag() {
        return certificationFlag;
    }

    public void setCertificationFlag(boolean certificationFlag) {
        this.certificationFlag = certificationFlag;
    }

    public boolean getBoundBankCard() {
        return isBoundBankCard;
    }

    public void setIsBoundBankCard(boolean isBoundBankCard) {
        this.isBoundBankCard = isBoundBankCard;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public boolean getFastPayment() {
        return isFastPayment;
    }

    public void setIsFastPayment(boolean isFastPayment) {
        this.isFastPayment = isFastPayment;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public boolean getFastPaymentEnable() {
        return fastPaymentEnable;
    }

    public void setFastPaymentEnable(boolean fastPaymentEnable) {
        this.fastPaymentEnable = fastPaymentEnable;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public boolean isAutoInvest() {
        return autoInvest;
    }

    public void setAutoInvest(boolean autoInvest) {
        this.autoInvest = autoInvest;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public boolean isExperienceEnable() {
        return isExperienceEnable;
    }

    public void setIsExperienceEnable(boolean isExperienceEnable) {
        this.isExperienceEnable = isExperienceEnable;
    }

    public boolean isNewbieEnable() {
        return isNewbieEnable;
    }

    public void setIsNewbieEnable(boolean isNewbieEnable) {
        this.isNewbieEnable = isNewbieEnable;
    }

    public boolean isAnxinUser() {
        return anxinUser;
    }

    public void setAnxinUser(boolean anxinUser) {
        this.anxinUser = anxinUser;
    }

    public boolean isSkipAuth() {
        return skipAuth;
    }

    public void setSkipAuth(boolean skipAuth) {
        this.skipAuth = skipAuth;
    }

    public boolean isHasAuthed() {
        return hasAuthed;
    }

    public void setHasAuthed(boolean hasAuthed) {
        this.hasAuthed = hasAuthed;
    }

    public String getRiskEstimate() {
        return riskEstimate;
    }

    public void setRiskEstimate(String riskEstimate) {
        this.riskEstimate = riskEstimate;
    }

    public String getRiskEstimateDesc() {
        return riskEstimateDesc;
    }

    public void setRiskEstimateDesc(String riskEstimateDesc) {
        this.riskEstimateDesc = riskEstimateDesc;
    }

    public Integer getEstimateLevel() {
        return estimateLevel;
    }

    public void setEstimateLevel(Integer estimateLevel) {
        this.estimateLevel = estimateLevel;
    }

    public long getEstimateLimit() {
        return estimateLimit;
    }

    public void setEstimateLimit(long estimateLimit) {
        this.estimateLimit = estimateLimit;
    }
}
