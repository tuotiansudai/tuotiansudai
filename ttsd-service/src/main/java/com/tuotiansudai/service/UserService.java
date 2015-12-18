package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.EditUserException;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;

import java.util.Date;
import java.util.List;

public interface UserService {

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
     * @param mobile
     * @param originalPassword 用户目前的密码（明文）
     * @param newPassword 新密码（明文）
     * @return 修改成功返回 true , 修改失败返回 false
     */
    boolean changePassword(String loginName, String mobile, String originalPassword, String newPassword);

    void editUser(String operatorLoginName, EditUserDto editUserDto, String ip) throws EditUserException, ReferrerRelationException;

    void updateUserStatus(String loginName, UserStatus userStatus, String ip, String operatorLoginName);

    EditUserDto getEditUser(String loginName);

    BaseDto<BasePaginationDataDto> findAllUser(String loginName, String email,
                String mobile, Date beginTime, Date endTime,
                Source source,
                Role role, String referrer, String channel, Integer pageIndex, Integer pageSize);


    List<String> findLoginNameFromAccountLike(String loginName);

    List<String> findLoginNameLike(String loginName);

    boolean verifyPasswordCorrect(String loginName, String password);

    List<String> findAllChannels();

    int findUserCount();

    void refreshAreaByMobile(List<UserModel> userModels) ;

    void refreshAreaByMobileInJob() ;

    List<UserModel> searchAllUsers(String loginName, String referrer, String mobile, String identityNumber, Integer index, Integer pageSize);

    int searchAllUsersCount(String loginName, String referrer, String mobile, String identityNumber);
}
