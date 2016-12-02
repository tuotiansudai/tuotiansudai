package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.point.repository.model.UserAddressModel;
import io.swagger.annotations.ApiModelProperty;

public class UserAddressResponseDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "收货人", example = "王拓天")
    private String contact;

    @ApiModelProperty(value = "手机号码", example = "15211113045")
    private String mobile;

    @ApiModelProperty(value = "地址", example = "北京市")
    private String address;

    public UserAddressResponseDto(UserAddressModel userAddressModel) {
        this.contact = userAddressModel.getContact();
        this.mobile = userAddressModel.getMobile();
        this.address = userAddressModel.getAddress();
    }

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
