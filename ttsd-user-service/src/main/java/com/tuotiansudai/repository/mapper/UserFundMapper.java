package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserFundView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFundMapper {

    UserFundView findByLoginName(@Param(value = "loginName") String loginName);
}
