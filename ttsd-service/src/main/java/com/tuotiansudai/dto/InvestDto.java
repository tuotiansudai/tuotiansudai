package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

public class InvestDto implements Serializable {

    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private String loanId;

    private String transferInvestId;

    private String loginName;

    @NotEmpty
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String amount;

    private List<Long> userCouponIds;

    private String channel;

    private Source source = Source.WEB;

    private boolean noPassword;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getTransferInvestId() {
        return transferInvestId;
    }

    public void setTransferInvestId(String transferInvestId) {
        this.transferInvestId = transferInvestId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<Long> getUserCouponIds() {
        return userCouponIds;
    }

    public void setUserCouponIds(List<Long> userCouponIds) {
        this.userCouponIds = userCouponIds;
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

    public boolean isNoPassword() {
        return noPassword;
    }

    public void setNoPassword(boolean noPassword) {
        this.noPassword = noPassword;
    }
}
