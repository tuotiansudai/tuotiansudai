package com.tuotiansudai.jpush.repository.model;


import com.tuotiansudai.jpush.dto.JPushAlertDto;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class JPushAlertModel implements Serializable{

    private long id;
    private String name;
    private PushType pushType;
    private List<String> pushDistricts;
    private PushSource pushSource;
    private List<PushUserType> pushUserType;
    private PushStatus status;
    private String content;
    private JumpTo jumpTo;
    private String jumpToLink;
    private Date expectPushTime;
    private Date createdTime;
    private String createdBy;
    private Date updatedTime;
    private String updatedBy;
    private boolean isAutomatic;

    private String auditor;
    private int iosTargetNum;
    private int iosArriveNum;
    private int iosOpenNum;
    private int androidTargetNum;
    private int androidArriveNum;
    private int androidOpenNum;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public PushSource getPushSource() {
        return pushSource;
    }

    public void setPushSource(PushSource pushSource) {
        this.pushSource = pushSource;
    }

    public PushStatus getStatus() {
        return status;
    }

    public void setStatus(PushStatus status) {
        this.status = status;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<String> getPushDistricts() {
        return pushDistricts;
    }

    public void setPushDistricts(List<String> pushDistricts) {
        this.pushDistricts = pushDistricts;
    }

    public String getJumpToLink() {
        return jumpToLink;
    }

    public void setJumpToLink(String jumpToLink) {
        this.jumpToLink = jumpToLink;
    }


    public boolean isAutomatic() {
        return isAutomatic;
    }

    public void setIsAutomatic(boolean isAutomatic) {
        this.isAutomatic = isAutomatic;
    }

    public List<PushUserType> getPushUserType() {
        return pushUserType;
    }

    public void setPushUserType(List<PushUserType> pushUserType) {
        this.pushUserType = pushUserType;
    }

    public Date getExpectPushTime() {
        return expectPushTime;
    }

    public void setExpectPushTime(Date expectPushTime) {
        this.expectPushTime = expectPushTime;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public int getIosTargetNum() {
        return iosTargetNum;
    }

    public void setIosTargetNum(int iosTargetNum) {
        this.iosTargetNum = iosTargetNum;
    }

    public int getIosArriveNum() {
        return iosArriveNum;
    }

    public void setIosArriveNum(int iosArriveNum) {
        this.iosArriveNum = iosArriveNum;
    }

    public int getIosOpenNum() {
        return iosOpenNum;
    }

    public void setIosOpenNum(int iosOpenNum) {
        this.iosOpenNum = iosOpenNum;
    }

    public int getAndroidTargetNum() {
        return androidTargetNum;
    }

    public void setAndroidTargetNum(int androidTargetNum) {
        this.androidTargetNum = androidTargetNum;
    }

    public int getAndroidArriveNum() {
        return androidArriveNum;
    }

    public void setAndroidArriveNum(int androidArriveNum) {
        this.androidArriveNum = androidArriveNum;
    }

    public int getAndroidOpenNum() {
        return androidOpenNum;
    }

    public void setAndroidOpenNum(int androidOpenNum) {
        this.androidOpenNum = androidOpenNum;
    }

    public JPushAlertModel(){

    }
    public JPushAlertModel(JPushAlertDto jPushAlertDto){
        if (StringUtils.isNotEmpty(jPushAlertDto.getId())){
            this.id = Long.parseLong(jPushAlertDto.getId());
        }
        this.name = jPushAlertDto.getName();
        this.pushType = jPushAlertDto.getPushType();
        this.pushDistricts = jPushAlertDto.getPushDistricts();
        this.pushSource = jPushAlertDto.getPushSource();
        this.pushUserType = jPushAlertDto.getPushUserType();
        this.status = PushStatus.WAIT_AUDIT;
        this.content = jPushAlertDto.getContent();
        this.jumpTo = jPushAlertDto.getJumpTo();
        this.jumpToLink = jPushAlertDto.getJumpToLink();
        this.expectPushTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(jPushAlertDto.getExpectPushTime()).toDate();
        this.isAutomatic = jPushAlertDto.isAutomatic();
    }
}
