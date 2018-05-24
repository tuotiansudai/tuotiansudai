package com.tuotiansudai.fudian.dto.response;

public class CancelCardBindContentDto extends UserBaseContentDto {

    private String bank;

    private String bankAccountNo;

    private String bankCode;

    private String status; //1-解绑失败， 4-解绑成功

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return "4".equalsIgnoreCase(status);
    }
}
