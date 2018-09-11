package com.tuotiansudai.console.dto;

import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.repository.model.UserView;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserItemDataDto implements Serializable {
    private String loginName;
    private String userName;
    private String email;
    private String mobile;
    private String referrerMobile;
    private String staffMobile;
    private boolean fastPay;
    private String channel;
    private boolean staff;
    private Date registerTime;
    private List<UserRoleModel> userRoles;
    private UserStatus status;
    private Source source;
    private String autoInvestStatus;
    private boolean referrerStaff;
    private boolean bankCard;
    private boolean modify;
    private String identityNumber;
    private String province;
    private String city;
    private String balance;
    private Date lastBillTime;
    private String umpUserName;
    private String umpIdentityNumber;

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

    public String getReferrerMobile() {
        return referrerMobile;
    }

    public void setReferrerMobile(String referrerMobile) {
        this.referrerMobile = referrerMobile;
    }

    public boolean isFastPay() {
        return fastPay;
    }

    public void setFastPay(boolean fastPay) {
        this.fastPay = fastPay;
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

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public String getStaffMobile() {
        return staffMobile;
    }

    public void setStaffMobile(String staffMobile) {
        this.staffMobile = staffMobile;
    }

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

    public UserItemDataDto(UserView userView) {
        this.loginName = userView.getLoginName();
        this.email = userView.getEmail();
        this.referrerMobile = userView.getReferrerMobile();
        this.staffMobile = userView.getStaffMobile();
        this.channel = userView.getChannel();
        this.source = userView.getSource();
        this.mobile = userView.getMobile();
        this.registerTime = userView.getRegisterTime();
        this.userName = userView.getUserName();
        this.balance = AmountConverter.convertCentToString(userView.getBalance());
        this.status = userView.getStatus();
        this.autoInvestStatus = userView.getAutoInvestStatus();
        this.identityNumber = userView.getIdentityNumber();
        this.province = userView.getProvince();
        this.city = userView.getCity();
        this.lastBillTime = userView.getLastBillTime();
        this.referrerStaff = userView.isReferrerStaff();
        this.staff = userView.isStaff();
        this.bankCard = userView.isBindBankCard();
        this.fastPay = userView.isFastPay();
        this.umpUserName=userView.getUmpUserName();
        this.umpIdentityNumber=userView.getUmpIdentityNumber();
    }

    public String getUmpUserName() {
        return umpUserName;
    }

    public void setUmpUserName(String umpUserName) {
        this.umpUserName = umpUserName;
    }

    public String getUmpIdentityNumber() {
        return umpIdentityNumber;
    }

    public void setUmpIdentityNumber(String umpIdentityNumber) {
        this.umpIdentityNumber = umpIdentityNumber;
    }
}

