package com.tuotiansudai.ask.service;

import com.tuotiansudai.ask.dto.AnswerDto;
import com.tuotiansudai.ask.dto.AnswerRequestDto;
import com.tuotiansudai.ask.dto.BaseDto;
import com.tuotiansudai.ask.dto.BasePaginationDataDto;

import java.util.List;

public interface AnswerService {

    boolean createAnswer(String loginName, AnswerRequestDto answerRequestDto);

    AnswerDto getBestAnswer(String loginName, long questionId);

    List<AnswerDto> getAnswers(String loginName, long questionId);

    boolean makeBestAnswer(long answerId);

    boolean favor(String loginName, long answerId);

    BaseDto<BasePaginationDataDto> findMyAnswers(String loginName, int index, int pageSize);
}
