package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserModel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    UserModel findByEmail(String email);

    UserModel findByMobile(String mobile);

    UserModel findByLoginName(String loginName);

    UserModel findByLoginNameOrMobile(String loginNameOrMobile);

    void create(UserModel userModel);
}
