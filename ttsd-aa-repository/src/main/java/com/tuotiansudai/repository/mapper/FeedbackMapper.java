package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.Source;
import com.tuotiansudai.repository.model.FeedbackModel;
import com.tuotiansudai.repository.model.FeedbackProcessStatus;
import com.tuotiansudai.repository.model.FeedbackType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FeedbackMapper {
    void create(FeedbackModel model);

    List<FeedbackModel> findAll(@Param("mobile") String mobile,
                                @Param("source") Source source,
                                @Param("type") FeedbackType type,
                                @Param("status") FeedbackProcessStatus status,
                                @Param("startTime") Date startTime,
                                @Param("endTime") Date endTime,
                                @Param("rowIndex") int rowIndex,
                                @Param("rowLimit") int rowLimit);

    long findAllCount(@Param("mobile") String mobile,
                      @Param("source") Source source,
                      @Param("type") FeedbackType type,
                      @Param("status") FeedbackProcessStatus status,
                      @Param("startTime") Date startTime,
                      @Param("endTime") Date endTime);

    void updateStatus(@Param("feedbackId") long feedbackId,
                      @Param("status") FeedbackProcessStatus status);

    void updateRemark(FeedbackModel feedbackModel);

    FeedbackModel findById(@Param("feedbackId") long feedbackId);

}
