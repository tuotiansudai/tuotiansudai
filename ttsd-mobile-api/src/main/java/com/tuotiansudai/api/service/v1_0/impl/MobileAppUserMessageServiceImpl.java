package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppUserMessageService;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.MessageChannel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.message.service.UserMessageService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppUserMessageServiceImpl implements MobileAppUserMessageService {

    @Autowired
    private UserMessageService userMessageServices;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Override
    public BaseResponseDto getUserMessages(UserMessagesRequestDto requestDto) {
        String loginName = requestDto.getBaseParam().getUserId();
        userMessageServices.generateUserMessages(loginName);
        Integer index = (requestDto.getIndex() == null || requestDto.getIndex() <= 0) ? 0 : requestDto.getIndex();
        Integer pageSize = (requestDto.getPageSize() == null || requestDto.getPageSize() <= 0) ? 10 : requestDto.getPageSize();
        UserMessageResponseDataDto messageDataDto = fillMessageDataDto(loginName, index, pageSize);
        BaseResponseDto responseDto = new BaseResponseDto<>();
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        responseDto.setData(messageDataDto);
        return responseDto;
    }

    private UserMessageResponseDataDto fillMessageDataDto(String loginName, Integer index, Integer pageSize) {
        UserMessageResponseDataDto responseDataDto = new UserMessageResponseDataDto();
        List<UserMessageModel> userMessageModels = userMessageMapper.findMessagesByLoginName(loginName, MessageChannel.APP, index, pageSize);
        long totalCount = userMessageMapper.countMessagesByLoginName(loginName, MessageChannel.APP);
        List<UserMessageDto> userMessages = CollectionUtils.isEmpty(userMessageModels) ? new ArrayList<UserMessageDto>() :
                Lists.transform(userMessageModels, new Function<UserMessageModel, UserMessageDto>() {
                    @Override
                    public UserMessageDto apply(UserMessageModel model) {
                        return new UserMessageDto(model);
                    }
                });
        responseDataDto.setIndex(index);
        responseDataDto.setPageSize(pageSize);
        responseDataDto.setTotalCount(totalCount);
        responseDataDto.setData(userMessages);
        return responseDataDto;
    }

}
