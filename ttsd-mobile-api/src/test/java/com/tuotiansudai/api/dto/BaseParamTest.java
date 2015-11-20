package com.tuotiansudai.api.dto;

public class BaseParamTest extends BaseParam {
    private static final BaseParamTest instance = new BaseParamTest();

    public static BaseParam getInstance() {
        return instance;
    }

    public static BaseParam newInstance() {
        BaseParam baseParam = new BaseParam();
        baseParam.setAppVersion("1");
        baseParam.setDeviceId("7c174e2d6856dfa9dc3ee17802b197c851744d79");
        baseParam.setDeviceModel("iPhone");
        baseParam.setOsVersion("9.0.2");
        baseParam.setPhoneNum("13800138000");
        baseParam.setPlatform("iOS");
        baseParam.setScreenH("960");
        baseParam.setScreenW("640");
        baseParam.setToken("token_of_testuser");
        baseParam.setUserId("testuser");
        baseParam.setChannel("");
        return baseParam;
    }

    private BaseParamTest() {
        super();
        super.setAppVersion("1");
        super.setDeviceId("7c174e2d6856dfa9dc3ee17802b197c851744d79");
        super.setDeviceModel("iPhone");
        super.setOsVersion("9.0.2");
        super.setPhoneNum("13800138000");
        super.setPlatform("iOS");
        super.setScreenH("960");
        super.setScreenW("640");
        super.setToken("token_of_testuser");
        super.setUserId("testuser");
        super.setChannel("");
    }

    @Override
    public void setAppVersion(String appVersion) {
    }

    @Override
    public void setDeviceId(String deviceId) {
    }

    @Override
    public void setDeviceModel(String deviceModel) {
    }

    @Override
    public void setOsVersion(String osVersion) {
    }

    @Override
    public void setPhoneNum(String phoneNum) {
    }

    @Override
    public void setPlatform(String platform) {
    }

    @Override
    public void setScreenH(String screenH) {
    }

    @Override
    public void setScreenW(String screenW) {
    }

    @Override
    public void setToken(String token) {
    }

    @Override
    public void setUserId(String userId) {
    }

    @Override
    public void setChannel(String channel) {
    }
}

