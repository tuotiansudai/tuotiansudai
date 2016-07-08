package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.EditUserException;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.repository.model.UserView;

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
     * @param originalPassword 用户目前的密码（明文）
     * @param newPassword      新密码（明文）
     * @return 修改成功返回 true , 修改失败返回 false
     */
    boolean changePassword(String loginName, String originalPassword, String newPassword, String ip, String platform, String deviceId);

    void editUser(String operatorLoginName, EditUserDto editUserDto, String ip) throws EditUserException, ReferrerRelationException;

    void updateUserStatus(String loginName, UserStatus userStatus, String ip, String operatorLoginName);

    EditUserDto getEditUser(String loginName);

    List<String> findStaffNameFromUserLike(String loginName);

    List<String> findAllLoanerLikeLoginName(String loginName);

    List<String> findAccountLikeLoginName(String loginName);

    List<String> findLoginNameLike(String loginName);

    List<String> findMobileLike(String mobile);

    boolean verifyPasswordCorrect(String loginName, String password);

    List<String> findAllChannels();

    List<String> findAllUserChannels();

    void refreshAreaByMobile(List<UserModel> userModels);

    void refreshAreaByMobileInJob();

    List<UserView> searchAllUsers(String loginName, String referrerMobile, String mobile, String identityNumber);

    List<UserItemDataDto> findUsersAccountBalance(String loginName, String balanceMin, String balanceMax, int currentPageNo, int pageSize);

    int findUsersAccountBalanceCount(String loginName, String balanceMin, String balanceMax);

    boolean resetUmpayPassword(String loginName, String identityNumber);

    long findUsersAccountBalanceSum(String loginName, String balanceMin, String balanceMax);
}
