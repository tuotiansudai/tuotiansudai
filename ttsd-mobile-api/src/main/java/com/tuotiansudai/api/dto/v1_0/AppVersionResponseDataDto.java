package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class AppVersionResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "版本", example = "3.5")
    private String version;

    @ApiModelProperty(value = "版本号", example = "200")
    private Integer versionCode;

    @ApiModelProperty(value = "信息", example = "1，拓天助手修改为个人中心")
    private String message;

    @ApiModelProperty(value = "强制升级", example = "true")
    private boolean forceUpgrade;

    @ApiModelProperty(value = "链接地址", example = "https://tuotiansudai.com/app/tuotiansudai.apk")
    private String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(boolean forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
