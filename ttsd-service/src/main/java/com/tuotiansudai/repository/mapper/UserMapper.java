package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserModel;
import org.apache.ibatis.annotations.Insert;


public interface UserMapper {

    UserModel findUserById(Long id);

    UserModel findUserByEmail(String email) ;

    UserModel findUserByMobileNumber(String mobileNumber) ;

    UserModel findUserByLoginName(String loginName) ;

    void insertUser(UserModel userModel) ;
}
