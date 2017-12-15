package com.tuotiansudai.rest.client;

import com.tuotiansudai.dto.request.*;
import com.tuotiansudai.dto.response.UserInfo;
import com.tuotiansudai.dto.response.UserRestPagingResponse;
import com.tuotiansudai.dto.response.UserRestUserInfo;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.rest.support.client.annotations.RestClient;
import com.tuotiansudai.rest.support.client.exceptions.RestException;

import javax.ws.rs.*;
import java.text.SimpleDateFormat;

@RestClient(url = "user.rest.server")
public interface UserRestClient {

    @POST
    @Path("/users")
    UserRestUserInfo register(RegisterRequestDto requestDto) throws RestException;

    @PUT
    @Path("/user")
    UserRestUserInfo update(UpdateUserInfoRequestDto requestDto) throws RestException;

    @GET
    @Path("/user/{loginNameOrMobile}")
    UserRestUserInfo findByLoginNameOrMobile(@PathParam("loginNameOrMobile") String loginNameOrMobile) throws RestException;

    @PUT
    @Path("/user/reset-password")
    UserRestUserInfo resetPassword(ResetPasswordRequestDto requestDto) throws RestException;

    @POST
    @Path("/user/change-password")
    UserRestUserInfo changePassword(ChangePasswordRequestDto requestDto) throws RestException;

    @GET
    @Path("/users")
    UserRestPagingResponse<UserInfo> _search(
            @QueryParam("page") Integer page,
            @QueryParam("page_size") Integer pageSize,
            @QueryParam("sort") String sort,
            @QueryParam("fields") String fields,
            @QueryParam("email") String email,
            @QueryParam("role") Role role,
            @QueryParam("status") UserStatus status,
            @QueryParam("channels") String channels,
            @QueryParam("referrer") String referrer,
            @QueryParam("identity_number") String identityNumber,
            @QueryParam("mobile__like") String mobileLike,
            @QueryParam("register_time__gte") String registerTimeGte,
            @QueryParam("register_time__lte") String registerTimeLte,
            @QueryParam("referrer__hasvalue") String referrerHasValue) throws RestException;

    default UserRestPagingResponse<UserInfo> search(UserRestQueryDto queryDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return this._search(queryDto.getPage(),
                queryDto.getPageSize(),
                queryDto.getSort() == null ? null : String.join(",", queryDto.getSort()),
                queryDto.getFields() == null ? null : String.join(",", queryDto.getFields()),
                queryDto.getEmail(),
                queryDto.getRole(),
                queryDto.getStatus(),
                queryDto.getChannels() == null ? null : String.join(",", queryDto.getChannels()),
                queryDto.getReferrer(),
                queryDto.getIdentityNumber(),
                queryDto.getMobileLike(),
                queryDto.getRegisterTimeGte() == null ? null : sdf.format(queryDto.getRegisterTimeGte()),
                queryDto.getRegisterTimeLte() == null ? null : sdf.format(queryDto.getRegisterTimeLte()),
                queryDto.getHasReferrer() == null? null: queryDto.getHasReferrer().toString());
    }

    @GET
    @Path("/users/province-empty")
    UserRestPagingResponse<UserInfo> findEmptyProvinceUsers(@QueryParam("limit") int limit);
}
