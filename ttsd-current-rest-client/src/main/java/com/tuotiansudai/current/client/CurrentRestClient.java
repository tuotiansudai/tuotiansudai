package com.tuotiansudai.current.client;

import com.tuotiansudai.current.dto.DepositRequestDto;
import com.tuotiansudai.current.dto.RedeemRequestDto;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.rest.support.client.annotations.RestClient;
import com.tuotiansudai.rest.support.client.exceptions.RestException;

import javax.ws.rs.GET;
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

    @POST
    @Path("/redeem/create")
    BaseDto<RedeemDataDto> redeem(RedeemRequestDto requestDto) throws RestException;

    @GET
    @Path("/redeem/{loginName}/limits")
    BaseDto<RedeemLimitsDataDto> limits(@PathParam("loginName") String loginName) throws RestException;

}
