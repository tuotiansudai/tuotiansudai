package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.FeedbackMapper;
import com.tuotiansudai.repository.model.FeedbackModel;
import com.tuotiansudai.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public FeedbackModel create(String loginName, String content) {
        FeedbackModel feedbackModel = new FeedbackModel();
        feedbackModel.setLoginName(loginName);
        feedbackModel.setContent(content);
        feedbackModel.setCreatedTime(new Date());
        feedbackMapper.create(feedbackModel);
        return feedbackModel;
    }

    @Override
    public BasePaginationDataDto<FeedbackModel> getFeedbackPagination(String loginName, int index, int pageSize) {
        int rowIndex = (index - 1) * pageSize;
        int rowLimit = pageSize;
        List<FeedbackModel> feedbackModelList = feedbackMapper.findAll(loginName, rowIndex, rowLimit);
        long feedbackCount = feedbackMapper.findAllCount(loginName);
        BasePaginationDataDto<FeedbackModel> feedbackPagination = new BasePaginationDataDto<>(index, pageSize, feedbackCount, feedbackModelList);
        return feedbackPagination;
    }
}
