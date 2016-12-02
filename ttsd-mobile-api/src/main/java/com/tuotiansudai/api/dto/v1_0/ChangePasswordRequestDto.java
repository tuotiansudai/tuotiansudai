package com.tuotiansudai.api.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class ChangePasswordRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "原密码", example = "123abc")
    private String originPassword;

    @NotEmpty(message = "0012")
    @Pattern(regexp = "^(?=.*[^\\d])(.{6,20})$", message = "0012")
    @ApiModelProperty(value = "新密码", example = "abc123")
    @JsonProperty("password")
    private String newPassword;

    public String getOriginPassword() {
        return originPassword;
    }

    public void setOriginPassword(String originPassword) {
        this.originPassword = originPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
