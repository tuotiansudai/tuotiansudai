package com.tuotiansudai.ask.repository.mapper;

import com.tuotiansudai.ask.repository.model.AnswerModel;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerMapper {

    long create(AnswerModel answerModel);

    long update(AnswerModel answerModel);

    AnswerModel lockById(@Param(value = "id") long id);

    List<AnswerModel> findByLoginName(@Param(value = "loginName") String loginName,
                                      @Param(value = "index") int index,
                                      @Param(value = "pageSize") int pageSize);

    long countByLoginName(@Param(value = "loginName") String loginName);


    List<AnswerModel> findByQuestionId(@Param(value = "questionId") long questionId);

    AnswerModel findBestAnswerByQuestionId(@Param(value = "questionId") long questionId);
}
