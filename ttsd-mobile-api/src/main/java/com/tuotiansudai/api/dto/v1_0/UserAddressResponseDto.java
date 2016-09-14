package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.point.repository.model.UserAddressModel;

public class UserAddressResponseDto extends BaseResponseDataDto {

    private String contact;
    private String mobile;
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
