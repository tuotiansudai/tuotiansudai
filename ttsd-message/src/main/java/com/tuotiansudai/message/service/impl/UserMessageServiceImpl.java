package com.tuotiansudai.message.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
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
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
        this.generateUserMessages(loginName);

        long count = userMessageMapper.countMessagesByLoginName(loginName, Lists.newArrayList(MessageChannel.WEBSITE));
        pageSize = pageSize < 1 ? 10 : pageSize;
        int totalPage = (int) (count > 0 && count % pageSize == 0 ? count / pageSize : count / pageSize + 1);
        index = index < 1 ? 1 : Ints.min(index, totalPage);
        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(loginName, Lists.newArrayList(MessageChannel.WEBSITE), (index - 1) * pageSize, pageSize);
        for (UserMessageModel userMessageModel : userMessageModels) {
            if (Strings.isNullOrEmpty(userMessageModel.getContent())) {
                userMessageModel.setRead(true);
                userMessageModel.setReadTime(new Date());
                userMessageMapper.update(userMessageModel);
            }
        }

        List<UserMessagePaginationItemDto> records = Lists.transform(userMessageModels, new Function<UserMessageModel, UserMessagePaginationItemDto>() {
            @Override
            public UserMessagePaginationItemDto apply(UserMessageModel input) {
                return new UserMessagePaginationItemDto(input, messageMapper.findById(input.getMessageId()).getType());
            }
        });

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
        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(loginName, null, null, null);
        for (UserMessageModel userMessageModel : userMessageModels) {
            if (!userMessageModel.isRead()) {
                ((UserMessageService) AopContext.currentProxy()).readMessage(userMessageModel.getId());
            }
        }
        return true;
    }

    @Override
    public long getUnreadMessageCount(String loginName) {
        List<MessageModel> unreadManualMessages = getUnreadManualMessages(loginName);
        long unreadCount = userMessageMapper.countUnreadMessagesByLoginName(loginName, Lists.newArrayList(MessageChannel.WEBSITE));
        return unreadManualMessages.size() + unreadCount;
    }

    @Override
    @Transactional
    public void generateUserMessages(String loginName) {
        userMapper.lockByLoginName(loginName);
        List<MessageModel> unreadManualMessages = getUnreadManualMessages(loginName);
        for (MessageModel message : unreadManualMessages) {
            userMessageMapper.create(new UserMessageModel(message.getId(),
                    loginName,
                    message.getTitle(),
                    message.getTemplate()));
        }
    }

    private List<MessageModel> getUnreadManualMessages(String loginName) {
        List<MessageModel> messages = this.messageMapper.findAssignableManualMessages(loginName);
        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(loginName, null, null, null);

        List<MessageModel> unreadManualMessages = Lists.newArrayList();
        for (final MessageModel message : messages) {
            Optional<UserMessageModel> userMessageModelOptional = Iterators.tryFind(userMessageModels.iterator(), new Predicate<UserMessageModel>() {
                @Override
                public boolean apply(UserMessageModel input) {
                    return input.getMessageId() == message.getId();
                }
            });

            if (!userMessageModelOptional.isPresent() && messageUserGroupDecisionManager.decide(loginName, message.getId())) {
                unreadManualMessages.add(message);
            }
        }

        return unreadManualMessages;
    }
}
