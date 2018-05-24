package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

import java.util.Map;

public class LoanCreateRequestDto extends PayBaseRequestDto {

    private String amount;

    private String loanName;

    private String loanType = "1"; // 1表示普通标的， 融资人和资金使用方相同。资金为融资人使用 3表示担保标的， 融资人无法还款的时候，有担保公司代偿还款，代偿必须传此类型，否则无法代偿成功

    public LoanCreateRequestDto(String loginName, String mobile, String userName, String accountNo, String loanName, String amount, Map<String, String> extraValues) {
        super(Source.WEB, loginName, mobile, userName, accountNo, ApiType.LOAN_CREATE, extraValues);
        this.amount = amount;
        this.loanName = loanName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
}