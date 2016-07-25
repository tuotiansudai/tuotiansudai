package com.tuotiansudai.ask.repository.mapper;

import com.tuotiansudai.ask.repository.model.QuestionModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionMapper {

    long create(QuestionModel questionModel);

    List<QuestionModel> findByLoginName(@Param(value = "loginName") String loginName);

    long update(QuestionModel questionModel);
}
