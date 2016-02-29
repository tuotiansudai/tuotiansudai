package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserItemDataDto implements Serializable {
    private String loginName;
    private String userName;
    private String email;
    private String mobile;
    private String referrer;
    private String channel;
    private boolean staff;
    private Date registerTime;
    private List<UserRoleModel> userRoles;
    private UserStatus status;
    private Source source;
    private String autoInvestStatus;
    private boolean referrerStaff;
    private boolean bankCard;
    private String identityNumber;
    private String province;
    private String city;
    private String balance;
    private Date lastBillTime;
    private Long point;
    private int availablePoint;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isStaff() {
        return staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public List<UserRoleModel> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRoleModel> userRoles) {
        this.userRoles = userRoles;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getAutoInvestStatus() {
        return autoInvestStatus;
    }

    public void setAutoInvestStatus(String autoInvestStatus) {
        this.autoInvestStatus = autoInvestStatus;
    }

    public boolean isReferrerStaff() {
        return referrerStaff;
    }

    public void setReferrerStaff(boolean referrerStaff) {
        this.referrerStaff = referrerStaff;
    }

    public boolean isBankCard() {
        return bankCard;
    }

    public void setBankCard(boolean bankCard) {
        this.bankCard = bankCard;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Date getLastBillTime() {
        return lastBillTime;
    }

    public void setLastBillTime(Date lastBillTime) {
        this.lastBillTime = lastBillTime;
    }

    public Long getPoint() {return point;}

    public void setPoint(Long point) { this.point = point; }

    public int getAvailablePoint() { return availablePoint; }

    public void setAvailablePoint(int availablePoint) { this.availablePoint = availablePoint; }

    public String getBirthday() {
        if (identityNumber == null) {
            return "";
        } else if (identityNumber.length() == 18) {
            return identityNumber.substring(6, 14);
        } else if (identityNumber.length() == 15) {
            return identityNumber.substring(6, 12);
        } else {
            return "";
        }
    }

    public UserItemDataDto(UserModel userModel) {
        this.loginName = userModel.getLoginName();
        this.email = userModel.getEmail();
        this.referrer = userModel.getReferrer();
        this.channel = userModel.getChannel();
        this.source = userModel.getSource();
        this.mobile = userModel.getMobile();
        this.registerTime = userModel.getRegisterTime();
        if (userModel.getAccount() != null) {
            this.userName = userModel.getAccount().getUserName();
            this.point = userModel.getAccount().getPoint();
        }
        this.balance = AmountConverter.convertCentToString(userModel.getAccount().getBalance());
        this.status = userModel.getStatus();
        this.autoInvestStatus = userModel.getAutoInvestStatus();
        this.identityNumber = userModel.getAccount().getIdentityNumber();
        this.province = userModel.getProvince();
        this.city = userModel.getCity();
        this.lastBillTime = userModel.getLastBillTime();
    }
}

