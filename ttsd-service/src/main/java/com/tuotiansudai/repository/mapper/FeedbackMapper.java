package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.FeedbackModel;
import com.tuotiansudai.repository.model.FeedbackType;
import com.tuotiansudai.repository.model.ProcessStatus;
import com.tuotiansudai.repository.model.Source;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FeedbackMapper {
    void create(FeedbackModel model);

    List<FeedbackModel> findAll(@Param("loginName") String loginName,
                                @Param("source") Source source,
                                @Param("type") FeedbackType type,
                                @Param("status") ProcessStatus status,
                                @Param("startTime") Date startTime,
                                @Param("endTime") Date endTime,
                                @Param("rowIndex") int rowIndex,
                                @Param("rowLimit") int rowLimit);

    long findAllCount(@Param("loginName") String loginName,
                      @Param("source") Source source,
                      @Param("type") FeedbackType type,
                      @Param("status") ProcessStatus status,
                      @Param("startTime") Date startTime,
                      @Param("endTime") Date endTime);

    void updateStatus(@Param("feedbackId") long feedbackId,
                      @Param("status") ProcessStatus status);
}
