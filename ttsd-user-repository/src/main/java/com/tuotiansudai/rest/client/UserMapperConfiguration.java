package com.tuotiansudai.rest.client;

import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.UserMapperDB;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.FeignClientConfig;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.rest.client.mapper.UserMapperRest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Configuration
@Import(FeignClientConfig.class)
public class UserMapperConfiguration {

    @Bean
    @Profile("test")
    public UserMapper userMapperTest(UserMapperDB userMapperDB) {
        return new UserMapperDBShadow(userMapperDB);
    }

    @Bean
    @Profile("!test")
    public UserMapper userMapper(UserRestClient userRestClient) {
        return new UserMapperRest(userRestClient);
    }
}

class UserMapperDBShadow implements UserMapper {
    private final UserMapperDB mapper;

    UserMapperDBShadow(UserMapperDB mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserModel findByLoginNameOrMobile(String loginNameOrMobile) {
        return mapper.findByLoginNameOrMobile(loginNameOrMobile);
    }

    @Override
    public UserModel findByIdentityNumber(String identityNumber) {
        return mapper.findByIdentityNumber(identityNumber);
    }

    @Override
    public UserModel findByEmail(String email) {
        return mapper.findByEmail(email);
    }

    @Override
    public List<String> findAllLoginNames() {
        return mapper.findAllLoginNames();
    }

    @Override
    public void updateEmail(String loginName, String email) {
        mapper.updateEmail(loginName, email);
    }

    @Override
    public void updateSignInCount(String loginName, int signInCount) {
        mapper.updateSignInCount(loginName, signInCount);
    }

    @Override
    public void updateUserNameAndIdentityNumber(String loginName, String userName, String identityNumber) {
        mapper.updateUserNameAndIdentityNumber(loginName, userName, identityNumber);
    }

    @Override
    public List<UserModel> findUsersByChannel(List<String> channels) {
        return mapper.findUsersByChannel(channels);
    }

    @Override
    public List<UserModel> findUsersByRegisterTimeOrReferrer(Date startTime, Date endTime, String referrer) {
        return mapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, referrer);
    }

    @Override
    public List<String> findAllByRole(Role role) {
        return mapper.findAllByRole(role);
    }

    @Override
    public long findCountByRole(Role role) {
        return mapper.findCountByRole(role);
    }

    @Override
    public long findUsersCount() {
        return mapper.findUsersCount();
    }

    @Override
    public List<UserModel> findUserModelByMobileLike(String mobile, int page, int pageSize) {
        return mapper.findUserModelByMobileLike(mobile, page, pageSize);
    }

    @Override
    public int findCountByMobileLike(String mobile) {
        return mapper.findCountByMobileLike(mobile);
    }

    @Override
    public List<String> findAllRecommendation(HashMap<String, Object> districtName) {
        return mapper.findAllRecommendation(districtName);
    }

    @Override
    public UserModel lockByLoginName(String loginName) {
        return mapper.lockByLoginName(loginName);
    }

    @Override
    public List<UserModel> findUsersByProvince() {
        return mapper.findUsersByProvince();
    }

    @Override
    public int updateProvinceAndCity(String loginName, String province, String city) {
        return mapper.updateProvinceAndCity(loginName, province, city);
    }
}
