package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserModel;
import org.apache.ibatis.annotations.Insert;


public interface UserMapper {

    public UserModel findUserByEmail(String email) ;

    public UserModel findUserByMobileNumber(String mobileNumber) ;

    public UserModel findUserByLoginName(String loginName) ;

    public void insertUser(UserModel userModel) ;
}
