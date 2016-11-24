package com.tuotiansudai.dto;

public class LoanMessageRequestDto {
    private String loanMessageTitle;
    private String loanMessageContent;

    public LoanMessageRequestDto() {
    }

    public LoanMessageRequestDto(String loanMessageTitle, String loanMessageContent) {
        this.loanMessageTitle = loanMessageTitle;
        this.loanMessageContent = loanMessageContent;
    }

    public String getLoanMessageTitle() {
        return loanMessageTitle;
    }

    public void setLoanMessageTitle(String loanMessageTitle) {
        this.loanMessageTitle = loanMessageTitle;
    }

    public String getLoanMessageContent() {
        return loanMessageContent;
    }

    public void setLoanMessageContent(String loanMessageContent) {
        this.loanMessageContent = loanMessageContent;
    }
}
