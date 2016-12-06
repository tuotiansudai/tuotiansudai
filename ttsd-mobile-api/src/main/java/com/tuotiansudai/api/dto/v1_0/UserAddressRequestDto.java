package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class UserAddressRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "收货人", example = "王拓天")
    private String contact;

    @ApiModelProperty(value = "手机号码", example = "15211113045")
    private String mobile;

    @ApiModelProperty(value = "地址", example = "北京市")
    private String address;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
