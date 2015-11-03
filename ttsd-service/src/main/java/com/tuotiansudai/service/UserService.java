package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.BaseException;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserStatus;

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
     * @param originalPassword 用户目前的密码（明文）
     * @param newPassword 新密码（明文）
     * @return 修改成功返回 true , 修改失败返回 false
     */
    boolean changePassword(String originalPassword, String newPassword);

    void editUser(EditUserDto editUserDto, String ip) throws BaseException;

    void updateUserStatus(String loginName, UserStatus userStatus, String ip);

    EditUserDto getEditUser(String loginName);

    BaseDto<BasePaginationDataDto> findAllUser(String loginName, String email,
                String mobile, Date beginTime, Date endTime,
                Role role, String referrer, Integer pageIndex, Integer pageSize);


    List<String> findLoginNameLike(String loginName);

    boolean verifyPasswordCorrect(String password);
}
