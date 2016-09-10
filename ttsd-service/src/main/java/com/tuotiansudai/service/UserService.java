package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.EditUserException;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.repository.model.UserView;

import java.util.List;

public interface UserService {

    String getMobile(String loginName);

    boolean emailIsExist(String email);

    boolean mobileIsExist(String mobile);

    boolean registerUser(RegisterUserDto dto) throws ReferrerRelationException;

    boolean loginNameIsExist(String loginName);

    boolean loginNameOrMobileIsExist(String loginNameOrMobile);

    BaseDto<PayDataDto> registerAccount(RegisterAccountDto dto);

    /**
     * 修改用户密码
     *
     * @param loginName
     * @param originalPassword 用户目前的密码（明文）
     * @param newPassword      新密码（明文）
     * @return 修改成功返回 true , 修改失败返回 false
     */
    boolean changePassword(String loginName, String originalPassword, String newPassword, String ip, String platform, String deviceId);

    void editUser(String operatorLoginName, EditUserDto editUserDto, String ip) throws EditUserException, ReferrerRelationException;

    EditUserDto getEditUser(String loginName);

    boolean verifyPasswordCorrect(String loginName, String password);

    List<String> findAllUserChannels();

    void refreshAreaByMobile(List<UserModel> userModels);

    void refreshAreaByMobileInJob();

    boolean resetUmpayPassword(String loginName, String identityNumber);

    boolean mobileIsRegister(String mobile);

    UserModel findByMobile(String mobile);

}
