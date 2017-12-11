package com.tuotiansudai.rest.client;

import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.rest.support.client.annotations.RestClient;
import com.tuotiansudai.rest.support.client.exceptions.RestException;

import javax.ws.rs.*;
import java.util.List;

@RestClient(url = "ask.rest.server")
public interface AskRestClient {

    @POST
    @Path("/question")
    QuestionModel createQuestion(QuestionRequestDto questionPostDto) throws RestException;

    @PUT
    @Path("/question/approve")
    void approveQuestion(List<Long> questionIds) throws RestException;

    @PUT
    @Path("/question/reject")
    void rejectQuestion(List<Long> questionIds) throws RestException;

    @GET
    @Path("/question/{questionId}")
    QuestionModel getQuestion(@PathParam("questionId") long questionId) throws RestException;

    @GET
    @Path("/question/all")
    BaseDto<BasePaginationDataDto<QuestionModel>> findAllQuestions(@QueryParam("index") int index) throws RestException;

    @GET
    @Path("/question/unresolved")
    BaseDto<BasePaginationDataDto<QuestionModel>> findAllUnresolvedQuestions(@QueryParam("index") int index) throws RestException;

    @GET
    @Path("/question/hot")
    BaseDto<BasePaginationDataDto<QuestionModel>> findAllHotQuestions(@QueryParam("index") int index) throws RestException;

    @GET
    @Path("/question/mine")
    BaseDto<BasePaginationDataDto<QuestionModel>> findMyQuestions(@QueryParam("index") int index) throws RestException;

    @GET
    @Path("/question/tag/{tag}")
    BaseDto<BasePaginationDataDto<QuestionModel>> findByTag(@PathParam("tag") Tag tag, @QueryParam("index") int index) throws RestException;

    @GET
    @Path("/question/isNewAnswerExists")
    Boolean isNewAnswerExists() throws RestException;

    @GET
    @Path("/question/byKeywords")
    BaseDto<BasePaginationDataDto<QuestionModel>> getQuestionsByKeywords(@QueryParam("keywords") String keywords, @QueryParam("index") int index) throws RestException;

    @GET
    @Path("/question/embodyAll")
    BaseDto<BasePaginationDataDto<QuestionModel>> findEmbodyAllQuestions(@QueryParam("index") int index) throws RestException;

    @PUT
    @Path("/question/updateEmbody/{questionId}")
    void updateEmbody(@PathParam("questionId") Long questionId) throws RestException;

    @GET
    @Path("/question/console")
    BaseDto<BasePaginationDataDto<QuestionModel>> findQuestionsForConsole(@QueryParam("question") String question,
                                                                          @QueryParam("mobile") String mobile,
                                                                          @QueryParam("status") QuestionStatus status,
                                                                          @QueryParam("index") int index,
                                                                          @QueryParam("pageSize") int pageSize) throws RestException;

}
