package com.tuotiansudai.message.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.jpush.dto.JPushAlertDto;
import com.tuotiansudai.jpush.repository.model.JPushAlertModel;
import com.tuotiansudai.jpush.service.JPushAlertService;
import com.tuotiansudai.message.dto.MessageCreateDto;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageType;
import com.tuotiansudai.message.repository.model.MessageUserGroup;
import com.tuotiansudai.message.service.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private JPushAlertService jPushAlertService;

    public final static String redisMessageReceivers = "message:manual-message:receivers";

    private final static int EXPIRED_PERIOD = 30;

    @Override
    public long findMessageCount(String title, MessageStatus messageStatus, String createdBy, MessageType messageType) {
        return messageMapper.findMessageCount(title, messageStatus, createdBy, messageType);
    }

    private MessageCreateDto messageModelToDto(MessageModel messageModel) {
        MessageCreateDto messageCreateDto = new MessageCreateDto(messageModel);
        JPushAlertModel jPushAlertModel = jPushAlertService.findJPushAlertModelByMessageId(messageModel.getId());
        if (null != jPushAlertModel) {
            messageCreateDto.setJpush(true);
            messageCreateDto.setPushType(jPushAlertModel.getPushType());
            messageCreateDto.setPushSource(jPushAlertModel.getPushSource());
            messageCreateDto.setPushDistricts(jPushAlertModel.getPushDistricts());
        } else {
            messageCreateDto.setJpush(false);
        }
        return messageCreateDto;
    }

    @Override
    public List<MessageCreateDto> findMessageList(String title, MessageStatus messageStatus, String createdBy, MessageType messageType, int index, int pageSize) {
        List<MessageModel> messageModels = messageMapper.findMessageList(title, messageStatus, createdBy, messageType, (index - 1) * pageSize, pageSize);
        return messageModels.stream().map(this::messageModelToDto).collect(Collectors.toList());
    }

    @Override
    public long createAndEditManualMessage(MessageCreateDto messageCreateDto, long importUsersId) {
        long messageId = messageCreateDto.getId();
        if (isMessageExist(messageId)) {
            editManualMessage(messageCreateDto, importUsersId);
        } else {
            MessageModel messageModel = createManualMessage(messageCreateDto, importUsersId);
            messageId = messageModel.getId();
        }
        if (messageCreateDto.isJpush()) {
            editManualJPush(messageCreateDto, messageId);
        }
        return messageId;
    }

    private void editManualJPush(MessageCreateDto messageCreateDto, long messageId) {
        JPushAlertModel jPushAlertModel = jPushAlertService.findJPushAlertModelByMessageId(messageId);
        JPushAlertDto jPushAlertDto = messageCreateDto.getJPushAlertDto();
        jPushAlertDto.setMessageId(messageId);
        if (null != jPushAlertModel) {
            if (messageCreateDto.isJpush()) {
                jPushAlertDto.setId(String.valueOf(jPushAlertModel.getId()));
                jPushAlertService.buildJPushAlert(messageCreateDto.getCreatedBy(), jPushAlertDto);
            } else {
                jPushAlertService.delete(messageCreateDto.getUpdatedBy(), jPushAlertModel.getId());
            }
        } else {
            if (messageCreateDto.isJpush()) {
                jPushAlertService.buildJPushAlert(messageCreateDto.getCreatedBy(), jPushAlertDto);
            }
        }
    }

    @Override
    public long createImportReceivers(long oldImportUsersId, InputStream inputStream) throws IOException {
        if (redisWrapperClient.hexists(redisMessageReceivers, String.valueOf(oldImportUsersId))) {
            redisWrapperClient.hdel(redisMessageReceivers, String.valueOf(oldImportUsersId));
        }

        long importUsersId = new Date().getTime();

        List<String> importUsers = Lists.newArrayList();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while (null != (line = bufferedReader.readLine())) {
            stringBuilder.append(line);
        }

        for (String loginName : Splitter.on(',').splitToList(stringBuilder.toString())) {
            if (!StringUtils.isEmpty(loginName)) {
                importUsers.add(loginName);
            }
        }
        redisWrapperClient.hsetSeri(redisMessageReceivers, String.valueOf(importUsersId), importUsers);
        return importUsersId;
    }

    @Override
    public MessageCreateDto getMessageByMessageId(long messageId) {
        return messageModelToDto(messageMapper.findById(messageId));
    }

    @Override
    public BaseDto<BaseDataDto> rejectMessage(long messageId, String checkerName) {
        if (!isMessageExist(messageId)) {
            return new BaseDto<>(new BaseDataDto(false, "messageId not existed!"));
        }
        MessageModel messageModel = messageMapper.findById(messageId);
        if (MessageStatus.TO_APPROVE == messageModel.getStatus()) {
            messageModel.setStatus(MessageStatus.REJECTION);
            messageModel.setUpdatedTime(new Date());
            messageModel.setUpdatedBy(checkerName);
            messageMapper.update(messageModel);

            JPushAlertModel jPushAlertModel = jPushAlertService.findJPushAlertModelByMessageId(messageId);
            if (null != jPushAlertModel) {
                jPushAlertService.reject(checkerName, jPushAlertModel.getId(), null);
            }
            return new BaseDto<>(new BaseDataDto(true, null));
        } else if (MessageType.EVENT == messageModel.getType()) {
            messageModel.setStatus(MessageStatus.TO_APPROVE);
            messageModel.setUpdatedTime(new Date());
            messageModel.setUpdatedBy(checkerName);
            messageMapper.update(messageModel);

            JPushAlertModel jPushAlertModel = jPushAlertService.findJPushAlertModelByMessageId(messageId);
            if (null != jPushAlertModel) {
                jPushAlertService.reject(checkerName, jPushAlertModel.getId(), "");
            }
            return new BaseDto<>(new BaseDataDto(true, null));
        }
        return new BaseDto<>(new BaseDataDto(false, "message state is not TO_APPROVE or EVENT message"));
    }

    @Override
    public BaseDto<BaseDataDto> approveMessage(long messageId, String checkerName) {
        if (!isMessageExist(messageId)) {
            return new BaseDto<>(new BaseDataDto(false, "messageId not existed!"));
        }
        MessageModel messageModel = messageMapper.findById(messageId);
        if (MessageStatus.TO_APPROVE == messageModel.getStatus() || MessageType.EVENT == messageModel.getType()) {
            messageModel.setActivatedBy(checkerName);
            messageModel.setActivatedTime(new Date());
            messageModel.setStatus(MessageStatus.APPROVED);
            messageModel.setUpdatedTime(new Date());
            messageModel.setUpdatedBy(checkerName);
            messageMapper.update(messageModel);

            JPushAlertModel jPushAlertModel = jPushAlertService.findJPushAlertModelByMessageId(messageId);
            if (null != jPushAlertModel) {
                jPushAlertService.manualJPushAlert(jPushAlertModel.getId());
            }
            return new BaseDto<>(new BaseDataDto(true, null));
        }
        return new BaseDto<>(new BaseDataDto(false, "message state is not TO_APPROVE or EVENT message"));
    }

    @Override
    public BaseDto<BaseDataDto> deleteMessage(long messageId, String updatedBy) {
        if (!isMessageExist(messageId)) {
            return new BaseDto<>(new BaseDataDto(false, "messageId not existed!"));
        }
        messageMapper.deleteById(messageId, updatedBy, new Date());
        redisWrapperClient.hdelSeri(redisMessageReceivers, String.valueOf(messageId));

        JPushAlertModel jPushAlertModel = jPushAlertService.findJPushAlertModelByMessageId(messageId);
        if (null != jPushAlertModel) {
            jPushAlertService.delete(updatedBy, jPushAlertModel.getId());
        }
        return new BaseDto<>(new BaseDataDto(true, null));
    }

    @Override
    public boolean isMessageExist(long messageId) {
        return null != messageMapper.findById(messageId);
    }

    @SuppressWarnings(value = "unchecked")
    private MessageModel createManualMessage(MessageCreateDto messageCreateDto, long importUsersId) {
        MessageModel messageModel = messageCreateDto.getMessageModel();

        messageModel.setType(MessageType.MANUAL);
        messageModel.setStatus(MessageStatus.TO_APPROVE);
        messageModel.setReadCount(0);
        messageModel.setExpiredTime(new DateTime().plusDays(EXPIRED_PERIOD).withTimeAtStartOfDay().toDate());
        messageModel.setCreatedTime(new Date());
        messageModel.setUpdatedTime(messageModel.getCreatedTime());

        messageMapper.create(messageModel);

        if (messageCreateDto.getUserGroups().contains(MessageUserGroup.IMPORT_USER)) {
            String messageId = String.valueOf(messageModel.getId());
            List<String> importUsers = (List<String>) redisWrapperClient.hgetSeri(redisMessageReceivers, String.valueOf(importUsersId));
            redisWrapperClient.hdelSeri(redisMessageReceivers, String.valueOf(importUsersId));
            redisWrapperClient.hsetSeri(redisMessageReceivers, messageId, importUsers);
        }

        return messageModel;
    }

    @SuppressWarnings(value = "unchecked")
    private void editManualMessage(MessageCreateDto messageCreateDto, long importUsersId) {
        List<String> importUsers = (List<String>) redisWrapperClient.hgetSeri(redisMessageReceivers, String.valueOf(importUsersId));
        redisWrapperClient.hdelSeri(redisMessageReceivers, String.valueOf(importUsersId));

        MessageModel originMessageModel = messageMapper.findById(messageCreateDto.getId());
        MessageModel messageModel = messageCreateDto.getMessageModel();

        messageModel.setType(originMessageModel.getType());
        messageModel.setEventType(originMessageModel.getEventType());
        messageModel.setStatus(originMessageModel.getStatus());
        messageModel.setReadCount(originMessageModel.getReadCount());
        messageModel.setActivatedBy(originMessageModel.getActivatedBy());
        messageModel.setActivatedTime(originMessageModel.getActivatedTime());
        messageModel.setExpiredTime(originMessageModel.getExpiredTime());
        messageModel.setUpdatedTime(new Date());
        messageModel.setCreatedBy(originMessageModel.getCreatedBy());
        messageModel.setCreatedTime(originMessageModel.getCreatedTime());

        messageMapper.update(messageModel);

        if (messageCreateDto.getUserGroups().contains(MessageUserGroup.IMPORT_USER)) {
            String messageId = String.valueOf(messageCreateDto.getId());
            redisWrapperClient.hsetSeri(redisMessageReceivers, messageId, importUsers);
        }
    }
}
