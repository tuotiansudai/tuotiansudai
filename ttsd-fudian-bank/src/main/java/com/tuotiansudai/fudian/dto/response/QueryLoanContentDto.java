package com.tuotiansudai.fudian.dto.response;

public class QueryLoanContentDto extends BaseContentDto {

    private String amount;

    private String balance;

    private String loanTxNo;

    private String status; // 标的状态：0：发标成功（可以进行投资）、1：投标中（有一笔或者多笔投资）、2：满标放款成功、3：还款中（有一笔或者多笔还款）、4：还款完成 5撤标

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
