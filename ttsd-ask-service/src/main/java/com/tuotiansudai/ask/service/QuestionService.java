package com.tuotiansudai.ask.service;

import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;

public interface QuestionService {

    BaseDto<BaseDataDto> createQuestion(String loginName, QuestionRequestDto questionRequestDto);

    BaseDto<BasePaginationDataDto> findAllQuestions(String loginName, int index, int pageSize);

    BaseDto<BasePaginationDataDto> findAllUnresolvedQuestions(String loginName, int index, int pageSize);

    BaseDto<BasePaginationDataDto> findAllHotQuestions(String loginName, int index, int pageSize);
}
