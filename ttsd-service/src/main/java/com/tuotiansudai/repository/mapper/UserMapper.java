package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * Created by hourglasskoala on 15/6/18.
 */
public interface UserMapper {
    @Select("select * from user where email = #{email}")
    @Results(value = {
         @Result(property = "loginName",column = "login_name"),
         @Result(property = "mobileNumber",column = "mobile_number"),
         @Result(property = "lastLoginTime",column = "last_login_time"),
         @Result(property = "registerTime",column = "register_time"),
         @Result(property = "lastModifiedTime",column = "last_modified_time"),
         @Result(property = "lastModifiedUser",column = "last_modified_user"),
         @Result(property = "forbiddenTime",column = "forbidden_time"),
    })
    public UserModel findUserByEmail(String email) throws Exception;
    @Results(value = {
            @Result(property = "loginName",column = "login_name"),
            @Result(property = "mobileNumber",column = "mobile_number"),
            @Result(property = "lastLoginTime",column = "last_login_time"),
            @Result(property = "registerTime",column = "register_time"),
            @Result(property = "lastModifiedTime",column = "last_modified_time"),
            @Result(property = "lastModifiedUser",column = "last_modified_user"),
            @Result(property = "forbiddenTime",column = "forbidden_time"),
    })
    @Select("select * from user where mobile_number = #{mobileNumber}")
    public UserModel findUserByMobileNumber(String mobileNumber) throws  Exception;
    @Results(value = {
            @Result(property = "loginName",column = "login_name"),
            @Result(property = "mobileNumber",column = "mobile_number"),
            @Result(property = "lastLoginTime",column = "last_login_time"),
            @Result(property = "registerTime",column = "register_time"),
            @Result(property = "lastModifiedTime",column = "last_modified_time"),
            @Result(property = "lastModifiedUser",column = "last_modified_user"),
            @Result(property = "forbiddenTime",column = "forbidden_time"),
    })
    @Select("select * from user where login_name = #{loginName}")
    public UserModel findUserByLoginName(String loginName) throws Exception;

    @Insert("insert into user (login_name,password,email,address,mobile_number,last_login_time,register_time,last_modified_time,last_modified_user,forbidden_time,avatar,referrer,status)"
            +" value(#{loginName},#{password},#{email},#{address},#{mobileNumber},#{lastLoginTime},#{registerTime},#{lastModifiedTime},#{lastModifiedUser},#{forbiddenTime},#{avatar},#{referrer},#{status})")
    public void insertUser(UserModel userModel) throws Exception;
}
