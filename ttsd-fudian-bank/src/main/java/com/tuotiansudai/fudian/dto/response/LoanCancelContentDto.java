package com.tuotiansudai.fudian.dto.response;

public class LoanCancelContentDto extends BaseContentDto {

    private String loanTxNo;

    private String loanOrderNo;

    private String loanOrderDate;

    private String status;

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }

    public String getLoanOrderNo() {
        return loanOrderNo;
    }

    public void setLoanOrderNo(String loanOrderNo) {
        this.loanOrderNo = loanOrderNo;
    }

    public String getLoanOrderDate() {
        return loanOrderDate;
    }

    public void setLoanOrderDate(String loanOrderDate) {
        this.loanOrderDate = loanOrderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
