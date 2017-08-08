package com.tuotiansudai.current.client;

import com.tuotiansudai.current.dto.*;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CurrentDataDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.rest.support.client.annotations.RestClient;
import com.tuotiansudai.rest.support.client.exceptions.RestException;
import feign.Param;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@RestClient(url = "${current.rest.server}")
public interface CurrentRestClient {

    @GET
    @Path("/deposit/{id}")
    DepositDetailResponseDto getDeposit(@PathParam("id") long id) throws RestException;

    @POST
    @Path("/deposit")
    BaseDto<PayFormDataDto> deposit(DepositRequestDto requestDto) throws RestException;

    @POST
    @Path("/deposit")
    BaseDto<PayDataDto> noPasswordDeposit(DepositRequestDto requestDto) throws RestException;

    @POST
    @Path("/redeem")
    RedeemResponseDto redeem(RedeemRequestDto requestDto) throws RestException;

    @GET
    @Path("/account/{loginName}")
    AccountResponseDto getAccount(@PathParam("loginName") String loginName) throws RestException;

    @GET
    @Path("/task?handler_role={handlerRole}")
    CurrentDataDto<TaskResponseDto> task(@PathParam("handlerRole") List<Role> handlerRole) throws RestException;
}
