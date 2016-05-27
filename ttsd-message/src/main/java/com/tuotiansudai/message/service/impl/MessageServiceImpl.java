package com.tuotiansudai.message.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.message.dto.MessageDto;
import com.tuotiansudai.message.dto.UserMessagePaginationItemDto;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageUserGroup;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.message.service.MessageService;
import com.tuotiansudai.message.util.MessageUserGroupDecisionManager;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.SerializeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class MessageServiceImpl implements MessageService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private MessageUserGroupDecisionManager messageUserGroupDecisionManager;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private IdGenerator idGenerator;

    final private static String redisMessageReceivers = "message:message:receivers";
    final private static String redisMessageDetails = "message:message:details";

    @Override
    @Transactional
    public BasePaginationDataDto<UserMessagePaginationItemDto> getUserMessages(String loginName, int index, int pageSize) {
        this.generateUserMessages(loginName);
        long count = userMessageMapper.countMessagesByLoginName(loginName);
        pageSize = pageSize < 1 ? 10 : pageSize;
        int totalPage = (int) (count % pageSize == 0 ? count / pageSize : count / pageSize + 1);
        index = index < 1 ? 1 : Ints.min(index, totalPage);
        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(loginName, (index - 1) * pageSize, pageSize);

        List<UserMessagePaginationItemDto> records = Lists.transform(userMessageModels, new Function<UserMessageModel, UserMessagePaginationItemDto>() {
            @Override
            public UserMessagePaginationItemDto apply(UserMessageModel input) {
                return new UserMessagePaginationItemDto(input);
            }
        });

        return new BasePaginationDataDto<>(index, pageSize, count, records);
    }

    private void generateUserMessages(String loginName) {
        userMapper.lockByLoginName(loginName);
        List<MessageModel> messages = this.messageMapper.findAssignableManualMessages(loginName);
        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(loginName, null, null);
        for (final MessageModel message : messages) {
            Optional<UserMessageModel> userMessageModelOptional = Iterators.tryFind(userMessageModels.iterator(), new Predicate<UserMessageModel>() {
                @Override
                public boolean apply(UserMessageModel input) {
                    return input.getMessageId() == message.getId();
                }
            });

            if (!userMessageModelOptional.isPresent() && messageUserGroupDecisionManager.decide(loginName, message.getId())) {
                userMessageMapper.create(new UserMessageModel(message.getId(), loginName, message.getTitle(), message.getTemplate()));
            }
        }
    }

    @Override
    public void createManualMessage(MessageDto messageDto, long importUsersId) {
        if(messageDto.getUserGroups().contains(MessageUserGroup.IMPORT_USER)) {
            messageDto.setId(importUsersId);
        } else {
            messageDto.setId(idGenerator.generate());
        }
        redisWrapperClient.hsetSeri(redisMessageDetails, String.valueOf(messageDto.getId()), messageDto);
    }

    @Override
    public void editManualMessage(MessageDto messageDto, long importUsersId) {
        long originMessageId = messageDto.getId();
        if(messageDto.getUserGroups().contains(MessageUserGroup.IMPORT_USER)) {
            messageDto.setId(importUsersId);
            redisWrapperClient.hdelSeri(redisMessageDetails, String.valueOf(originMessageId));
            redisWrapperClient.hsetSeri(redisMessageDetails, String.valueOf(messageDto.getId()), messageDto);
        }
        else {
            redisWrapperClient.hsetSeri(redisMessageDetails, String.valueOf(originMessageId), messageDto);
        }
    }

    @Override
    public long createImportReceivers(List<String> receivers) {
        long messageId = idGenerator.generate();
        redisWrapperClient.hsetSeri(redisMessageReceivers, String.valueOf(messageId), receivers);
        return messageId;
    }

    @Override
    public MessageDto getMessageByMessageId(long messageId) {
        return (MessageDto)redisWrapperClient.hgetSeri(redisMessageDetails, String.valueOf(messageId));
    }

    private List<MessageDto> findMessagesFromRedis(String title, MessageStatus messageStatus, String creator) {
        Map<byte[], byte[]> map = redisWrapperClient.hgetAllSeri(redisMessageDetails);
        List<MessageDto> results = new ArrayList<>();
        for(Map.Entry<byte[], byte[]> entry : map.entrySet()) {
            MessageDto messageDto = (MessageDto)SerializeUtil.deserialize(entry.getValue());
            if(null != title && !title.equals(messageDto.getTitle())) {
                continue;
            }
            if(null != messageStatus && !messageStatus.equals(messageDto.getStatus())) {
                continue;
            }
            if(null != creator && !creator.equals(messageDto.getCreatedBy())) {
                continue;
            }
            results.add(messageDto);
        }
        return results;
    }

    @Override
    public BasePaginationDataDto<MessageDto> getManualMessageList(String title, MessageStatus messageStatus, String creator, int index, int pageSize) {
        List<MessageDto> messageDtos = findMessagesFromRedis(title, messageStatus, creator);
        Collections.sort(messageDtos, new Comparator<MessageDto>() {
            @Override
            public int compare(MessageDto o1, MessageDto o2) {
                return o2.getCreatedTime().after(o1.getCreatedTime()) ? 1 : -1;
            }
        });

        List<MessageDto> results = new ArrayList<>();
        for (int startIndex = (index - 1) * pageSize,
             endIndex = index * pageSize <= messageDtos.size() ? index * pageSize : messageDtos.size();
             startIndex < endIndex; ++startIndex) {
            results.add(messageDtos.get(startIndex));
        }

        return new BasePaginationDataDto<>(index, pageSize, messageDtos.size(), results);
    }

    public BaseDto<BaseDataDto> rejectManualMessage(long messageId) {
        if(!redisWrapperClient.hexists(redisMessageDetails, String.valueOf(messageId))) {
            return new BaseDto<>(new BaseDataDto(false, "messageId not existed!"));
        }
        MessageDto messageDto = (MessageDto)redisWrapperClient.hgetSeri(redisMessageDetails, String.valueOf(messageId));
        if(MessageStatus.TO_APPROVE.equals(messageDto.getStatus())) {
            messageDto.setStatus(MessageStatus.REJECTION);
            redisWrapperClient.hsetSeri(redisMessageDetails, String.valueOf(messageId), messageDto);
            return new BaseDto<>(new BaseDataDto(true));
        }
        return new BaseDto<>(new BaseDataDto(false, "message state is not TO_APPROVE"));
    }

    public BaseDto<BaseDataDto> approveManualMessage(long messageId) {
        if(!redisWrapperClient.hexists(redisMessageDetails, String.valueOf(messageId))) {
            return new BaseDto<>(new BaseDataDto(false, "messageId not existed!"));
        }
        MessageDto messageDto = (MessageDto)redisWrapperClient.hgetSeri(redisMessageDetails, String.valueOf(messageId));
        if(MessageStatus.TO_APPROVE.equals(messageDto.getStatus())) {
            //TODO:需要添加实际发送部分
            messageDto.setStatus(MessageStatus.APPROVED);
            redisWrapperClient.hsetSeri(redisMessageDetails, String.valueOf(messageId), messageDto);
            return new BaseDto<>(new BaseDataDto(true));
        }
        return new BaseDto<>(new BaseDataDto(false, "message state is not TO_APPROVE"));
    }

    public BaseDto<BaseDataDto> deleteManualMessage(long messageId) {
        if(!redisWrapperClient.hexists(redisMessageDetails, String.valueOf(messageId))) {
            return new BaseDto<>(new BaseDataDto(false, "messageId not existed!"));
        }
        redisWrapperClient.hdelSeri(redisMessageDetails, String.valueOf(messageId));
        return new BaseDto<>(new BaseDataDto(true));
    }

    public long getMessageReceiverCount(long messageId) {
        if(!redisWrapperClient.hexists(redisMessageDetails, String.valueOf(messageId))) {
            return 0L;
        }
        MessageDto messageDto = (MessageDto)redisWrapperClient.hgetSeri(redisMessageDetails, String.valueOf(messageId));
        if(messageDto.getUserGroups().contains(MessageUserGroup.IMPORT_USER)) {
            List<String> importUsers = (List<String>)redisWrapperClient.hgetSeri(redisMessageReceivers, String.valueOf(messageDto.getId()));
            return importUsers.size();
        }
        //TODO:需要补全查询其他情形下数据的功能
        return -1L;
    }
}
