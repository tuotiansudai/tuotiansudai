package com.tuotiansudai.message.dto;

import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.message.repository.model.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

public class MessageCreateDto implements Serializable {
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String template;

    @NotEmpty
    private String templateTxt;

    @NotEmpty
    private MessageUserGroup userGroup;

    @NotEmpty
    private List<MessageChannel> channels;

    @NotEmpty
    private MessageCategory messageCategory;

    private String webUrl;

    private AppUrl appUrl;

    private PushCreateDto push;

    private Long importUsersFlag;

    private String validStartTime;

    private String validEndTime;

    public MessageCreateDto() {
    }

    public MessageCreateDto(MessageModel messageModel, PushModel pushModel) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.id = messageModel.getId();
        this.title = messageModel.getTitle();
        this.template = messageModel.getTemplateTxt();
        this.userGroup = messageModel.getUserGroup();
        this.channels = messageModel.getChannels();
        this.messageCategory = messageModel.getMessageCategory();
        this.webUrl = messageModel.getWebUrl();
        this.appUrl = messageModel.getAppUrl();
        this.validStartTime = simpleDateFormat.format(messageModel.getValidStartTime());
        this.validEndTime = simpleDateFormat.format(messageModel.getValidEndTime());
        this.push = pushModel != null ? new PushCreateDto(pushModel) : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public MessageUserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(MessageUserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public Long getImportUsersFlag() {
        return importUsersFlag;
    }

    public void setImportUsersFlag(Long importUsersFlag) {
        this.importUsersFlag = importUsersFlag;
    }

    public List<MessageChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<MessageChannel> channels) {
        this.channels = channels;
    }

    public MessageCategory getMessageCategory() {
        return messageCategory;
    }

    public void setMessageCategory(MessageCategory messageCategory) {
        this.messageCategory = messageCategory;
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

    public PushCreateDto getPush() {
        return push;
    }

    public void setPush(PushCreateDto push) {
        this.push = push;
    }

    public String getValidStartTime() {
        return validStartTime;
    }

    public void setValidStartTime(String validStartTime) {
        this.validStartTime = validStartTime;
    }

    public String getValidEndTime() {
        return validEndTime;
    }

    public void setValidEndTime(String validEndTime) {
        this.validEndTime = validEndTime;
    }
}
