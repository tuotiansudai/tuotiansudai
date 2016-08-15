package com.tuotiansudai.console.activity.repository.mapper;

import com.tuotiansudai.console.activity.repository.model.UserModel;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    UserModel findByLoginName(String loginName);

}