package com.tuotiansudai.console.dto;


import com.tuotiansudai.dto.BaseDataDto;

public class AppVersionValueDto extends BaseDataDto {

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
        return "android : {Version='" + androidVersion + ", VersionCode='" + androidVersionCode + ", Url='" + androidUrl + ", ForceUpgrade='" + androidForceUpgrade + ", Message='" + androidMessage + "}" +
                "ios : {Version='" + iosVersion + ", VersionCode='" + iosVersionCode + ", Url='" + iosUrl + ", ForceUpgrade='" + iosForceUpgrade + ", Message='" + iosMessage + '}';
    }
}
