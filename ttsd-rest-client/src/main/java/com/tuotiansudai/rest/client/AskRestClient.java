package com.tuotiansudai.rest.client;

import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.rest.support.client.annotations.RestClient;
import com.tuotiansudai.rest.support.client.exceptions.RestException;
import feign.RequestLine;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestClient(url = "${ask.rest.server}")
public interface AskRestClient {

    @RequestLine("POST /question")
    QuestionModel createQuestion(QuestionRequestDto questionPostDto) throws RestException;

    @RequestLine("PUT /question/approve")
    void approveQuestion(List<Long> questionIds) throws RestException;

    @RequestLine("PUT /question/reject")
    void rejectQuestion(List<Long> questionIds) throws RestException;

    @RequestLine("GET /question/{questionId}")
    QuestionModel getQuestion(long questionId) throws RestException;

    @RequestLine("GET /question/all")
    BaseDto<BasePaginationDataDto> findAllQuestions(@QueryParam("index") int index) throws RestException;

    @RequestLine("GET /question/unresolved")
    BaseDto<BasePaginationDataDto> findAllUnresolvedQuestions(@QueryParam("index")int index) throws RestException;

    @RequestLine("GET /question/hot")
    BaseDto<BasePaginationDataDto> findAllHotQuestions(@QueryParam("index")int index) throws RestException;

    @RequestLine("GET /question/mine")
    BaseDto<BasePaginationDataDto> findMyQuestions(@QueryParam("index")int index) throws RestException;

    @RequestLine("GET /question/tag/{tag}")
    BaseDto<BasePaginationDataDto> findByTag(@QueryParam("tag")Tag tag, @QueryParam("index")int index) throws RestException;

    @RequestLine("GET /question/isNewAnswerExists")
    Boolean isNewAnswerExists() throws RestException;

    @RequestLine("GET /question/byKeywords")
    BaseDto<BasePaginationDataDto> getQuestionsByKeywords(@QueryParam("keywords")String keywords, @QueryParam("index")int index) throws RestException;

    @RequestLine("GET /question/embodyAll")
    BaseDto<BasePaginationDataDto> findEmbodyAllQuestions(@QueryParam("index")int index) throws RestException;

    @RequestLine("PATCH /question/updateEmbody/{questionId}")
    void updateEmbody(@QueryParam("questionId")Long questionId) throws RestException;

    @RequestLine("GET /question/console")
    BaseDto<BasePaginationDataDto> findQuestionsForConsole(@QueryParam("question")String question, @QueryParam("mobile")String mobile, @QueryParam("status")QuestionStatus status, @QueryParam("index")int index, @QueryParam("pageSize")int pageSize) throws RestException;

}
