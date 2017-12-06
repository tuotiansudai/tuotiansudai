package com.tuotiansudai.rest.client;

import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.UserMapperDB;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRegisterInfo;
import com.tuotiansudai.rest.FeignClientConfig;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.rest.client.mapper.UserMapperRest;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.Date;
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
        throw new NotImplementedException("findAllLoginNames not support for test, please Mock UserMapper for test");
    }

    @Override
    public void updateEmail(String loginName, String email) {
        mapper.updateEmail(loginName, email);
    }

    @Override
    public void updateUserNameAndIdentityNumber(String loginName, String userName, String identityNumber) {
        throw new NotImplementedException("updateUserNameAndIdentityNumber not support for test, please Mock UserMapper for test");
    }

    @Override
    public List<UserModel> findUsersByChannel(List<String> channels) {
        return mapper.findUsersByChannel(channels);
    }

    @Override
    public List<UserRegisterInfo> findUsersByRegisterTimeOrReferrer(Date startTime, Date endTime, String referrer) {
        return new ArrayList<>(mapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, referrer));
    }

    @Override
    public List<UserRegisterInfo> findUsersHasReferrerByRegisterTime(Date startTime, Date endTime) {
        return new ArrayList<>(mapper.findUsersHasReferrerByRegisterTime(startTime, endTime));
    }

    @Override
    public long findUserCountByRegisterTimeOrReferrer(Date startTime, Date endTime, String referrer) {
        return mapper.findUserCountByRegisterTimeOrReferrer(startTime, endTime, referrer);
    }

    @Override
    public List<String> findAllByRole(Role role) {
        throw new NotImplementedException("findAllByRole not support for test, please Mock UserMapper for test");
    }

    @Override
    public long findCountByRole(Role role) {
        throw new NotImplementedException("findCountByRole not support for test, please Mock UserMapper for test");
    }

    @Override
    public long findUsersCount() {
        return mapper.findUsersCount();
    }

    @Override
    public List<UserModel> findUserModelByMobileLike(String mobile, int page, int pageSize) {
        throw new NotImplementedException("findUserModelByMobileLike not support for test, please Mock UserMapper for test");
    }

    @Override
    public int findCountByMobileLike(String mobile) {
        throw new NotImplementedException("findCountByMobileLike not support for test, please Mock UserMapper for test");
    }

    @Override
    public UserModel lockByLoginName(String loginName) {
        return mapper.lockByLoginName(loginName);
    }

    @Override
    public List<UserModel> findEmptyProvinceUsers() {
        throw new NotImplementedException("findEmptyProvinceUsers not support for test, please Mock UserMapper for test");
    }

    @Override
    public void updateProvinceAndCity(String loginName, String province, String city) {
        throw new NotImplementedException("updateProvinceAndCity not support for test, please Mock UserMapper for test");
    }
}
