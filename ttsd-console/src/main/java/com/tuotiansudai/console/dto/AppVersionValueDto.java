package com.tuotiansudai.console.dto;


public class AppVersionValueDto {

    private String androidVersion;

    private String androidVersionCode;

    private String androidUrl;

    private String androidForceUpgrade;

    private String androidMessage;

    private String iosVersion;

    private String iosVersionCode;

    private String iosUrl;

    private String iosForceUpgrade;

    private String iosMessage;

    public AppVersionValueDto() {
    }

    public AppVersionValueDto(String androidVersion, String androidVersionCode, String androidUrl, String androidForceUpgrade, String androidMessage, String iosVersion, String iosVersionCode, String iosUrl, String iosForceUpgrade, String iosMessage) {
        this.androidVersion = androidVersion;
        this.androidVersionCode = androidVersionCode;
        this.androidUrl = androidUrl;
        this.androidForceUpgrade = androidForceUpgrade;
        this.androidMessage = androidMessage;
        this.iosVersion = iosVersion;
        this.iosVersionCode = iosVersionCode;
        this.iosUrl = iosUrl;
        this.iosForceUpgrade = iosForceUpgrade;
        this.iosMessage = iosMessage;
    }

    public int getAndroidVersion() {
        return Integer.parseInt(androidVersion.replace(".", ""));
    }

    public int getAndroidVersionCode() {
        return Integer.parseInt(androidVersionCode);
    }

    public String getAndroidUrl() {
        return androidUrl;
    }

    @Override
    public String toString() {
        return "{" +
                "androidVersion='" + androidVersion + '\'' +
                ", androidVersionCode='" + androidVersionCode + '\'' +
                ", androidUrl='" + androidUrl + '\'' +
                ", androidForceUpgrade='" + androidForceUpgrade + '\'' +
                ", androidMessage='" + androidMessage + '\'' +
                ", iosVersion='" + iosVersion + '\'' +
                ", iosVersionCode='" + iosVersionCode + '\'' +
                ", iosUrl='" + iosUrl + '\'' +
                ", iosForceUpgrade='" + iosForceUpgrade + '\'' +
                ", iosMessage='" + iosMessage + '\'' +
                '}';
    }
}
