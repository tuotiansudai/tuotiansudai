package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;

import java.text.SimpleDateFormat;

public class InvestRepayRecordResponseDataDto {

    /*** 标的id */
    @ApiModelProperty(value = "标的ID", example = "1111")
    private String loanId;

    /*** 标的名称 */
    @ApiModelProperty(value = "标的名称", example = "车辆抵押借款")
    private String loanName;

    /*** 投资id */
    @ApiModelProperty(value = "投资ID", example = "1111")
    private String investId;

    /*** 投资金额 */
    @ApiModelProperty(value = "投资金额", example = "11")
    private String investMoney;

    /*** 投资时间 */
    @ApiModelProperty(value = "投资时间", example = "2016-11-23")
    private String investTime;

    /*** 还款日 */
    @ApiModelProperty(value = "还款日", example = "2016-12-23")
    private String repayDay;

    /*** 还款状态 */
    @ApiModelProperty(value = "还款状态", example = "COMPLETE")
    private String status;

    /*** 还款状态描述 */
    @ApiModelProperty(value = "还款状态描述", example = "完成")
    private String statusDesc;

    /*** 本金 */
    @ApiModelProperty(value = "本金", example = "10000")
    private String corpus;

    /*** 利息 */
    @ApiModelProperty(value = "利息", example = "10000")
    private String interest;

    @ApiModelProperty(value = "产品线", example = "360天")
    private String loanType;

    @ApiModelProperty(value = "是否是富滇银行标的", example = "true")
    private boolean isBankPlatForm;

    public InvestRepayRecordResponseDataDto() {
    }

    public InvestRepayRecordResponseDataDto(InvestRepayModel investRepay, InvestModel invest, LoanModel loan) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        this.loanId = String.valueOf(loan.getId());
        this.loanName = loan.getName();
        this.investId = String.valueOf(invest.getId());
        this.investMoney = AmountConverter.convertCentToString(invest.getAmount());
        this.investTime = sdf.format(invest.getTradingTime() == null ? invest.getCreatedTime() : invest.getTradingTime());
        this.repayDay = sdfDay.format(investRepay.getRepayDate());
        this.status = investRepay.getStatus().name();
        this.statusDesc = investRepay.getStatus().getDescription();
        if (investRepay.getStatus() == RepayStatus.COMPLETE) {
            if(investRepay.getActualRepayDate() != null){

                this.repayDay = sdf.format(investRepay.getActualRepayDate());
            }
        }else{
            this.repayDay = sdfDay.format(investRepay.getRepayDate());
        }
        this.corpus = AmountConverter.convertCentToString(investRepay.getCorpus());
        if (RepayStatus.COMPLETE == investRepay.getStatus()) {
            this.interest = AmountConverter.convertCentToString(investRepay.getActualInterest() + investRepay.getDefaultInterest() - investRepay.getActualFee());
        } else {
            this.interest = AmountConverter.convertCentToString(investRepay.getExpectedInterest() + investRepay.getDefaultInterest() - investRepay.getExpectedFee());
        }
        this.loanType = loan.getProductType() != null ? loan.getProductType().name() : "";
        this.isBankPlatForm = loan.getIsBankPlatform();
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }

    public String getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(String investMoney) {
        this.investMoney = investMoney;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public String getRepayDay() {
        return repayDay;
    }

    public void setRepayDay(String repayDay) {
        this.repayDay = repayDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getCorpus() {
        return corpus;
    }

    public void setCorpus(String corpus) {
        this.corpus = corpus;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public boolean isBankPlatForm() {
        return isBankPlatForm;
    }

    public void setBankPlatForm(boolean bankPlatForm) {
        isBankPlatForm = bankPlatForm;
    }
}
