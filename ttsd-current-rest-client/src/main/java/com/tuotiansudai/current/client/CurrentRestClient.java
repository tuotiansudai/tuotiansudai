package com.tuotiansudai.current.client;

import com.tuotiansudai.current.dto.DepositRequestDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.rest.support.client.annotations.RestClient;
import com.tuotiansudai.rest.support.client.exceptions.RestException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@RestClient(url = "${current.rest.server}")
public interface CurrentRestClient {

    @POST
    @Path("/deposit-with-password")
    BaseDto<PayFormDataDto> invest(DepositRequestDto requestDto) throws RestException;

    @POST
    @Path("/deposit-with-no-password")
    BaseDto<PayDataDto> noPasswordInvest(DepositRequestDto requestDto) throws RestException;

//    @GET
//    @Path("/question/{questionId}")
//    QuestionModel getQuestion(@PathParam("questionId") long questionId) throws RestException;

//    @GET
//    @Path("/question/all")
//    BaseDto<BasePaginationDataDto<QuestionModel>> findAllQuestions(@QueryParam("index") int index) throws RestException;

}
