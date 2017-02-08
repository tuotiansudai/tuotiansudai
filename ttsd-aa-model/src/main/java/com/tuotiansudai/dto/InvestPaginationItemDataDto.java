package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.InvestPaginationItemView;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;

public class InvestPaginationItemDataDto implements Serializable {
    private long investId;
    private long loanId;
    private String loanName;
    private int loanPeriods;
    private String investorLoginName;
    private String investorUserName;
    private String investorMobile;
    private boolean investorStaff;
    private String referrerLoginName;
    private String referrerUserName;
    private String referrerMobile;
    private boolean referrerStaff;
    private String channel;
    private Source source;
    private Date investTime;
    private boolean autoInvest;
    private String investAmount;
    private String couponDetail;
    private String couponActualInterest;
    private String extraDetail;
    private String extraActualInterest;
    private InvestStatus investStatus;
    private String province;
    private String city;
    private String identityNumber;

    public InvestPaginationItemDataDto(InvestPaginationItemView view) {
        this.investId = view.getInvestId();
        this.loanId = view.getLoanId();
        this.loanName = view.getLoanName();
        this.loanPeriods = view.getLoanPeriods();
        this.investorLoginName = view.getInvestorLoginName();
        this.investorUserName = view.getInvestorUserName();
        this.investorMobile = view.getInvestorMobile();
        this.investorStaff = view.getInvestorRoleList().contains(Role.STAFF);
        this.referrerLoginName = view.getReferrerLoginName();
        this.referrerUserName = view.getReferrerUserName();
        this.referrerMobile = view.getReferrerMobile();
        this.referrerStaff = view.getReferrerRoleList().contains(Role.STAFF);
        this.channel = view.getChannel();
        this.source = view.getSource();
        this.investTime = view.getInvestTime();
        this.autoInvest = view.isAutoInvest();
        this.investAmount = AmountConverter.convertCentToString(view.getInvestAmount());
        this.extraDetail = view.getExtraRate() == 0 ? "-" : MessageFormat.format("{0,number,#.##}%", view.getExtraRate() * 100);
        this.extraActualInterest = view.getExtraActualInterest() == 0 ? "-" : AmountConverter.convertCentToString(view.getExtraActualInterest());
        this.investStatus = view.getInvestStatus();
        this.province = view.getProvince();
        this.city = view.getCity();
        this.identityNumber = view.getIdentityNumber();
    }

    public void setCouponDetail(CouponModel couponModel) {
        switch (couponModel.getCouponType()) {
            case RED_ENVELOPE:
            case NEWBIE_COUPON:
            case INVEST_COUPON:
                this.couponDetail = MessageFormat.format("{0}元{1}", AmountConverter.convertCentToString(couponModel.getAmount()), couponModel.getCouponType().getName());
                break;
            case INTEREST_COUPON:
                this.couponDetail = MessageFormat.format("{0,number,#.##}%{1}", couponModel.getRate() * 100, couponModel.getCouponType().getName());
                break;
            case BIRTHDAY_COUPON:
                this.couponDetail = MessageFormat.format("{0}倍{1}", couponModel.getBirthdayBenefit() + 1, couponModel.getCouponType().getName());
                break;
            default:
                this.couponDetail = "";
                break;
        }
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public int getLoanPeriods() {
        return loanPeriods;
    }

    public void setLoanPeriods(int loanPeriods) {
        this.loanPeriods = loanPeriods;
    }

    public String getInvestorLoginName() {
        return investorLoginName;
    }

    public void setInvestorLoginName(String investorLoginName) {
        this.investorLoginName = investorLoginName;
    }

    public String getInvestorUserName() {
        return investorUserName;
    }

    public void setInvestorUserName(String investorUserName) {
        this.investorUserName = investorUserName;
    }

    public String getInvestorMobile() {
        return investorMobile;
    }

    public void setInvestorMobile(String investorMobile) {
        this.investorMobile = investorMobile;
    }

    public boolean isInvestorStaff() {
        return investorStaff;
    }

    public void setInvestorStaff(boolean investorStaff) {
        this.investorStaff = investorStaff;
    }

    public String getReferrerLoginName() {
        return referrerLoginName;
    }

    public void setReferrerLoginName(String referrerLoginName) {
        this.referrerLoginName = referrerLoginName;
    }

    public String getReferrerUserName() {
        return referrerUserName;
    }

    public void setReferrerUserName(String referrerUserName) {
        this.referrerUserName = referrerUserName;
    }

    public String getReferrerMobile() {
        return referrerMobile;
    }

    public void setReferrerMobile(String referrerMobile) {
        this.referrerMobile = referrerMobile;
    }

    public boolean isReferrerStaff() {
        return referrerStaff;
    }

    public void setReferrerStaff(boolean referrerStaff) {
        this.referrerStaff = referrerStaff;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public boolean isAutoInvest() {
        return autoInvest;
    }

    public void setAutoInvest(boolean autoInvest) {
        this.autoInvest = autoInvest;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getCouponDetail() {
        return couponDetail;
    }

    public void setCouponDetail(String couponDetail) {
        this.couponDetail = couponDetail;
    }

    public String getCouponActualInterest() {
        return couponActualInterest;
    }

    public void setCouponActualInterest(long couponActualInterest) {
        this.couponActualInterest = AmountConverter.convertCentToString(couponActualInterest);
    }

    public String getExtraDetail() {
        return extraDetail;
    }

    public void setExtraDetail(String extraDetail) {
        this.extraDetail = extraDetail;
    }

    public String getExtraActualInterest() {
        return extraActualInterest;
    }

    public void setExtraActualInterest(String extraActualInterest) {
        this.extraActualInterest = extraActualInterest;
    }

    public InvestStatus getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(InvestStatus investStatus) {
        this.investStatus = investStatus;
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

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

}
