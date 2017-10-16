package com.tuotiansudai.rest.client.mapper;

import com.tuotiansudai.dto.request.UpdateUserInfoRequestDto;
import com.tuotiansudai.dto.request.UserRestQueryDto;
import com.tuotiansudai.dto.response.UserRestPagingResponse;
import com.tuotiansudai.dto.response.UserRestUserInfo;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.UserMapperDB;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.UserRestClient;
import com.tuotiansudai.rest.support.client.exceptions.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserRemoteMapper implements UserMapper {
    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private UserMapperDB userMapperDB;

    @Override
    public UserModel findByLoginNameOrMobile(String loginNameOrMobile) {
        try {
            UserRestUserInfo userRestUserInfo = userRestClient.findByLoginNameOrMobile(loginNameOrMobile);
            return userRestUserInfo.getUserInfo();
        } catch (RestException e) {
            return null;
        }
    }

    @Override
    public UserModel findByMobile(String mobile) {
        return this.findByLoginNameOrMobile(mobile);
    }

    @Override
    public UserModel findByLoginName(String loginName) {
        return this.findByLoginNameOrMobile(loginName);
    }

    @Override
    public UserModel findByIdentityNumber(String identityNumber) {
        UserRestQueryDto queryDto = new UserRestQueryDto(false);
        queryDto.setIdentityNumber(identityNumber);
        UserRestPagingResponse<UserModel> searchResult = userRestClient.search(queryDto);
        return searchResult.getTotalCount() > 0 ? searchResult.getItems().get(0) : null;
    }

    @Override
    public UserModel findByEmail(String email) {
        UserRestQueryDto queryDto = new UserRestQueryDto(false);
        queryDto.setEmail(email);
        UserRestPagingResponse<UserModel> searchResult = userRestClient.search(queryDto);
        return searchResult.getTotalCount() > 0 ? searchResult.getItems().get(0) : null;
    }

    @Override
    public List<String> findAllLoginNames() {
        UserRestQueryDto queryDto = new UserRestQueryDto(false);
        queryDto.setFields("login_name");
        UserRestPagingResponse<UserModel> searchResult = userRestClient.search(queryDto);
        return searchResult.getItems().stream().map(UserModel::getLoginName).collect(Collectors.toList());
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
        UserRestPagingResponse<UserModel> searchResult = userRestClient.search(queryDto);
        return searchResult.getItems();
    }

    @Override
    public List<UserModel> findUsersByRegisterTimeOrReferrer(Date startTime, Date endTime, String referrer) {
        UserRestQueryDto queryDto = new UserRestQueryDto(false);
        queryDto.setRegisterTimeGte(startTime);
        queryDto.setRegisterTimeLte(endTime);
        queryDto.setReferrer(referrer);
        queryDto.setSort("register_time");
        UserRestPagingResponse<UserModel> searchResult = userRestClient.search(queryDto);
        return searchResult.getItems();
    }

    @Override
    public List<String> findAllByRole(Role role) {
        UserRestQueryDto queryDto = new UserRestQueryDto(false);
        queryDto.setFields("login_name");
        queryDto.setRole(role);
        UserRestPagingResponse<UserModel> searchResult = userRestClient.search(queryDto);
        return searchResult.getItems().stream().map(UserModel::getLoginName).collect(Collectors.toList());
    }

    @Override
    public long findCountByRole(Role role) {
        UserRestQueryDto queryDto = new UserRestQueryDto(1, 1);
        queryDto.setFields("login_name");
        queryDto.setRole(role);
        UserRestPagingResponse<UserModel> searchResult = userRestClient.search(queryDto);
        return searchResult.getTotalCount();
    }

    @Override
    public long findUsersCount() {
        UserRestQueryDto queryDto = new UserRestQueryDto(1, 1);
        UserRestPagingResponse<UserModel> searchResult = userRestClient.search(queryDto);
        return searchResult.getTotalCount();
    }

    @Override
    public List<UserModel> findUserModelByMobileLike(String mobile, int page, int pageSize) {
        UserRestQueryDto queryDto = new UserRestQueryDto(page, pageSize);
        queryDto.setMobileLike(mobile);
        queryDto.setSort("-register_time");
        UserRestPagingResponse<UserModel> searchResult = userRestClient.search(queryDto);
        return searchResult.getItems();
    }

    @Override
    public int findCountByMobileLike(String mobile) {
        UserRestQueryDto queryDto = new UserRestQueryDto(1, 1);
        queryDto.setMobileLike(mobile);
        UserRestPagingResponse<UserModel> searchResult = userRestClient.search(queryDto);
        return searchResult.getTotalCount();
    }

    @Override
    public List<String> findAllRecommendation(HashMap<String, Object> districtName) {
        return userMapperDB.findAllRecommendation(districtName);
    }

    @Override
    public UserModel lockByLoginName(String loginName) {
        return userMapperDB.lockByLoginName(loginName);
    }

    @Override
    public List<UserModel> findUsersByProvince() {
        return userMapperDB.findUsersByProvince();
    }

    @Override
    public void updateProvinceAndCity(String loginName, String province, String city) {
        userMapperDB.updateProvinceAndCity(loginName, province, city);
    }
}