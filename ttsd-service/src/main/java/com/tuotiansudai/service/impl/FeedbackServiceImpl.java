package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.FeedbackMapper;
import com.tuotiansudai.repository.model.FeedbackModel;
import com.tuotiansudai.repository.model.FeedbackType;
import com.tuotiansudai.repository.model.ProcessStatus;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.FeedbackService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public FeedbackModel create(String loginName, String contact, Source source, FeedbackType type, String content) {
        FeedbackModel feedbackModel = new FeedbackModel();
        feedbackModel.setLoginName(loginName);
        feedbackModel.setContact(contact);
        feedbackModel.setSource(source);
        feedbackModel.setType(type == null ? FeedbackType.opinion : type);
        feedbackModel.setContent(content);
        feedbackModel.setCreatedTime(new Date());
        feedbackModel.setStatus(ProcessStatus.NOT_DONE);
        feedbackMapper.create(feedbackModel);
        return feedbackModel;
    }

    @Override
    public BasePaginationDataDto<FeedbackModel> getFeedbackPagination(String loginName, Source source, FeedbackType type, ProcessStatus status, Date startTime, Date endTime, int index, int pageSize) {
        int rowIndex = (index - 1) * pageSize;
        int rowLimit = pageSize;
        List<FeedbackModel> feedbackModelList = feedbackMapper.findAll(loginName, source, type, status, startTime, new DateTime(endTime).plusDays(1).toDate(), rowIndex, rowLimit);
        long feedbackCount = feedbackMapper.findAllCount(loginName, source, type, status, startTime, endTime);
        BasePaginationDataDto<FeedbackModel> feedbackPagination = new BasePaginationDataDto<>(index, pageSize, feedbackCount, feedbackModelList);
        return feedbackPagination;
    }

    @Override
    public void updateStatus(long feedbackId, ProcessStatus status) {
        feedbackMapper.updateStatus(feedbackId, status);
    }
}
