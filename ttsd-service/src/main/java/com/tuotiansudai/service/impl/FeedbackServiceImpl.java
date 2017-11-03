package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.FeedbackMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public FeedbackModel create(String loginName, Source source, FeedbackType type, String content,String contact) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        FeedbackModel feedbackModel = new FeedbackModel();
        feedbackModel.setLoginName(loginName);
        feedbackModel.setContact(userModel != null ? userModel.getMobile():contact);
        feedbackModel.setSource(source);
        feedbackModel.setType(type == null ? FeedbackType.opinion : type);
        feedbackModel.setContent(content);
        feedbackModel.setCreatedTime(new Date());
        feedbackModel.setStatus(ProcessStatus.NOT_DONE);
        feedbackMapper.create(feedbackModel);
        return feedbackModel;
    }
}
