package com.tuotiansudai.api.dto.v1_0;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;

public class BaseParam {
    private String userId;
    @ApiModelProperty(value = "手机号码", example = "13800138000")
    private String phoneNum;
    @ApiModelProperty(value = "身份认证票据", example = "66666666-6666-6666-6666-666666666666")
    private String token;
    @ApiModelProperty(value = "客户端类型", example = "Android")
    private String platform;
    @ApiModelProperty(value = "APP版本", example = "3.0")
    private String appVersion;
    @ApiModelProperty(value = "操作系统版本", example = "7.0")
    private String osVersion;
    @ApiModelProperty(value = "客户端设备ID", example = "FFFFFFF-FFFFFFFFFF-FFFFFFFFFFFFF")
    private String deviceId;
    @ApiModelProperty(value = "客户端设备型号", example = "XiaoMi")
    private String deviceModel;
    @ApiModelProperty(value = "客户端屏幕宽度", example = "768")
    private String screenW;
    @ApiModelProperty(value = "客户端屏幕高度", example = "1024")
    private String screenH;
    @ApiModelProperty(value = "渠道", example = "xiaomi")
    private String channel;

    public String getUserId() {
        return Strings.isNullOrEmpty(userId) ? "" : userId ;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNum() {
        return Strings.isNullOrEmpty(phoneNum) ? "" : phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getToken() {
        return Strings.isNullOrEmpty(token) ? "" : token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPlatform() {
        return Strings.isNullOrEmpty(platform) ? "" : platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAppVersion() {
        return Strings.isNullOrEmpty(appVersion) ? "" : appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getOsVersion() {
        return Strings.isNullOrEmpty(osVersion) ? "" : osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getDeviceId() {
        return Strings.isNullOrEmpty(deviceId) ? "" : deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceModel() {
        return Strings.isNullOrEmpty(deviceModel) ? "" : deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getScreenW() {
        return Strings.isNullOrEmpty(screenW) ? "" : screenW;
    }

    public void setScreenW(String screenW) {
        this.screenW = screenW;
    }

    public String getScreenH() {
        return Strings.isNullOrEmpty(screenH) ? "" : screenH;
    }

    public void setScreenH(String screenH) {
        this.screenH = screenH;
    }

    public String getChannel() {
        return Strings.isNullOrEmpty(channel) ? "" : channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "BaseParam{" +
                "userId='" + userId + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", token='" + token + '\'' +
                ", platform='" + platform + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", screenW='" + screenW + '\'' +
                ", screenH='" + screenH + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
