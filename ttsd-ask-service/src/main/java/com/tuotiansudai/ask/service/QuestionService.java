package com.tuotiansudai.ask.service;

import com.tuotiansudai.ask.dto.BaseDataDto;
import com.tuotiansudai.ask.dto.BaseDto;
import com.tuotiansudai.ask.dto.BasePaginationDataDto;
import com.tuotiansudai.ask.dto.QuestionRequestDto;

public interface QuestionService {

    BaseDto<BaseDataDto> createQuestion(String loginName, QuestionRequestDto questionRequestDto);

    BaseDto<BasePaginationDataDto> findAllQuestions(String loginName, int index, int pageSize);

    BaseDto<BasePaginationDataDto> findAllUnresolvedQuestions(String loginName, int index, int pageSize);

    BaseDto<BasePaginationDataDto> findAllHotQuestions(String loginName, int index, int pageSize);
}
