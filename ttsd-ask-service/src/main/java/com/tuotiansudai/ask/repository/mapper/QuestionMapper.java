package com.tuotiansudai.ask.repository.mapper;

import com.tuotiansudai.ask.repository.model.QuestionModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionMapper {

    long create(QuestionModel questionModel);

    long update(QuestionModel questionModel);

    List<QuestionModel> findByLoginName(@Param(value = "loginName") String loginName);

    List<QuestionModel> findAllQuestions(@Param(value = "loginName") String loginName,
                                         @Param(value = "index") int index,
                                         @Param(value = "pageSize") int pageSize);

    long countAllQuestions(@Param(value = "loginName") String loginName);

    List<QuestionModel> findAllUnresolvedQuestions(@Param(value = "loginName") String loginName,
                                                   @Param(value = "index") int index,
                                                   @Param(value = "pageSize") int pageSize);

    long countAllUnresolvedQuestions(@Param(value = "loginName") String loginName);

    List<QuestionModel> findAllHotQuestions(@Param(value = "loginName") String loginName,
                                            @Param(value = "index") int index,
                                            @Param(value = "pageSize") int pageSize);
}
