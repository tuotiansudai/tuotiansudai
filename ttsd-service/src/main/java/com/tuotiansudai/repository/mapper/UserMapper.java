package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserModel;
import org.apache.ibatis.annotations.Insert;


public interface UserMapper {

    public UserModel findUserByEmail(String email) throws Exception;

    public UserModel findUserByMobileNumber(String mobileNumber) throws  Exception;

    public UserModel findUserByLoginName(String loginName) throws Exception;

    @Insert("insert into user (login_name,password,email,address,mobile_number,last_login_time,register_time,last_modified_time,last_modified_user,forbidden_time,avatar,referrer,status,salt)"
            +" value(#{loginName},#{password},#{email},#{address},#{mobileNumber},#{lastLoginTime},#{registerTime},#{lastModifiedTime},#{lastModifiedUser},#{forbiddenTime},#{avatar},#{referrer},#{status},#{salt})")
    public void insertUser(UserModel userModel) throws Exception;
}
