package com.tuotiansudai.ask.service;

import com.tuotiansudai.ask.dto.*;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.repository.model.Tag;

import java.util.List;

public interface QuestionService {

    BaseDto<BaseDataDto> createQuestion(String loginName, QuestionRequestDto questionRequestDto);

    void approve(String loginName, List<Long> questionIds);

    void reject(String loginName, List<Long> questionIds);

    QuestionDto getQuestion(long questionId);

    BaseDto<BasePaginationDataDto> findAllQuestions(String loginName, int index, int pageSize);

    BaseDto<BasePaginationDataDto> findAllUnresolvedQuestions(String loginName, int index, int pageSize);

    BaseDto<BasePaginationDataDto> findAllHotQuestions(String loginName, int index, int pageSize);

    BaseDto<BasePaginationDataDto> findMyQuestions(String loginName, int index, int pageSize);

    BaseDto<BasePaginationDataDto> findByTag(String loginName, Tag tag, int index, int pageSize);

    BaseDto<BasePaginationDataDto> findQuestionsForConsole(String question, String mobile, QuestionStatus status, int index, int pageSize);

    boolean isNewAnswerExists(String loginName);
}
