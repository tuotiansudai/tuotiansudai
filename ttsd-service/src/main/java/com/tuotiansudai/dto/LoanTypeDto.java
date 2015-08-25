package com.tuotiansudai.dto;

public class LoanTypeDto {
    //标的类型名称
    private String name;
    //标的类型对应的enum属性名
    private String loanTypeName;
    //时间单位
    private String repayTimeUnit;
    //时间间隔
    private String repayTimePeriod;

    public LoanTypeDto(String name,String loanTypeName,String repayTimeUnit,String repayTimePeriod){
        this.name = name;
        this.loanTypeName = loanTypeName;
        this.repayTimeUnit = repayTimeUnit;
        this.repayTimePeriod = repayTimePeriod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoanTypeName() {
        return loanTypeName;
    }

    public void setLoanTypeName(String loanTypeName) {
        this.loanTypeName = loanTypeName;
    }

    public String getRepayTimeUnit() {
        return repayTimeUnit;
    }

    public void setRepayTimeUnit(String repayTimeUnit) {
        this.repayTimeUnit = repayTimeUnit;
    }

    public String getRepayTimePeriod() {
        return repayTimePeriod;
    }

    public void setRepayTimePeriod(String repayTimePeriod) {
        this.repayTimePeriod = repayTimePeriod;
    }
}
