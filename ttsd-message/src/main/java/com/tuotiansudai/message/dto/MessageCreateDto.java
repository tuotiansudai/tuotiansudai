package com.tuotiansudai.message.dto;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.jpush.dto.JPushAlertDto;
import com.tuotiansudai.jpush.repository.model.JumpTo;
import com.tuotiansudai.jpush.repository.model.PushUserType;
import com.tuotiansudai.message.repository.model.*;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageCreateDto implements Serializable {
    private long id;
    private String title;
    private String template;
    private String templateTxt;
    private MessageType type;
    private List<MessageUserGroup> userGroups;
    private List<MessageChannel> channels;
    private ManualMessageType manualMessageType;
    private String webUrl;
    private AppUrl appUrl;
    private boolean jpush;
    private PushType pushType;
    private PushSource pushSource;
    private List<String> pushDistricts;
    private MessageStatus status;
    private long readCount;
    private String activatedBy;
    private Date activatedTime;
    private Date expiredTime;
    private String updatedBy;
    private Date updatedTime;
    private String createdBy;
    private Date createdTime;

    public MessageCreateDto() {
    }

    public MessageCreateDto(MessageModel messageModel) {
        this.id = messageModel.getId();
        this.title = messageModel.getTitle();
        this.template = messageModel.getTemplate();
        this.templateTxt = messageModel.getTemplateTxt();
        this.type = messageModel.getType();
        this.userGroups = messageModel.getUserGroups();
        this.channels = messageModel.getChannels();
        this.manualMessageType = messageModel.getManualMessageType();
        this.webUrl = messageModel.getWebUrl();
        this.appUrl = messageModel.getAppUrl();
        this.status = messageModel.getStatus();
        this.readCount = messageModel.getReadCount();
        this.activatedBy = messageModel.getActivatedBy();
        this.activatedTime = messageModel.getActivatedTime();
        this.expiredTime = messageModel.getExpiredTime();
        this.updatedBy = messageModel.getUpdatedBy();
        this.updatedTime = messageModel.getUpdatedTime();
        this.createdBy = messageModel.getCreatedBy();
        this.createdTime = messageModel.getCreatedTime();
    }

    public MessageModel getMessageModel() {
        MessageModel messageModel = new MessageModel();

        messageModel.setId(this.id);
        messageModel.setTitle(this.title);
        messageModel.setAppTitle(this.title);
        messageModel.setTemplate(this.template);
        messageModel.setTemplateTxt(this.templateTxt);
        messageModel.setUserGroups(this.userGroups);
        messageModel.setChannels(this.channels);
        messageModel.setManualMessageType(this.manualMessageType);
        messageModel.setWebUrl(this.webUrl);
        messageModel.setAppUrl(this.appUrl);
        messageModel.setUpdatedBy(this.updatedBy);
        messageModel.setCreatedBy(this.createdBy);

        return messageModel;
    }

    public JPushAlertDto getJPushAlertDto() {
        JPushAlertDto jpushAlertDto = new JPushAlertDto();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        jpushAlertDto.setId(null);
        jpushAlertDto.setName(this.title);
        jpushAlertDto.setPushType(this.pushType);
        jpushAlertDto.setPushDistricts(CollectionUtils.isEmpty(this.pushDistricts) ? null : this.pushDistricts);
        jpushAlertDto.setPushSource(this.pushSource);
        jpushAlertDto.setPushUserType(Lists.newArrayList(PushUserType.ALL));
        jpushAlertDto.setContent(this.templateTxt);
        jpushAlertDto.setJumpTo(JumpTo.MESSAGE_CENTER);
        jpushAlertDto.setJumpToLink(null);
        jpushAlertDto.setExpectPushTime(simpleDateFormat.format(new Date()));
        jpushAlertDto.setAutomatic(false);
        jpushAlertDto.setInvestId(null);
        jpushAlertDto.setLoanId(null);
        jpushAlertDto.setIsCompleted(null);

        return jpushAlertDto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTemplateTxt() {
        return templateTxt;
    }

    public void setTemplateTxt(String templateTxt) {
        this.templateTxt = templateTxt;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public List<MessageUserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<MessageUserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public List<MessageChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<MessageChannel> channels) {
        this.channels = channels;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public long getReadCount() {
        return readCount;
    }

    public void setReadCount(long readCount) {
        this.readCount = readCount;
    }

    public String getActivatedBy() {
        return activatedBy;
    }

    public void setActivatedBy(String activatedBy) {
        this.activatedBy = activatedBy;
    }

    public Date getActivatedTime() {
        return activatedTime;
    }

    public void setActivatedTime(Date activatedTime) {
        this.activatedTime = activatedTime;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public ManualMessageType getManualMessageType() {
        return manualMessageType;
    }

    public void setManualMessageType(ManualMessageType manualMessageType) {
        this.manualMessageType = manualMessageType;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public AppUrl getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(AppUrl appUrl) {
        this.appUrl = appUrl;
    }

    public boolean isJpush() {
        return jpush;
    }

    public void setJpush(boolean jpush) {
        this.jpush = jpush;
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

    public List<String> getPushDistricts() {
        return pushDistricts;
    }

    public void setPushDistricts(List<String> pushDistricts) {
        this.pushDistricts = pushDistricts;
    }
}
