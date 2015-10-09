package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public interface UserService {

    boolean emailIsExist(String email);

    boolean mobileIsExist(String mobile);

    boolean registerUser(RegisterUserDto dto);

    boolean loginNameIsExist(String loginName);

    BaseDto<PayDataDto> registerAccount(RegisterAccountDto dto);

    BaseDto<PayDataDto> reRegisterAccount(RegisterAccountDto dto);

    void saveReferrerRelations(String referrerLoginName, String loginName);

    /**
     * 修改用户密码
     *
     * @param loginName        用户名
     * @param oldPasswordPlain 用户目前的密码（明文）
     * @param newPasswordPlain 新密码（明文）
     * @return 修改成功返回 true , 修改失败返回 false
     */
    boolean changePassword(String loginName, String oldPasswordPlain, String newPasswordPlain);

    BaseDto<PayDataDto> editUser(EditUserDto editUserDto,String ip);

    EditUserDto getUser(String loginName);

    BaseDto<BasePaginationDataDto> findAllUser(String loginName, String email,
                String mobile, Date beginTime, Date endTime,
                Role role, String referrer, Integer pageIndex, Integer pageSize);



}
