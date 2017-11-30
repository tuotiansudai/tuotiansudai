package com.tuotiansudai.repository.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceAccountMapper {
    int addBalance(@Param(value = "loginName") String loginName,
                   @Param(value = "experienceBalance") long experienceBalance);

    int getExperienceBalance(String loginName);

    int create(@Param(value = "loginName") String loginName,
               @Param(value = "experienceBalance") long experienceBalance);

}
