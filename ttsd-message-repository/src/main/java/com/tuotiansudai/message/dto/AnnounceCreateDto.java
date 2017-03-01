package com.tuotiansudai.message.dto;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.message.repository.model.MessageCategory;
import com.tuotiansudai.message.repository.model.MessageChannel;
import com.tuotiansudai.message.repository.model.MessageUserGroup;

public class AnnounceCreateDto {

    private long id;

    private String title;

    private String content;

    private String contentText;

    private boolean showOnHome;

    public AnnounceCreateDto() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public boolean isShowOnHome() {
        return showOnHome;
    }

    public void setShowOnHome(boolean showOnHome) {
        this.showOnHome = showOnHome;
    }

    public MessageCreateDto transferTo() {
        MessageCreateDto messageCreateDto = new MessageCreateDto();
        messageCreateDto.setTitle(this.title);
        messageCreateDto.setTemplate(this.content);
        messageCreateDto.setTemplateTxt(this.contentText);
        messageCreateDto.setUserGroup(MessageUserGroup.ALL_USER);
        messageCreateDto.setChannels(Lists.newArrayList(MessageChannel.WEBSITE, MessageChannel.APP_MESSAGE));
        messageCreateDto.setMessageCategory(MessageCategory.NOTIFY);
        messageCreateDto.setAppUrl(AppUrl.MESSAGE_CENTER_LIST);
        PushCreateDto pushCreateDto = new PushCreateDto();
        pushCreateDto.setPushType(PushType.IMPORTANT_EVENT);
        pushCreateDto.setPushSource(PushSource.ALL);
        pushCreateDto.setContent(this.contentText);
        pushCreateDto.setJumpTo(AppUrl.MESSAGE_CENTER_LIST);
        messageCreateDto.setPush(pushCreateDto);
        messageCreateDto.setValidStartTime("0001-01-01 00:00:00");
        messageCreateDto.setValidEndTime("9999-12-31 23:59:59");

        return messageCreateDto;
    }
}
