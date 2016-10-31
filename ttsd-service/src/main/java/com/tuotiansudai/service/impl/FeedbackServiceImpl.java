package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.FeedbackMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.FeedbackService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    @Override
    public BasePaginationDataDto<FeedbackModel> getFeedbackPagination(String mobile, Source source, FeedbackType type, ProcessStatus status, Date startTime, Date endTime, int index, int pageSize) {
        int rowIndex = (index - 1) * pageSize;
        int rowLimit = pageSize;
        List<FeedbackModel> feedbackModelList = feedbackMapper.findAll(mobile, source, type, status, startTime, new DateTime(endTime).plusDays(1).toDate(), rowIndex, rowLimit);
        long feedbackCount = feedbackMapper.findAllCount(mobile, source, type, status, startTime, endTime);
        BasePaginationDataDto<FeedbackModel> feedbackPagination = new BasePaginationDataDto<>(index, pageSize, feedbackCount, feedbackModelList);
        return feedbackPagination;
    }

    @Override
    public void updateStatus(long feedbackId, ProcessStatus status) {
        feedbackMapper.updateStatus(feedbackId, status);
    }

    @Override
    public void updateRemark(FeedbackModel feedbackModel) {
        feedbackMapper.updateRemark(feedbackModel);
    }

    @Override
    public FeedbackModel findById(long feedbackId) {
        return feedbackMapper.findById(feedbackId);
    }


}
