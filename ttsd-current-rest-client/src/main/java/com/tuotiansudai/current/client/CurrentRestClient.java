package com.tuotiansudai.current.client;

import com.tuotiansudai.current.dto.AccountResponseDto;
import com.tuotiansudai.current.dto.DepositDto;
import com.tuotiansudai.current.dto.*;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.rest.support.client.annotations.RestClient;
import com.tuotiansudai.rest.support.client.exceptions.RestException;

import javax.ws.rs.*;

@RestClient(url = "${current.rest.server}")
public interface CurrentRestClient {

    @GET
    @Path("/deposit/{id}")
    DepositDto getDeposit(@PathParam("id") long id) throws RestException;

    @PUT
    @Path("/deposit/{id}")
    DepositDto updateDeposit(@PathParam("id") long id, DepositDto requestDto) throws RestException;

    @POST
    @Path("/deposit")
    BaseDto<PayFormDataDto> deposit(DepositDto requestDto) throws RestException;

    @POST
    @Path("/deposit")
    BaseDto<PayDataDto> noPasswordDeposit(DepositDto requestDto) throws RestException;

    @POST
    @Path("/redeem")
    RedeemResponseDto redeem(RedeemRequestDto requestDto) throws RestException;

    @GET
    @Path("/account/{loginName}")
    AccountResponseDto getAccount(@PathParam("loginName") String loginName) throws RestException;
}
