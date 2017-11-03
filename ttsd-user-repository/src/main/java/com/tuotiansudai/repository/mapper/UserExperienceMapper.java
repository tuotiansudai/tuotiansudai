package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.ExperienceBillOperationType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExperienceMapper {
    int updateExperienceBalance(@Param(value = "loginName") String loginName,
                                @Param(value = "experienceBillOperationType") ExperienceBillOperationType experienceBillOperationType,
                                @Param(value = "experienceBalance") long experienceBalance);

    Long findExperienceByLoginName(String loginName);

}
