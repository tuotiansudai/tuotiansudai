package com.tuotiansudai.membership.repository.model;

import com.tuotiansudai.enums.MembershipPrivilegePurchaseStatus;
import com.tuotiansudai.membership.dto.MembershipPrivilegePurchaseDto;
import com.tuotiansudai.repository.model.Source;

import java.io.Serializable;
import java.util.Date;

public class MembershipPrivilegePurchaseModel implements Serializable{
    private long id;
    private String loginName;
    private String mobile;
    private String userName;
    private MembershipPrivilegePriceType privilegePriceType;
    private MembershipPrivilege privilege;
    private long amount;
    private MembershipPrivilegePurchaseStatus status;
    private Source source;
    private Date createdTime;

    public MembershipPrivilegePurchaseModel(){}

    public MembershipPrivilegePurchaseModel(long id,String loginName,String mobile,String userName,
                                            MembershipPrivilegePriceType privilegePriceType,
                                            MembershipPrivilege privilege,Source source){
        this.id = id;
        this.loginName = loginName;
        this.mobile = mobile;
        this.userName = userName;
        this.privilegePriceType = privilegePriceType;
        this.privilege = privilege;
        this.amount = privilegePriceType.getPrice();
        this.status = MembershipPrivilegePurchaseStatus.WAIT_PAY;
        this.source = source;
        this.createdTime = new Date();

    }
    public MembershipPrivilegePurchaseModel(long id, MembershipPrivilegePurchaseDto purchaseDto){
        this(id, purchaseDto.getLoginName(),
                purchaseDto.getMobile(),
                purchaseDto.getUserName(),
                MembershipPrivilegePriceType.getPriceTypeByDuration(purchaseDto.getDuration()),
                MembershipPrivilege.SERVICE_FEE, purchaseDto.getSource());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public MembershipPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(MembershipPrivilege privilege) {
        this.privilege = privilege;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public MembershipPrivilegePurchaseStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipPrivilegePurchaseStatus status) {
        this.status = status;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public MembershipPrivilegePriceType getPrivilegePriceType() {
        return privilegePriceType;
    }

    public void setPrivilegePriceType(MembershipPrivilegePriceType privilegePriceType) {
        this.privilegePriceType = privilegePriceType;
    }
}
