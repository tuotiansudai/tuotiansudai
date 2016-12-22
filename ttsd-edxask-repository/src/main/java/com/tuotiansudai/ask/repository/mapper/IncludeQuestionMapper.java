package com.tuotiansudai.ask.repository.mapper;

import com.tuotiansudai.ask.repository.model.IncludeQuestionModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncludeQuestionMapper {

    long create(IncludeQuestionModel includeQuestionModel);

    IncludeQuestionModel findById(@Param(value = "id") long id);

    List<IncludeQuestionModel> findAllIncludeQuestions(@Param(value = "index") int index,
                                                       @Param(value = "pageSize") int pageSize);
    long countAllIncludeQuestions();
}
