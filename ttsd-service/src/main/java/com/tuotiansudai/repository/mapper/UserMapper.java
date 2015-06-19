package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserModel;
import org.apache.ibatis.annotations.Select;

/**
 * Created by hourglasskoala on 15/6/18.
 */
public interface UserMapper {
    @Select("select * from user where email = #{email}")
    public UserModel findUserByEmail(String email) throws Exception;

    @Select("select * from user where mobile_number = #{mobileNumber}")
    public UserModel findUserByMobileNumber(String mobileNumber) throws  Exception;

    @Select("select * from user where login_name = #{loginName}")
    public UserModel findReferrerByLoginName(String loginName) throws Exception;
}
