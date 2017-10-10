package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Strings;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppUserMessageService;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.enums.MessageType;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.MessageChannel;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.message.service.UserMessageService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RedisWrapperClient;
import javafx.event.EventType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MobileAppUserMessageServiceImpl implements MobileAppUserMessageService {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private UserMessageService userMessageServices;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private MessageMapper messageMapper;

    public static final String UNREAD_MESSAGE_COUNT_ID_KEY = "app:unread:message:count:ids:{0}";

    @Autowired
    private PageValidUtils pageValidUtils;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Override
    public BaseResponseDto<UserMessageResponseDataDto> getUserMessages(UserMessagesRequestDto requestDto) {
        String loginName = LoginUserInfo.getLoginName();
        String mobile = LoginUserInfo.getMobile();
        userMessageServices.generateUserMessages(loginName, mobile, MessageChannel.APP_MESSAGE);
        UserMessageResponseDataDto messageDataDto = fillMessageDataDto(loginName, requestDto.getIndex(), requestDto.getPageSize());
        BaseResponseDto<UserMessageResponseDataDto> responseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
        responseDto.setData(messageDataDto);
        return responseDto;
    }

    @Override
    public BaseResponseDto<MobileAppUnreadMessageCount> getUnreadMessageCount(BaseParamDto baseParamDto) {
        String loginName = LoginUserInfo.getLoginName();
        String mobile = LoginUserInfo.getMobile();
        long unreadMessageCount = userMessageServices.getUnreadMessageCount(loginName, mobile, MessageChannel.APP_MESSAGE);
        boolean existUnreadMessage = existUnreadMessage(loginName, unreadMessageCount);
        MobileAppUnreadMessageCount messageCount = new MobileAppUnreadMessageCount(unreadMessageCount, existUnreadMessage);
        BaseResponseDto<MobileAppUnreadMessageCount> responseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
        responseDto.setData(messageCount);
        return responseDto;
    }

    private UserMessageResponseDataDto fillMessageDataDto(String loginName, int index, int pageSize) {
        pageSize = pageValidUtils.validPageSizeLimit(pageSize);
        long totalCount = userMessageMapper.countMessagesByLoginName(loginName, MessageChannel.APP_MESSAGE);
        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(loginName, MessageChannel.APP_MESSAGE, (index - 1) * pageSize, pageSize);
        List<UserMessageDto> userMessages = userMessageModels.stream().map(userMessageModel -> {
            UserMessageDto userMessageDto = new UserMessageDto(userMessageModel);

            MessageModel messageModel = messageMapper.findById(userMessageModel.getMessageId());
            userMessageDto.setMessageType(messageModel.getMessageCategory() != null ? messageModel.getMessageCategory().getDescription() : "");
            if (messageModel.getType().equals(MessageType.EVENT)) {
                userMessageDto.setContent(userMessageModel.getTitle());
            } else if (messageModel.getType().equals(MessageType.MANUAL)) {
                if(Strings.isNullOrEmpty(messageModel.getTemplateTxt())) {
                    userMessageDto.setContent(messageModel.getTemplate());
                } else {
                    userMessageDto.setContent(messageModel.getTemplateTxt());
                }
            }

            return userMessageDto;
        }).collect(Collectors.toList());

        return new UserMessageResponseDataDto(index, pageSize, totalCount, userMessages);
    }

    private boolean existUnreadMessage(String loginName, long currentUnreadMessageCount) {
        String unreadMessageKey = MessageFormat.format(UNREAD_MESSAGE_COUNT_ID_KEY, loginName);
        String unreadMessageCountValue = redisWrapperClient.get(unreadMessageKey);

        long lastUnreadMessageCount = StringUtils.isEmpty(unreadMessageCountValue) ? 0 : Long.parseLong(unreadMessageCountValue);

        if (lastUnreadMessageCount == currentUnreadMessageCount) {
            return false;
        } else {
            redisWrapperClient.set(unreadMessageKey, String.valueOf(currentUnreadMessageCount));
            return true;
        }
    }

    @Override
    public BaseResponseDto updateReadMessage(String userMessageId) {
        BaseResponseDto baseDto = new BaseResponseDto();
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        userMessageServices.readMessage(Long.parseLong(userMessageId));
        return baseDto;
    }

    @Override
    public UserMessageViewDto getUserMessageModelById(long userMessageId) {
        UserMessageModel userMessageModel = userMessageMapper.findById(userMessageId);
        if(null == userMessageModel) {
            return new UserMessageViewDto(userMessageId, null, null, null, null);
        }
        userMessageServices.readMessage(userMessageId);
        MessageModel messageModel = messageMapper.findById(userMessageModel.getMessageId());

        return new UserMessageViewDto(userMessageModel.getId(), userMessageModel.getTitle(), userMessageModel.getContent(), userMessageModel.getCreatedTime(), getPath(userMessageModel.getBusinessId(), messageModel));
    }

    public String getPath(Long businessId, MessageModel messageModel){
        String path = messageModel.getAppUrl() == null ? null : messageModel.getAppUrl().getPath();

        if (messageModel.getType().equals(MessageType.EVENT) && businessId != null && !Strings.isNullOrEmpty(path)) {
            long loanId = 0;
            switch (messageModel.getEventType()) {
                case INVEST_SUCCESS:
                    loanId = (businessId == null || investMapper.findById(businessId) == null) ? 0 : investMapper.findById(businessId).getLoanId();
                    path = loanId == 0 ? AppUrl.MY_INVEST_RAISING.getPath() : path;
                    break;
                case TRANSFER_SUCCESS:
                case TRANSFER_FAIL:
                    loanId = (businessId == null || transferApplicationMapper.findById(businessId) == null) ? 0 : transferApplicationMapper.findById(businessId).getLoanId();
                    path = loanId == 0 ? AppUrl.MY_INVEST_FINISH.getPath() : path;
                    break;
                case LOAN_OUT_SUCCESS:
                    loanId = businessId;
                    path = loanId == 0 ? AppUrl.MY_INVEST_REPAYING.getPath() : path;
                    break;
                case REPAY_SUCCESS:
                    loanId = (businessId == null || investRepayMapper.findById(businessId) == null) ? 0 : investMapper.findById(investRepayMapper.findById(businessId).getInvestId()).getLoanId();
                    path = loanId == 0 ? AppUrl.MY_INVEST_REPAYING.getPath() : path;
                    break;
                case ADVANCED_REPAY:
                    loanId = (businessId == null || investRepayMapper.findById(businessId) == null) ? 0 : investMapper.findById(investRepayMapper.findById(businessId).getInvestId()).getLoanId();
                    path = loanId == 0 ? AppUrl.MY_INVEST_FINISH.getPath() : path;
                    break;
            }
            path = loanId == 0 ? path : MessageFormat.format(path, loanId);
        }
        return path;
    }

    @Override
    public BaseResponseDto readAll(BaseParamDto baseParamDto) {
        String loginName = LoginUserInfo.getLoginName();
        userMessageServices.readAll(loginName, MessageChannel.APP_MESSAGE);
        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
    }
}
