package com.tuotiansudai.console.dto;


public class AppVersionValueDto {

    private String androidVersion;

    private String androidVersionCode;

    private String androidUrl;

    public AppVersionValueDto() {
    }

    public AppVersionValueDto(String androidVersion, String androidVersionCode, String androidUrl) {
        this.androidVersion = androidVersion;
        this.androidVersionCode = androidVersionCode;
        this.androidUrl = androidUrl;
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
}
