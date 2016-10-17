package com.tuotiansudai.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.enums.Source;
import com.tuotiansudai.repository.model.FeedbackModel;
import com.tuotiansudai.repository.model.FeedbackType;
import com.tuotiansudai.repository.model.FeedbackProcessStatus;

import java.util.Date;

public interface FeedbackService {
    FeedbackModel create(String loginName, Source source, FeedbackType type, String content);

    BasePaginationDataDto<FeedbackModel> getFeedbackPagination(String loginName,
                                                               Source source,
                                                               FeedbackType type,
                                                               FeedbackProcessStatus status,
                                                               Date startTime,
                                                               Date endTime,
                                                               int index,
                                                               int pageSize);

    void updateStatus(long feedbackId, FeedbackProcessStatus status);

    void updateRemark(FeedbackModel feedbackModel);

    FeedbackModel findById(long feedbackId);
}
