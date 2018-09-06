package com.tuotiansudai.dto;

/**
 * Created by qduljs2011 on 2018/9/5.
 */
public class LoanerInfoDto extends BaseDataDto {
    private String userName;
    private String sex;
    private int age;
    private String identityNumber;

    public LoanerInfoDto() {
    }

    public LoanerInfoDto(boolean status) {
        super(status);
    }

    public LoanerInfoDto(boolean status, String message) {
        super(status,message);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
