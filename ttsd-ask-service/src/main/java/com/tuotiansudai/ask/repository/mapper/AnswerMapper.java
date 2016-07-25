package com.tuotiansudai.ask.repository.mapper;

import com.tuotiansudai.ask.repository.model.AnswerModel;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerMapper {

    long create(AnswerModel answerModel);

    List<AnswerModel> findByLoginName(@Param(value = "loginName") String loginName);

    long update(AnswerModel answerModel);
}
