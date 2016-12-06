package com.tuotiansudai.message.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.message.dto.MessageCreateDto;
import com.tuotiansudai.message.dto.MessagePaginationItemDto;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageType;
import com.tuotiansudai.message.repository.model.MessageUserGroup;
import com.tuotiansudai.message.service.MessageService;
import com.tuotiansudai.push.repository.mapper.PushAlertMapper;
import com.tuotiansudai.push.repository.model.PushAlertModel;
import com.tuotiansudai.push.service.JPushAlertNewService;
import com.tuotiansudai.push.service.PushService;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private PushAlertMapper pushAlertMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private JPushAlertNewService jPushAlertNewService;

    @Autowired
    private PushService pushService;

    public final static String MESSAGE_IMPORT_USER_TEMP_KEY = "message:manual-message:receivers:temp:{0}";

    public final static String MESSAGE_IMPORT_USER_KEY = "message:manual-message:receivers";

    @Override
    public long findMessageCount(String title, MessageStatus messageStatus, String createdBy, MessageType messageType) {
        return messageMapper.findMessageCount(title, messageStatus, createdBy, messageType);
    }

    @Override
    public List<MessagePaginationItemDto> findMessagePagination(String title, MessageStatus messageStatus, String createdBy, MessageType messageType, int index, int pageSize) {
        int offset = PaginationUtil.calculateOffset(index, pageSize, messageMapper.findMessageCount(title, messageStatus, createdBy, messageType));
        List<MessageModel> messageModels = messageMapper.findMessagePagination(title, messageStatus, createdBy, messageType, offset, pageSize);
        return messageModels.stream().map(messageModel -> new MessagePaginationItemDto(messageModel, pushAlertMapper.findById(messageModel.getPushId()))).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public long createOrUpdateManualMessage(String loginName, MessageCreateDto messageCreateDto) {
        Long messageId = messageCreateDto.getId();
        if (messageId == null) {
            Long pushId = pushService.createOrUpdate(loginName, messageCreateDto.getPush());
            return this.createManualMessage(loginName, pushId, messageCreateDto);
        }
        this.updateManualMessage(loginName, messageCreateDto);

        return messageId;
    }

    @Override
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

    @Override
    public BaseDto<BaseDataDto> approveMessage(long messageId, String approvedBy) {
        MessageModel messageModel = messageMapper.findById(messageId);
        if (messageModel == null || messageModel.getStatus() != MessageStatus.TO_APPROVE) {
            return new BaseDto<>(new BaseDataDto(false, "消息审核失败"));
        }

        messageModel.setActivatedBy(approvedBy);
        messageModel.setActivatedTime(new Date());
        messageModel.setUpdatedTime(new Date());
        messageModel.setUpdatedBy(approvedBy);
        messageModel.setStatus(MessageStatus.APPROVED);
        messageMapper.update(messageModel);

        PushAlertModel pushAlertModel = pushAlertMapper.findById(messageModel.getPushId());
        if (pushAlertMapper.findById(messageModel.getPushId()) != null && MessageType.MANUAL.equals(messageModel.getType())) {
//            sendJPush(pushAlertModel, messageModel);
        }
        return new BaseDto<>(new BaseDataDto(true));
    }

    @Override
    public BaseDto<BaseDataDto> rejectMessage(long messageId, String approvedBy) {
        MessageModel messageModel = messageMapper.findById(messageId);
        if (messageModel == null || messageModel.getStatus() != MessageStatus.TO_APPROVE) {
            return new BaseDto<>(new BaseDataDto(false, "消息审核失败"));
        }
        messageModel.setUpdatedTime(new Date());
        messageModel.setUpdatedBy(approvedBy);
        messageModel.setStatus(messageModel.getType() == MessageType.EVENT ? MessageStatus.TO_APPROVE : MessageStatus.REJECTION);
        messageMapper.update(messageModel);

        return new BaseDto<>(new BaseDataDto(true));
    }

    @Override
    public BaseDto<BaseDataDto> deleteMessage(long messageId, String updatedBy) {
        MessageModel messageModel = messageMapper.findById(messageId);
        if (messageModel == null || messageModel.getStatus() != MessageStatus.TO_APPROVE) {
            return new BaseDto<>(new BaseDataDto(false, "消息不存在"));
        }
        messageMapper.deleteById(messageId, updatedBy);
        redisWrapperClient.hdelSeri(MESSAGE_IMPORT_USER_KEY, String.valueOf(messageId));
        return new BaseDto<>(new BaseDataDto(true));
    }

    private void sendJPush(PushAlertModel pushAlertModel, MessageModel messageModel) {
        if (redisWrapperClient.hexists(MESSAGE_IMPORT_USER_KEY, String.valueOf(messageModel.getId()))) {
            List<String> loginNames = (List<String>) redisWrapperClient.hgetSeri(MESSAGE_IMPORT_USER_KEY, String.valueOf(messageModel.getId()));
            jPushAlertNewService.autoJPushBatchByLoginNames(pushAlertModel, loginNames);
        } else {
            jPushAlertNewService.autoJPushAlertSendToAll(pushAlertModel);
        }
    }

    @Override
    public MessageModel findById(long messageId) {
        return messageMapper.findById(messageId);
    }

    @Override
    public MessageCreateDto getEditMessage(long messageId) {
        MessageModel messageModel = messageMapper.findById(messageId);
        PushAlertModel pushAlertModel = pushAlertMapper.findById(messageModel.getPushId());
        return new MessageCreateDto(messageModel, pushAlertModel);
    }

    @SuppressWarnings(value = "unchecked")
    private long createManualMessage(String createdBy, Long pushId, MessageCreateDto messageCreateDto) {
        MessageModel messageModel = new MessageModel(messageCreateDto.getTitle(),
                messageCreateDto.getTemplateTxt(),
                messageCreateDto.getUserGroup(),
                messageCreateDto.getMessageCategory(),
                messageCreateDto.getChannels(),
                pushId,
                createdBy);
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
        MessageModel messageModel = messageMapper.findById(messageCreateDto.getId());

        if (messageCreateDto.getPush() == null) {
            pushService.delete(messageModel.getPushId());
        } else {
            messageModel.setPushId(pushService.createOrUpdate(updatedBy, messageCreateDto.getPush()));
        }

        messageModel.setTitle(messageCreateDto.getTitle());
        messageModel.setAppTitle(messageCreateDto.getTitle());
        messageModel.setTemplate(messageCreateDto.getTemplateTxt());
        messageModel.setTemplateTxt(messageCreateDto.getTemplateTxt());
        messageModel.setUserGroup(messageCreateDto.getUserGroup());
        messageModel.setChannels(messageCreateDto.getChannels());
        messageModel.setMessageCategory(messageCreateDto.getMessageCategory());
        messageModel.setWebUrl(messageCreateDto.getWebUrl());
        messageModel.setAppUrl(messageCreateDto.getAppUrl());
        messageModel.setUpdatedBy(updatedBy);
        messageModel.setUpdatedTime(new Date());
        messageMapper.update(messageModel);

        if (messageCreateDto.getUserGroup() == MessageUserGroup.IMPORT_USER && messageCreateDto.getImportUsersFlag() != null && messageCreateDto.getImportUsersFlag() != messageModel.getId()) {
            String messageId = String.valueOf(messageModel.getId());
            String importUsers = redisWrapperClient.get(MessageFormat.format(MESSAGE_IMPORT_USER_TEMP_KEY, String.valueOf(messageCreateDto.getImportUsersFlag())));
            redisWrapperClient.hsetSeri(MESSAGE_IMPORT_USER_KEY, messageId, Lists.newArrayList(Splitter.on(',').splitToList(importUsers).stream().filter(loginName -> !StringUtils.isEmpty(loginName.trim())).collect(Collectors.toList())));
        }
    }
}
