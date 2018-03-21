package com.tuotiansudai.ask.repository.mapper;

import com.tuotiansudai.ask.repository.model.AnswerModel;
import com.tuotiansudai.ask.repository.model.AnswerStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerMapper {

    void initCharset();

    long create(AnswerModel answerModel);

    long update(AnswerModel answerModel);

    AnswerModel findById(@Param(value = "id") long id);

    AnswerModel lockById(@Param(value = "id") long id);

    List<AnswerModel> findByLoginName(@Param(value = "loginName") String loginName,
                                      @Param(value = "index") Integer index,
                                      @Param(value = "pageSize") Integer pageSize);

    long countByLoginName(@Param(value = "loginName") String loginName);


    List<AnswerModel> findByQuestionId(@Param(value = "loginName") String loginName,
                                       @Param(value = "questionId") long questionId);

    List<AnswerModel> findNotBestByQuestionId(@Param(value = "loginName") String loginName,
                                              @Param(value = "questionId") long questionId,
                                              @Param(value = "index") int index,
                                              @Param(value = "pageSize") int pageSize);

    long countNotBestByQuestionId(@Param(value = "loginName") String loginName,
                                  @Param(value = "questionId") long questionId);

    AnswerModel findBestAnswerByQuestionId(@Param(value = "questionId") long questionId);

    List<AnswerModel> findAnswersForConsole(@Param(value = "question") String question,
                                            @Param(value = "mobile") String mobile,
                                            @Param(value = "status") AnswerStatus status,
                                            @Param(value = "index") int index,
                                            @Param(value = "pageSize") int pageSize);

    long countAnswersForConsole(@Param(value = "question") String question,
                                @Param(value = "mobile") String mobile,
                                @Param(value = "status") AnswerStatus status);
}
