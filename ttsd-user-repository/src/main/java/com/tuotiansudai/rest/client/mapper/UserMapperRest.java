package com.tuotiansudai.rest.client.mapper;

import com.tuotiansudai.dto.request.UpdateUserInfoRequestDto;
import com.tuotiansudai.dto.request.UserRestQueryDto;
import com.tuotiansudai.dto.response.UserInfo;
import com.tuotiansudai.dto.response.UserRestPagingResponse;
import com.tuotiansudai.dto.response.UserRestUserInfo;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.UserMapperDB;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRegisterInfo;
import com.tuotiansudai.rest.client.UserRestClient;
import com.tuotiansudai.rest.support.client.exceptions.RestException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapperRest implements UserMapper {
    private final UserRestClient userRestClient;

    @Autowired
    private UserMapperDB userMapperDB;

    @Autowired
    public UserMapperRest(UserRestClient userRestClient) {
        this.userRestClient = userRestClient;
    }

    @Override
    public UserModel findByLoginNameOrMobile(String loginNameOrMobile) {
        if (StringUtils.isEmpty(loginNameOrMobile)) {
            return null;
        }
        try {
            UserRestUserInfo userRestUserInfo = userRestClient.findByLoginNameOrMobile(loginNameOrMobile);
            return userRestUserInfo.getUserInfo().toUserModel();
        } catch (RestException e) {
            return null;
        }
    }

    @Override
    public UserModel findByIdentityNumber(String identityNumber) {
        if (StringUtils.isEmpty(identityNumber)) {
            return null;
        }
        UserRestQueryDto queryDto = new UserRestQueryDto(false);
        queryDto.setIdentityNumber(identityNumber);
        UserRestPagingResponse<UserInfo> searchResult = userRestClient.search(queryDto);
        return searchResult.getTotalCount() > 0 ? searchResult.getItems().get(0).toUserModel() : null;
    }

    @Override
    public UserModel findByEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return null;
        }
        UserRestQueryDto queryDto = new UserRestQueryDto(false);
        queryDto.setEmail(email);
        UserRestPagingResponse<UserInfo> searchResult = userRestClient.search(queryDto);
        return searchResult.getTotalCount() > 0 ? searchResult.getItems().get(0).toUserModel() : null;
    }

    @Override
    public List<String> findAllLoginNames() {
        UserRestQueryDto queryDto = new UserRestQueryDto(false);
        queryDto.setFields("login_name");
        UserRestPagingResponse<UserInfo> searchResult = userRestClient.search(queryDto);
        return searchResult.getItems().stream().map(UserInfo::getLoginName).collect(Collectors.toList());
    }

    @Override
    public void updateEmail(String loginName, String email) {
        UpdateUserInfoRequestDto updateDto = new UpdateUserInfoRequestDto(loginName);
        updateDto.setEmail(email);
        userRestClient.update(updateDto);
    }

    @Override
    public void updateSignInCount(String loginName, int signInCount) {
        UpdateUserInfoRequestDto updateDto = new UpdateUserInfoRequestDto(loginName);
        updateDto.setSignInCount(signInCount);
        userRestClient.update(updateDto);
    }

    @Override
    public void updateUserNameAndIdentityNumber(String loginName, String userName, String identityNumber) {
        UpdateUserInfoRequestDto updateDto = new UpdateUserInfoRequestDto(loginName);
        updateDto.setUserName(userName);
        updateDto.setIdentityNumber(identityNumber);
        userRestClient.update(updateDto);
    }

    @Override
    public List<UserModel> findUsersByChannel(List<String> channels) {
        UserRestQueryDto queryDto = new UserRestQueryDto(false);
        queryDto.setChannels(channels);
        UserRestPagingResponse<UserInfo> searchResult = userRestClient.search(queryDto);
        return searchResult.getItems().stream().map(UserInfo::toUserModel).collect(Collectors.toList());
    }

    @Override
    public List<UserRegisterInfo> findUsersByRegisterTimeOrReferrer(Date startTime, Date endTime, String referrer) {
        UserRestQueryDto queryDto = new UserRestQueryDto(false);
        queryDto.setRegisterTimeGte(startTime);
        queryDto.setRegisterTimeLte(endTime);
        queryDto.setReferrer(referrer);
        queryDto.setFields(UserRegisterInfo.fields);
        queryDto.setSort("register_time");
        UserRestPagingResponse<UserInfo> searchResult = userRestClient.search(queryDto);
        return searchResult.getItems().stream().map(UserInfo::toUserModel).collect(Collectors.toList());
    }

    @Override
    public List<UserRegisterInfo> findUsersHasReferrerByRegisterTime(Date startTime, Date endTime) {
        UserRestQueryDto queryDto = new UserRestQueryDto(false);
        queryDto.setRegisterTimeGte(startTime);
        queryDto.setRegisterTimeLte(endTime);
        queryDto.setHasReferrer(true);
        queryDto.setFields(UserRegisterInfo.fields);
        queryDto.setSort("register_time");
        UserRestPagingResponse<UserInfo> searchResult = userRestClient.search(queryDto);
        return searchResult.getItems().stream().map(UserInfo::toUserModel).collect(Collectors.toList());
    }

    @Override
    public long findUserCountByRegisterTimeOrReferrer(Date startTime, Date endTime, String referrer) {
        UserRestQueryDto queryDto = new UserRestQueryDto(1, 1);
        queryDto.setRegisterTimeGte(startTime);
        queryDto.setRegisterTimeLte(endTime);
        queryDto.setReferrer(referrer);
        UserRestPagingResponse<UserInfo> searchResult = userRestClient.search(queryDto);
        return searchResult.getTotalCount();
    }

    @Override
    public List<String> findAllByRole(Role role) {
        UserRestQueryDto queryDto = new UserRestQueryDto(false);
        queryDto.setFields("login_name");
        queryDto.setRole(role);
        UserRestPagingResponse<UserInfo> searchResult = userRestClient.search(queryDto);
        return searchResult.getItems().stream().map(UserInfo::getLoginName).collect(Collectors.toList());
    }

    @Override
    public long findCountByRole(Role role) {
        UserRestQueryDto queryDto = new UserRestQueryDto(1, 1);
        queryDto.setFields("login_name");
        queryDto.setRole(role);
        UserRestPagingResponse<UserInfo> searchResult = userRestClient.search(queryDto);
        return searchResult.getTotalCount();
    }

    @Override
    public long findUsersCount() {
        UserRestQueryDto queryDto = new UserRestQueryDto(1, 1);
        UserRestPagingResponse<UserInfo> searchResult = userRestClient.search(queryDto);
        return searchResult.getTotalCount();
    }

    @Override
    public List<UserModel> findUserModelByMobileLike(String mobile, int page, int pageSize) {
        UserRestQueryDto queryDto = new UserRestQueryDto(page, pageSize);
        queryDto.setMobileLike(mobile);
        queryDto.setSort("-register_time");
        UserRestPagingResponse<UserInfo> searchResult = userRestClient.search(queryDto);
        return searchResult.getItems().stream().map(UserInfo::toUserModel).collect(Collectors.toList());
    }

    @Override
    public int findCountByMobileLike(String mobile) {
        UserRestQueryDto queryDto = new UserRestQueryDto(1, 1);
        queryDto.setMobileLike(mobile);
        UserRestPagingResponse<UserInfo> searchResult = userRestClient.search(queryDto);
        return searchResult.getTotalCount();
    }

    @Override
    public List<UserModel> findEmptyProvinceUsers() {
        UserRestPagingResponse<UserInfo> searchResult = userRestClient.findEmptyProvinceUsers(100);
        return searchResult.getItems().stream().map(UserInfo::toUserModel).collect(Collectors.toList());
    }

    @Override
    public void updateProvinceAndCity(String loginName, String province, String city) {
        UpdateUserInfoRequestDto updateDto = new UpdateUserInfoRequestDto(loginName);
        updateDto.setProvince(province);
        updateDto.setCity(city);
        userRestClient.update(updateDto);
    }

    @Override
    public UserModel lockByLoginName(String loginName) {
        return userMapperDB.lockByLoginName(loginName);
    }
}
