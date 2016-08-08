package com.tuotiansudai.ask.service;

import com.tuotiansudai.ask.dto.*;
import com.tuotiansudai.ask.repository.model.Tag;

public interface QuestionService {

    BaseDto<BaseDataDto> createQuestion(String loginName, QuestionRequestDto questionRequestDto);

    QuestionDto getQuestion(long questionId);

    BaseDto<BasePaginationDataDto> findAllQuestions(String loginName, int index, int pageSize);

    BaseDto<BasePaginationDataDto> findAllUnresolvedQuestions(String loginName, int index, int pageSize);

    BaseDto<BasePaginationDataDto> findAllHotQuestions(String loginName, int index, int pageSize);

    BaseDto<BasePaginationDataDto> findMyQuestions(String loginName, int index, int pageSize);

    BaseDto<BasePaginationDataDto> findByTag(String loginName, Tag tag, int index, int pageSize);
}
