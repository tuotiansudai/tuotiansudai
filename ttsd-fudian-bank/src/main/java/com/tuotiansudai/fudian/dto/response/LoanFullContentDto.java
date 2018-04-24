package com.tuotiansudai.fudian.dto.response;

public class LoanFullContentDto extends PayBaseContentDto {

    private String loanFee;

    private String loanTxNo;

    private String loanOrderNo;

    private String loanOrderDate;

    private String status; //标的状态：0：发标成功（可以进行投资）、1：投标中（有一笔或者多笔投资）、2：满标放款成功、3：还款中（有一笔或者多笔还款）、4：还款完成 5撤标

    public String getLoanFee() {
        return loanFee;
    }

    public void setLoanFee(String loanFee) {
        this.loanFee = loanFee;
    }

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