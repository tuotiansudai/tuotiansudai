package com.tuotiansudai.rest.client;

import com.tuotiansudai.dto.BasePaginationDataDto;
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
    public void updateEmail(String loginName, String email) {
        mapper.updateEmail(loginName, email);
    }

    @Override
    public void updateUserNameAndIdentityNumber(String loginName, String userName, String identityNumber) {
        throw new NotImplementedException("updateUserNameAndIdentityNumber not support for test, please Mock UserMapper for test");
    }

    @Override
    public BasePaginationDataDto<UserRegisterInfo> findUsersByRegisterTimeAndReferrer(Date startTime, Date endTime, String referrer, int page, int pageSize) {
        int rowIndex = (page - 1) * pageSize;
        List<UserModel> userModels = mapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, referrer, rowIndex, pageSize);
        long totalCount = mapper.findUserCountByRegisterTimeOrReferrer(startTime, endTime, referrer);
        return new BasePaginationDataDto<>(page, pageSize, totalCount, new ArrayList<>(userModels));
    }

    @Override
    public BasePaginationDataDto<UserRegisterInfo> findUsersHasReferrerByRegisterTime(Date startTime, Date endTime, int page, int pageSize) {
        int rowIndex = (page - 1) * pageSize;
        List<UserModel> userModels = mapper.findUsersHasReferrerByRegisterTime(startTime, endTime, pageSize, rowIndex);
        long totalCount = mapper.findUserCountHasReferrerByRegisterTime(startTime, endTime);
        return new BasePaginationDataDto<>(page, pageSize, totalCount, new ArrayList<>(userModels));
    }

    @Override
    public long findUserCountByRegisterTimeAndReferrer(Date startTime, Date endTime, String referrer) {
        return mapper.findUserCountByRegisterTimeOrReferrer(startTime, endTime, referrer);
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
