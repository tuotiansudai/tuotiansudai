package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.dto.BankLoanCreateDto;
import com.tuotiansudai.fudian.util.AmountUtils;

public class LoanCreateRequestDto extends BaseRequestDto {

    private String loanName;

    private String amount;

    private String loanType = "1"; // 1表示普通标的， 融资人和资金使用方相同。资金为融资人使用 3表示担保标的， 融资人无法还款的时候，有担保公司代偿还款，代偿必须传此类型，否则无法代偿成功

    private String endTime;

    public LoanCreateRequestDto(BankLoanCreateDto bankLoanCreateDto) {
        super(Source.WEB, bankLoanCreateDto.getLoginName(), bankLoanCreateDto.getMobile(), bankLoanCreateDto.getBankUserName(), bankLoanCreateDto.getBankAccountNo());
        this.loanName = bankLoanCreateDto.getLoanName();
        this.amount = AmountUtils.toYuan(bankLoanCreateDto.getAmount());
        this.endTime = bankLoanCreateDto.getEndTime();
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

    public String getEndTime() {
        return endTime;
    }
}