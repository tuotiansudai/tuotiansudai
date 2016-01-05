package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class InvestDto implements Serializable {

    private String loginName;

    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private String loanId;

    @NotEmpty
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String amount;

    @Pattern(regexp = "^\\d*$")
    private String userCouponId;

    private String channel;

    private Source source = Source.WEB;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(String userCouponId) {
        this.userCouponId = userCouponId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
