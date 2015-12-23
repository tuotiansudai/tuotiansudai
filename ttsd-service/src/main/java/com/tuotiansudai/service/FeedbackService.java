package com.tuotiansudai.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.FeedbackModel;

public interface FeedbackService {
    FeedbackModel create(String loginName, String content);

    BasePaginationDataDto<FeedbackModel> getFeedbackPagination(String loginName, int index, int pageSize);
}
