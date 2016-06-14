package com.tuotiansudai.message.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.message.dto.UserMessagePaginationItemDto;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageType;
import com.tuotiansudai.message.service.MessageService;
import com.tuotiansudai.message.util.MessageUserGroupDecisionManager;
import com.tuotiansudai.repository.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private MessageUserGroupDecisionManager messageUserGroupDecisionManager;

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
    public List<MessageModel> findMessageList(String title, MessageStatus messageStatus, String createdBy, MessageType messageType, int index, int pageSize) {
        return messageMapper.findMessageList(title, messageStatus, createdBy, messageType, (index-1)*pageSize, pageSize);
    }

    @Override
    public long findMessageCount(String title, MessageStatus messageStatus, String createdBy, MessageType messageType){
        return messageMapper.findMessageCount(title, messageStatus, createdBy, messageType);
    }
}
