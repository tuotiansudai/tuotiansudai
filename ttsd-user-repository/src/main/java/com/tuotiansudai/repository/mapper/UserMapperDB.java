package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface UserMapperDB {

    int updateExperienceBalance(@Param(value = "loginName") String loginName,
                                @Param(value = "experienceBillOperationType") ExperienceBillOperationType experienceBillOperationType,
                                @Param(value = "experienceBalance") long experienceBalance);

    int updateProvinceAndCity(@Param(value = "loginName") String loginName,
                              @Param(value = "province") String province,
                              @Param(value = "city") String city);

    UserModel lockByLoginName(String loginName);

    List<String> findAllUserChannels();

    List<UserModel> findUsersByProvince();

    List<String> findAllRecommendation(Map<String, Object> params);

    List<Integer> findScaleByGender(@Param(value = "endDate") Date endDate);

    List<Map<String, String>> findAgeDistributionByAge(@Param(value = "endDate") Date endDate);

    List<Map<String, String>> findCountInvestCityScaleTop3(@Param(value = "endDate") Date endDate);

    long findCountInvestCityScale(@Param(value = "endDate") Date endDate);

    Long findExperienceByLoginName(String loginName);
}
