package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.LoanApplicationRegion;
import com.tuotiansudai.repository.model.PledgeType;

public class LoanApplicationDto {
    private String loginName;
    private LoanApplicationRegion region;
    private int amount;
    private int period;
    private PledgeType pledgeType;
    private String pledgeInfo;
    //合规审查新增字段
    private String identityNumber;
    private String address;
    private short age;
    //是否结婚
    private boolean isMarried;
    //是否有信用报告
    private boolean haveCreditReport;
    //工作职位
    private String workPosition;
    //蚂蚁积分
    private Integer sesameCredit;
    //家庭年收入(万)
    private int homeIncome;
    //借款用途
    private String loanUsage;
    //其他资产
    private String elsePledge;
    //性别
    private String sex;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public LoanApplicationRegion getRegion() {
        return region;
    }

    public void setRegion(LoanApplicationRegion region) {
        this.region = region;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public PledgeType getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(PledgeType pledgeType) {
        this.pledgeType = pledgeType;
    }

    public String getPledgeInfo() {
        return pledgeInfo;
    }

    public void setPledgeInfo(String pledgeInfo) {
        this.pledgeInfo = pledgeInfo;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public short getAge() {
        return age;
    }

    public void setAge(short age) {
        this.age = age;
    }

    public boolean getIsMarried() {
        return isMarried;
    }

    public void setIsMarried(boolean married) {
        isMarried = married;
    }

    public boolean getHaveCreditReport() {
        return haveCreditReport;
    }

    public void setHaveCreditReport(boolean haveCreditReport) {
        this.haveCreditReport = haveCreditReport;
    }

    public String getWorkPosition() {
        return workPosition;
    }

    public void setWorkPosition(String workPosition) {
        this.workPosition = workPosition;
    }

    public int getSesameCredit() {
        return sesameCredit;
    }

    public void setSesameCredit(int sesameCredit) {
        this.sesameCredit = sesameCredit;
    }

    public int getHomeIncome() {
        return homeIncome;
    }

    public void setHomeIncome(int homeIncome) {
        this.homeIncome = homeIncome;
    }

    public String getLoanUsage() {
        return loanUsage;
    }

    public void setLoanUsage(String loanUsage) {
        this.loanUsage = loanUsage;
    }

    public String getElsePledge() {
        return elsePledge;
    }

    public void setElsePledge(String elsePledge) {
        this.elsePledge = elsePledge;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
