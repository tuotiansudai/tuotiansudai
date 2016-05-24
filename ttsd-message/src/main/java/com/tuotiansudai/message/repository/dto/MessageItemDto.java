package com.tuotiansudai.message.repository.dto;

import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageStatus;

import java.io.Serializable;

/**
 * Created by gengbeijun on 16/5/23.
 */
public class MessageItemDto implements Serializable{

    private String title;
    private MessageStatus messageStatus;
    private String createBy;


    public MessageItemDto(MessageModel messageModel){
        this.title = messageModel.getTitle();
        this.messageStatus = messageModel.getStatus();
        this.createBy = messageModel.getCreatedBy();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }



}
