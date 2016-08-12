package com.tuotiansudai.ask.repository.mapper;

import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.repository.model.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionMapper {

    long create(QuestionModel questionModel);

    long update(QuestionModel questionModel);

    QuestionModel findById(@Param(value = "id") long id);

    List<QuestionModel> findByLoginName(@Param(value = "loginName") String loginName,
                                        @Param(value = "index") Integer index,
                                        @Param(value = "pageSize") Integer pageSize);

    long countByLoginName(@Param(value = "loginName") String loginName);


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

    long countByTag(@Param(value = "loginName") String loginName,
                    @Param(value = "tag") Tag tag);

    List<QuestionModel> findByTag(@Param(value = "loginName") String loginName,
                                  @Param(value = "tag") Tag tag,
                                  @Param(value = "index") int index,
                                  @Param(value = "pageSize") int pageSize);

    List<QuestionModel> findQuestionsForConsole(@Param(value = "question") String question,
                                                @Param(value = "loginName") String loginName,
                                                @Param(value = "status") QuestionStatus status,
                                                @Param(value = "index") int index,
                                                @Param(value = "pageSize") int pageSize);

    long countQuestionsForConsole(@Param(value = "question") String question,
                                  @Param(value = "loginName") String loginName,
                                  @Param(value = "status") QuestionStatus status);
}
