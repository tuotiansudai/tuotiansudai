package com.tuotiansudai.repository.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceAccountMapper {
    int addBalance(@Param(value = "loginName") String loginName,
                   @Param(value = "experienceBalance") long experienceBalance);

    long getExperienceBalance(String loginName);

    long lockByLoginName(String loginName);

    int create(@Param(value = "loginName") String loginName,
               @Param(value = "experienceBalance") long experienceBalance);

    boolean exists(@Param(value = "loginName") String loginName);

}
