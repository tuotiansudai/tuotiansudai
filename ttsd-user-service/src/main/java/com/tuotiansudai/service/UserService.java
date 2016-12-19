package com.tuotiansudai.service;

import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.model.UserModel;

import java.util.List;

public interface UserService {

    String getMobile(String loginName);

    boolean emailIsExist(String email);

    boolean mobileIsExist(String mobile);

    boolean isIdentityNumberExist(String identityNumber);

    String getRealName(String loginNameOrMobile);

    boolean registerUser(RegisterUserDto dto) throws ReferrerRelationException;

    boolean loginNameIsExist(String loginName);

    boolean loginNameOrMobileIsExist(String loginNameOrMobile);

    /**
     * 修改用户密码
     *
     * @param loginName
     * @param originalPassword 用户目前的密码（明文）
     * @param newPassword      新密码（明文）
     * @return 修改成功返回 true , 修改失败返回 false
     */
    boolean changePassword(String loginName, String originalPassword, String newPassword, String ip, String platform, String deviceId);

    boolean verifyPasswordCorrect(String loginName, String password);

    void refreshAreaByMobile(List<UserModel> userModels);

    void refreshAreaByMobileInJob();

    boolean mobileIsRegister(String mobile);

    UserModel findByMobile(String mobile);
}
