package com.tuotiansudai.jpush.dto;

import com.tuotiansudai.jpush.repository.model.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import java.util.List;

public class JPushAlertDto {

    private String id;
    @NotEmpty
    private String name;
    @NotNull
    private PushType pushType;

    private List<String> pushDistricts;
    @NotNull
    private PushSource pushSource;

    private List<PushUserType> pushUserType;

    private String content;

    private JumpTo jumpTo;

    private String jumpToLink;

    private String expectPushTime;

    private boolean isAutomatic;

    private String investId;

    private String loanId;

    private String isCompleted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PushType getPushType() {
        return pushType;
    }

    public void setPushType(PushType pushType) {
        this.pushType = pushType;
    }

    public List<String> getPushDistricts() {
        return pushDistricts;
    }

    public void setPushDistricts(List<String> pushDistricts) {
        this.pushDistricts = pushDistricts;
    }

    public PushSource getPushSource() {
        return pushSource;
    }

    public void setPushSource(PushSource pushSource) {
        this.pushSource = pushSource;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public JumpTo getJumpTo() {
        return jumpTo;
    }

    public void setJumpTo(JumpTo jumpTo) {
        this.jumpTo = jumpTo;
    }

    public String getJumpToLink() {
        return jumpToLink;
    }

    public void setJumpToLink(String jumpToLink) {
        this.jumpToLink = jumpToLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PushUserType> getPushUserType() {
        return pushUserType;
    }

    public void setPushUserType(List<PushUserType> pushUserType) {
        this.pushUserType = pushUserType;
    }

    public String getExpectPushTime() {
        return expectPushTime;
    }

    public void setExpectPushTime(String expectPushTime) {
        this.expectPushTime = expectPushTime;
    }

    public boolean isAutomatic() {
        return isAutomatic;
    }

    public void setAutomatic(boolean isAutomatic) {
        this.isAutomatic = isAutomatic;
    }

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    public JPushAlertDto(){

    }
    public JPushAlertDto(JPushAlertModel jPushAlertModel){
        this.id = "" + jPushAlertModel.getId();
        this.name = jPushAlertModel.getName();
        this.pushType = jPushAlertModel.getPushType();
        this.pushDistricts = jPushAlertModel.getPushDistricts();
        this.pushUserType = jPushAlertModel.getPushUserType();
        this.pushSource = jPushAlertModel.getPushSource();
        this.content = jPushAlertModel.getContent();
        this.jumpTo = jPushAlertModel.getJumpTo();
        this.jumpToLink = jPushAlertModel.getJumpToLink();
        this.expectPushTime = new DateTime(jPushAlertModel.getExpectPushTime()).toString("yyyy-MM-dd HH:mm");
    }

    public JPushAlertDto(JPushAlertModel jPushAlertModel,String investId,String loanId,String isCompleted){
        this.id = "" + jPushAlertModel.getId();
        this.name = jPushAlertModel.getName();
        this.pushType = jPushAlertModel.getPushType();
        this.pushDistricts = jPushAlertModel.getPushDistricts();
        this.pushUserType = jPushAlertModel.getPushUserType();
        this.pushSource = jPushAlertModel.getPushSource();
        this.content = jPushAlertModel.getContent();
        this.jumpTo = jPushAlertModel.getJumpTo();
        this.jumpToLink = jPushAlertModel.getJumpToLink();
        this.expectPushTime = new DateTime(jPushAlertModel.getExpectPushTime()).toString("yyyy-MM-dd HH:mm");
        this.investId = investId;
        this.loanId = loanId;
        this.isCompleted = isCompleted;
    }
}
