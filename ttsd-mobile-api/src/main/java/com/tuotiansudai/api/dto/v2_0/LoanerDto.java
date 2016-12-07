package com.tuotiansudai.api.dto.v2_0;


import com.tuotiansudai.repository.model.LoanerDetailsModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class LoanerDto implements Serializable {

    @ApiModelProperty(value = "性别", example = "男")
    private String gender;

    @ApiModelProperty(value = "年龄", example = "33")
    private Integer age;

    @ApiModelProperty(value = "婚姻", example = "否")
    private String marriage;

    @ApiModelProperty(value = "职业", example = "百度")
    private String employmentStatus;

    @ApiModelProperty(value = "收入", example = "10000")
    private String income;
    private String purpose;
    private String overdueRate;

    public LoanerDto(LoanerDetailsModel model) {
        this.gender = model.getGender().getDescription();
        this.age = model.getAge();
        this.marriage = model.getMarriage().getDescription();
        this.employmentStatus = model.getEmploymentStatus();
        this.income = model.getIncome();
        this.purpose = model.getPurpose();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(String overdueRate) {
        this.overdueRate = overdueRate;
    }
}
