package com.tuotiansudai.repository.model;

import java.util.Date;

public class AccountModel {
    private Long id;

    private String loginName;

    private String userName;

    private String identityNumber;

    private String payUserId;

    private String payAccountId;

    private Date registerTime;

    public AccountModel() {
    }

    public AccountModel(String loginName, String userName, String identityNumber, String payUserId, String payAccountId, Date registerTime) {
        this.loginName = loginName;
        this.userName = userName;
        this.identityNumber = identityNumber;
        this.payUserId = payUserId;
        this.payAccountId = payAccountId;
        this.registerTime = registerTime;
    }

    public Long getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getUserName() {
        return userName;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public String getPayUserId() {
        return payUserId;
    }

    public String getPayAccountId() {
        return payAccountId;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountModel that = (AccountModel) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
