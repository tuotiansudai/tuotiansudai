package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppReadMessageService;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppReadMessageServiceImpl implements MobileAppReadMessageService{

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Override
    public BaseResponseDto updateReadMessage(String messageId) {
        BaseResponseDto baseDto = new BaseResponseDto();
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        userMessageMapper.updateReadAndReadTimeById(Long.parseLong(messageId),true, DateTime.now().toDate());
        return baseDto;
    }
}
