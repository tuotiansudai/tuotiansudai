package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppUserMessageService;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.MessageType;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.MessageCategory;
import com.tuotiansudai.message.repository.model.MessageChannel;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.message.service.UserMessageService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
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

    @Autowired
    private LoanRepayMapper loanRepayMapper;

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
            long investId = 0;
            switch (messageModel.getEventType()) {
                case INVEST_SUCCESS:
                case LOAN_OUT_SUCCESS:
                    investId = businessId;
                    path = investMapper.findById(investId) == null ? AppUrl.MY_INVEST_REPAYING.getPath() : path;
                    break;
                case TRANSFER_SUCCESS:
                case TRANSFER_FAIL:
                    investId = transferApplicationMapper.findById(businessId).getTransferInvestId();
                    break;
                case REPAY_SUCCESS:
                case ADVANCED_REPAY:
                    investId = investRepayMapper.findById(businessId).getInvestId();
                    break;
            }
            InvestModel investModel = investMapper.findById(investId);
            LoanModel loanModel = investModel == null ? null : loanMapper.findById(investModel.getLoanId());
            List<LoanRepayModel> loanRepayModels = loanModel==null? null: loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId()).stream().filter(i->i.getStatus()== RepayStatus.REPAYING).collect(Collectors.toList());
            boolean isTransfer = CollectionUtils.isNotEmpty(loanRepayModels) ? false : !loanRepayModels.get(loanRepayModels.size() - 1).getRepayDate().after(DateTime.now().plusDays(6).toDate());

            List<TransferApplicationModel> transferApplicationModels = investModel == null ? null : transferApplicationMapper.findByTransferInvestId(investId, null);
            TransferApplicationModel transferApplicationModel = CollectionUtils.isNotEmpty(transferApplicationModels) ? transferApplicationModels.get(transferApplicationModels.size() - 1) : null;
            path = investModel == null ? path : MessageFormat.format(path,
                    investModel.getTransferInvestId() == null ? "0" : "1",
                    loanModel.getStatus(),
                    String.valueOf(investModel.getId()),
                    transferApplicationModel == null ? "0" : String.valueOf(transferApplicationModel.getId()),
                    isTransfer ? TransferStatus.NONTRANSFERABLE : transferApplicationModel == null ? investModel.getTransferStatus() : transferApplicationModel.getApplicationTime().before(DateTime.now().withTimeAtStartOfDay().toDate()) ?
                            investModel.getTransferStatus(): transferApplicationModel.getStatus());
        }
        return messageModel.getMessageCategory().equals(MessageCategory.NOTIFY) ? null : path;
    }

    @Override
    public BaseResponseDto readAll(BaseParamDto baseParamDto) {
        String loginName = LoginUserInfo.getLoginName();
        userMessageServices.readAll(loginName, MessageChannel.APP_MESSAGE);
        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
    }
}
