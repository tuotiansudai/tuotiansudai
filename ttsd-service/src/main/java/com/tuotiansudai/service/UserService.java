package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.dto.RegisterUserDto;

public interface UserService {

    boolean emailIsExist(String email);

    boolean mobileIsExist(String mobile);

    boolean registerUser(RegisterUserDto dto);

    boolean loginNameIsExist(String loginName);

    BaseDto<PayDataDto> registerAccount(RegisterAccountDto dto);

    /**
     * 修改用户密码
     * @param mobile 用户手机号
     * @param password 重置的密码（密文）
     */
    void updatePassword(String mobile, String password);

    /**
     * 修改用户密码
     * @param loginName 用户名
     * @param oldPasswordPlain 用户目前的密码（明文）
     * @param newPasswordPlain 新密码（明文）
     * @return 修改成功返回 true , 修改失败返回 false
     */
    boolean changePassword(String loginName, String oldPasswordPlain, String newPasswordPlain);
}
