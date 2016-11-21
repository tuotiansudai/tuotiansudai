package com.tuotiansudai.message.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.message.dto.UserMessagePaginationItemDto;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.MessageChannel;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.message.service.UserMessageService;
import com.tuotiansudai.message.util.MessageUserGroupDecisionManager;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private MessageUserGroupDecisionManager messageUserGroupDecisionManager;

    @Override
    public BasePaginationDataDto<UserMessagePaginationItemDto> getUserMessages(String loginName, int index, int pageSize) {
        ((UserMessageService) AopContext.currentProxy()).generateUserMessages(loginName, MessageChannel.WEBSITE);

        pageSize = pageSize < 1 ? 10 : pageSize;
        long count = userMessageMapper.countMessagesByLoginName(loginName, MessageChannel.WEBSITE);
        int offset = PaginationUtil.calculateOffset(index, pageSize, count);

        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(loginName, MessageChannel.WEBSITE, offset, pageSize);
        for (UserMessageModel userMessageModel : userMessageModels) {
            if (Strings.isNullOrEmpty(userMessageModel.getContent())) {
                userMessageModel.setRead(true);
                userMessageModel.setReadTime(new Date());
                userMessageMapper.update(userMessageModel);
            }
        }

        List<UserMessagePaginationItemDto> records = userMessageModels.stream().map(userMessageModel -> {
            MessageModel messageModel = messageMapper.findByIdBesidesDeleted(userMessageModel.getMessageId());
            return new UserMessagePaginationItemDto(userMessageModel, messageModel.getType(), messageModel.getMessageCategory());
        }).collect(Collectors.toList());

        BasePaginationDataDto<UserMessagePaginationItemDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, records);
        dataDto.setStatus(true);

        return dataDto;
    }

    @Override
    @Transactional
    public UserMessageModel readMessage(long userMessageId) {
        UserMessageModel userMessageModel = userMessageMapper.findById(userMessageId);
        if (userMessageModel != null && !userMessageModel.isRead()) {
            MessageModel messageModel = messageMapper.lockById(userMessageModel.getMessageId());
            messageModel.setReadCount(messageModel.getReadCount() + 1);
            messageMapper.update(messageModel);
            userMessageModel.setRead(true);
            userMessageModel.setReadTime(new Date());
            userMessageMapper.update(userMessageModel);
        }

        return userMessageModel;
    }

    @Override
    public boolean readAll(String loginName) {
        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(loginName, MessageChannel.WEBSITE, null, null);
        for (UserMessageModel userMessageModel : userMessageModels) {
            if (!userMessageModel.isRead()) {
                ((UserMessageService) AopContext.currentProxy()).readMessage(userMessageModel.getId());
            }
        }
        return true;
    }

    @Override
    public long getUnreadMessageCount(String loginName, MessageChannel messageChannel) {
        List<MessageModel> unreadManualMessages = getUnreadManualMessages(loginName, messageChannel);
        long unreadCount = userMessageMapper.countUnreadMessagesByLoginName(loginName, messageChannel);
        return unreadManualMessages.size() + unreadCount;
    }

    @Override
    @Transactional
    public void generateUserMessages(String loginName, MessageChannel messageChannel) {
        userMapper.lockByLoginName(loginName);
        List<MessageModel> unreadManualMessages = getUnreadManualMessages(loginName, messageChannel);
        for (MessageModel message : unreadManualMessages) {
            userMessageMapper.create(new UserMessageModel(message.getId(),
                    loginName,
                    message.getTitle(),
                    message.getTitle(),
                    message.getTemplate()));
        }
    }

    private List<MessageModel> getUnreadManualMessages(String loginName, final MessageChannel messageChannel) {
        List<MessageModel> messages = this.messageMapper.findAssignableManualMessages(loginName);
        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(loginName, null, null, null);

        List<MessageModel> unreadManualMessages = Lists.newArrayList();
        for (final MessageModel message : messages) {
            if (message.getChannels().contains(messageChannel)) {
                Optional<UserMessageModel> userMessageModelOptional = Iterators.tryFind(userMessageModels.iterator(), new Predicate<UserMessageModel>() {
                    @Override
                    public boolean apply(UserMessageModel model) {
                        return model.getMessageId() == message.getId();
                    }
                });
                if (!userMessageModelOptional.isPresent() && messageUserGroupDecisionManager.decide(loginName, message.getId())) {
                    unreadManualMessages.add(message);
                }
            }
        }

        return unreadManualMessages;
    }
}
