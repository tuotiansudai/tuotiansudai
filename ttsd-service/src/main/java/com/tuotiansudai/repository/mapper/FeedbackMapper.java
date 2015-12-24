package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.FeedbackModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackMapper {
    void create(FeedbackModel model);

    List<FeedbackModel> findAll(@Param("loginName") String loginName,
                 @Param("rowIndex") int rowIndex,
                 @Param("rowLimit") int rowLimit);

    long findAllCount(@Param("loginName") String loginName);
}
