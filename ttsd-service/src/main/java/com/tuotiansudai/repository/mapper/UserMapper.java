package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserModel;
import org.apache.ibatis.annotations.Insert;


public interface UserMapper {

    UserModel findByEmail(String email) ;

    UserModel findByMobile(String mobile) ;

    UserModel findByLoginName(String loginName) ;

    void create(UserModel userModel) ;
}
