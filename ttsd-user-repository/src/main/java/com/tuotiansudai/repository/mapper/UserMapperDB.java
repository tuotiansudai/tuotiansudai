package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapperDB {

    int updateProvinceAndCity(@Param(value = "loginName") String loginName,
                              @Param(value = "province") String province,
                              @Param(value = "city") String city);

    UserModel lockByLoginName(String loginName);

    List<UserModel> findUsersByProvince();

    List<String> findAllRecommendation(Map<String, Object> params);
}
