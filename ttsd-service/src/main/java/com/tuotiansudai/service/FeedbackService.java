package com.tuotiansudai.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.FeedbackModel;
import com.tuotiansudai.repository.model.FeedbackType;
import com.tuotiansudai.repository.model.ProcessStatus;
import com.tuotiansudai.repository.model.Source;
import org.springframework.stereotype.Service;

import java.util.Date;

public interface FeedbackService {
    FeedbackModel create(String loginName,
                         String contact,
                         Source source,
                         FeedbackType type,
                         String content);

    BasePaginationDataDto<FeedbackModel> getFeedbackPagination(String loginName,
                                                               Source source,
                                                               FeedbackType type,
                                                               ProcessStatus status,
                                                               Date startTime,
                                                               Date endTime,
                                                               int index,
                                                               int pageSize);

    void updateStatus(long feedbackId, ProcessStatus status);

    void updateRemark(FeedbackModel feedbackModel);

    FeedbackModel findById(long feedbackId);
}
