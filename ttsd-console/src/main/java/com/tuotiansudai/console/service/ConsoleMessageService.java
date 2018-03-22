package com.tuotiansudai.console.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.enums.MessageType;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.message.dto.MessageCreateDto;
import com.tuotiansudai.message.dto.MessagePaginationItemDto;
import com.tuotiansudai.message.dto.PushCreateDto;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.PushMapper;
import com.tuotiansudai.message.repository.model.*;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.util.PaginationUtil;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsoleMessageService {

    private final static String MESSAGE_IMPORT_USER_TEMP_KEY = "message:manual-message:receivers:temp:{0}";

    private final static String MESSAGE_IMPORT_USER_KEY = "message:manual-message:receivers";

    private static Logger logger = Logger.getLogger(ConsoleMessageService.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private PushMapper pushMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    public BasePaginationDataDto<MessagePaginationItemDto> findMessagePagination(String title, MessageStatus messageStatus, String updatedBy, MessageType messageType, MessageCategory messageCategory, int index, int pageSize) {
        long count = messageMapper.findMessageCount(title, messageStatus, updatedBy, messageType, messageCategory);
        List<MessageModel> messageModels = messageMapper.findMessagePagination(title, messageStatus, updatedBy, messageType, messageCategory, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return new BasePaginationDataDto<>(index, pageSize, count, messageModels.stream().map(messageModel -> new MessagePaginationItemDto(messageModel, pushMapper.findById(messageModel.getPushId()))).collect(Collectors.toList()));
    }

    @Transactional
    public Long createOrUpdateManualMessage(String loginName, MessageCreateDto messageCreateDto) {
        Long messageId = messageCreateDto.getId();
        if (messageId == null) {
            Long pushId = this.createOrUpdatePush(loginName, messageCreateDto.getPush());
            return createManualMessage(loginName, pushId, messageCreateDto);
        }
        updateManualMessage(loginName, messageCreateDto);

        return messageId;
    }

    public long createImportUsers(InputStream inputStream) throws IOException {
        long importUsersFlag = new Date().getTime();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String rawData;
        while (null != (rawData = bufferedReader.readLine())) {
            stringBuilder.append(rawData);
        }
        redisWrapperClient.setex(MessageFormat.format(MESSAGE_IMPORT_USER_TEMP_KEY, String.valueOf(importUsersFlag)), 60 * 60, stringBuilder.toString());
        return importUsersFlag;
    }

    public BaseDto<BaseDataDto> approveMessage(long messageId, String approvedBy) {
        MessageModel messageModel = messageMapper.findActiveById(messageId);
        if (messageModel == null || messageModel.getStatus() != MessageStatus.TO_APPROVE) {
            return new BaseDto<>(new BaseDataDto(false, "消息审核失败"));
        }

        messageModel.setActivatedBy(approvedBy);
        messageModel.setActivatedTime(new Date());
        messageModel.setUpdatedTime(new Date());
        messageModel.setUpdatedBy(approvedBy);
        messageModel.setStatus(MessageStatus.APPROVED);
        messageMapper.update(messageModel);

        PushModel pushModel = pushMapper.findById(messageModel.getPushId());
        if (pushModel != null && MessageType.MANUAL.equals(messageModel.getType())) {
            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(
                    messageModel.getUserGroup() == MessageUserGroup.IMPORT_USER ? (List<String>) redisWrapperClient.hgetSeri(MESSAGE_IMPORT_USER_KEY, String.valueOf(messageModel.getId())) : null,
                    pushModel.getPushSource(),
                    pushModel.getPushType(),
                    pushModel.getContent(),
                    pushModel.getJumpTo()));
        }
        return new BaseDto<>(new BaseDataDto(true));
    }

    public BaseDto<BaseDataDto> rejectMessage(long messageId, String approvedBy) {
        MessageModel messageModel = messageMapper.findActiveById(messageId);
        if (messageModel == null
                || (messageModel.getType() == MessageType.EVENT && messageModel.getStatus() != MessageStatus.APPROVED)
                || (messageModel.getType() == MessageType.MANUAL && messageModel.getStatus() != MessageStatus.TO_APPROVE)) {
            return new BaseDto<>(new BaseDataDto(false, "消息审核失败"));
        }
        messageModel.setUpdatedTime(new Date());
        messageModel.setUpdatedBy(approvedBy);
        messageModel.setStatus(messageModel.getType() == MessageType.EVENT ? MessageStatus.TO_APPROVE : MessageStatus.REJECTION);
        messageMapper.update(messageModel);

        return new BaseDto<>(new BaseDataDto(true));
    }

    public BaseDto<BaseDataDto> deleteMessage(long messageId, String updatedBy) {
        MessageModel messageModel = messageMapper.findActiveById(messageId);
        if (messageModel == null || messageModel.getStatus() != MessageStatus.TO_APPROVE) {
            return new BaseDto<>(new BaseDataDto(false, "消息不存在"));
        }
        messageMapper.deleteById(messageId, updatedBy);
        redisWrapperClient.hdelSeri(MESSAGE_IMPORT_USER_KEY, String.valueOf(messageId));
        return new BaseDto<>(new BaseDataDto(true));
    }

    public MessageCreateDto getEditMessage(long messageId) {
        MessageModel messageModel = messageMapper.findActiveById(messageId);
        PushModel pushModel = pushMapper.findById(messageModel.getPushId());
        return new MessageCreateDto(messageModel, pushModel);
    }

    @SuppressWarnings(value = "unchecked")
    private Long createManualMessage(String createdBy, Long pushId, MessageCreateDto messageCreateDto) {
        Date validStartTime = null;
        Date validEndTime = null;
        try {
            validStartTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(messageCreateDto.getValidStartTime());
        } catch (ParseException e) {
            logger.error(MessageFormat.format("[ConsoleMessageService][createManualMessage] create message {0} validStartTime fail, parse string:{1}", messageCreateDto.getId(), messageCreateDto.getValidStartTime()));
        }
        try {
            validEndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(messageCreateDto.getValidEndTime());
        } catch (ParseException e) {
            logger.error(MessageFormat.format("[ConsoleMessageService][createManualMessage] create message {0} validEndTime fail, parse string:{1}", messageCreateDto.getId(), messageCreateDto.getValidEndTime()));
        }
        if (null == validStartTime || null == validEndTime) {
            return null;
        }
        MessageModel messageModel = new MessageModel(messageCreateDto.getTitle(),
                messageCreateDto.getTemplate(),
                messageCreateDto.getTemplateTxt(),
                messageCreateDto.getUserGroup(),
                messageCreateDto.getMessageCategory(),
                messageCreateDto.getChannels(),
                messageCreateDto.getWebUrl(),
                messageCreateDto.getAppUrl(),
                pushId,
                createdBy,
                validStartTime,
                validEndTime);
        messageMapper.create(messageModel);
        if (messageCreateDto.getUserGroup() == MessageUserGroup.IMPORT_USER) {
            String messageId = String.valueOf(messageModel.getId());
            String importUsers = redisWrapperClient.get(MessageFormat.format(MESSAGE_IMPORT_USER_TEMP_KEY, String.valueOf(messageCreateDto.getImportUsersFlag())));
            redisWrapperClient.hsetSeri(MESSAGE_IMPORT_USER_KEY, messageId, Lists.newArrayList(Splitter.on(',').splitToList(importUsers).stream().filter(loginName -> !StringUtils.isEmpty(loginName.trim())).collect(Collectors.toList())));
        }

        return messageModel.getId();
    }

    @SuppressWarnings(value = "unchecked")
    private void updateManualMessage(String updatedBy, MessageCreateDto messageCreateDto) {
        MessageModel messageModel = messageMapper.findActiveById(messageCreateDto.getId());

        if (messageCreateDto.getPush() == null) {
            pushMapper.deleteById(messageModel.getPushId());
        } else {
            messageModel.setPushId(this.createOrUpdatePush(updatedBy, messageCreateDto.getPush()));
        }

        messageModel.setTitle(messageCreateDto.getTitle());
        messageModel.setTemplate(messageCreateDto.getTemplateTxt());
        messageModel.setTemplateTxt(messageCreateDto.getTemplateTxt());
        messageModel.setUserGroup(messageCreateDto.getUserGroup());
        messageModel.setChannels(messageCreateDto.getChannels());
        messageModel.setMessageCategory(messageCreateDto.getMessageCategory());
        messageModel.setWebUrl(messageCreateDto.getWebUrl());
        messageModel.setAppUrl(messageCreateDto.getAppUrl());
        messageModel.setUpdatedBy(updatedBy);
        messageModel.setUpdatedTime(new Date());
        try {
            messageModel.setValidStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(messageCreateDto.getValidStartTime()));
        } catch (ParseException e) {
            logger.error(MessageFormat.format("[ConsoleMessageService][updateManualMessage] update message {0} validStartTime fail, parse string:{1}", messageCreateDto.getId(), messageCreateDto.getValidStartTime()));
        }
        try {
            messageModel.setValidEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(messageCreateDto.getValidEndTime()));
        } catch (ParseException e) {
            logger.error(MessageFormat.format("[ConsoleMessageService][updateManualMessage] update message {0} validEndTime fail, parse string:{1}", messageCreateDto.getId(), messageCreateDto.getValidEndTime()));
        }
        messageMapper.update(messageModel);

        if (messageCreateDto.getUserGroup() == MessageUserGroup.IMPORT_USER && messageCreateDto.getImportUsersFlag() != null && messageCreateDto.getImportUsersFlag() != messageModel.getId()) {
            String messageId = String.valueOf(messageModel.getId());
            String importUsers = redisWrapperClient.get(MessageFormat.format(MESSAGE_IMPORT_USER_TEMP_KEY, String.valueOf(messageCreateDto.getImportUsersFlag())));
            redisWrapperClient.hsetSeri(MESSAGE_IMPORT_USER_KEY, messageId, Lists.newArrayList(Splitter.on(',').splitToList(importUsers).stream().filter(loginName -> !StringUtils.isEmpty(loginName.trim())).collect(Collectors.toList())));
        }
    }

    private Long createOrUpdatePush(String createdOrUpdatedBy, PushCreateDto pushCreateDto) {
        if (pushCreateDto == null) {
            return null;
        }

        if (pushCreateDto.getId() == null) {
            PushModel pushModel = new PushModel(createdOrUpdatedBy, pushCreateDto.getPushType(), pushCreateDto.getPushSource(), pushCreateDto.getContent(), pushCreateDto.getJumpTo());
            pushMapper.create(pushModel);
            return pushModel.getId();
        }

        PushModel pushModel = pushMapper.findById(pushCreateDto.getId());
        pushModel.setUpdatedBy(createdOrUpdatedBy);
        pushModel.setUpdatedTime(new Date());
        pushModel.setPushSource(pushCreateDto.getPushSource());
        pushModel.setPushType(pushCreateDto.getPushType());
        pushModel.setJumpTo(pushCreateDto.getJumpTo());
        pushMapper.update(pushModel);
        return pushModel.getId();
    }

}
