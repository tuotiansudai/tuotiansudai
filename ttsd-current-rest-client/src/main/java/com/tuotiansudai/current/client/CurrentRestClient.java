package com.tuotiansudai.current.client;

import com.tuotiansudai.current.dto.DepositRequestDto;
import com.tuotiansudai.current.dto.RedeemRequestDto;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.rest.support.client.annotations.RestClient;
import com.tuotiansudai.rest.support.client.exceptions.RestException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@RestClient(url = "${current.rest.server}")
public interface CurrentRestClient {

    @POST
    @Path("/user/{loginName}/deposit-with-password")
    BaseDto<PayFormDataDto> invest(DepositRequestDto requestDto, @PathParam("loginName") String loginName) throws RestException;

    @POST
    @Path("/user/{loginName}/redeem")
    BaseDto<BaseDataDto> redeem(RedeemRequestDto requestDto, @PathParam("loginName") long loginName) throws RestException;

//    @GET
//    @Path("/question/all")
//    BaseDto<BasePaginationDataDto<QuestionModel>> findAllQuestions(@QueryParam("index") int index) throws RestException;

}
