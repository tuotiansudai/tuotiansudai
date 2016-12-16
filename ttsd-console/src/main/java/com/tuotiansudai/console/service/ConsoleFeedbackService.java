package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.FeedbackMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.FeedbackService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConsoleFeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    public BasePaginationDataDto<FeedbackModel> getFeedbackPagination(String mobile, Source source, FeedbackType type, ProcessStatus status, Date startTime, Date endTime, int index, int pageSize) {
        int rowIndex = (index - 1) * pageSize;
        int rowLimit = pageSize;
        List<FeedbackModel> feedbackModelList = feedbackMapper.findAll(mobile, source, type, status, startTime, new DateTime(endTime).plusDays(1).toDate(), rowIndex, rowLimit);
        long feedbackCount = feedbackMapper.findAllCount(mobile, source, type, status, startTime, endTime);
        BasePaginationDataDto<FeedbackModel> feedbackPagination = new BasePaginationDataDto<>(index, pageSize, feedbackCount, feedbackModelList);
        return feedbackPagination;
    }

    public void updateStatus(long feedbackId, ProcessStatus status) {
        feedbackMapper.updateStatus(feedbackId, status);
    }

    public void updateRemark(FeedbackModel feedbackModel) {
        feedbackMapper.updateRemark(feedbackModel);
    }

    public FeedbackModel findById(long feedbackId) {
        return feedbackMapper.findById(feedbackId);
    }
}
