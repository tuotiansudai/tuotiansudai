package com.tuotiansudai.dto;


import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class AccountDto implements Serializable{

    private

    @NotEmpty
    private String loginName;

    @NotEmpty
    @Pattern(regexp = "^[1-9]\\d{13,16}[a-zA-Z0-9]$")
    private String identityNumber;
}
