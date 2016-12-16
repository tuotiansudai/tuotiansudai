package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.FeedbackModel;
import com.tuotiansudai.repository.model.FeedbackType;
import com.tuotiansudai.repository.model.Source;

public interface FeedbackService {

    FeedbackModel create(String loginName, Source source, FeedbackType type, String content,String contract);

}
