package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.EditUserException;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.security.MyAuthenticationManager;
import com.tuotiansudai.service.ReferrerRelationService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserAuditLogService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.utils.LoginUserInfo;
import com.tuotiansudai.utils.MyShaPasswordEncoder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    static Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private MyShaPasswordEncoder myShaPasswordEncoder;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ReferrerRelationService referrerRelationService;

    @Autowired
    private UserAuditLogService userAuditLogService;

    @Autowired
    private MyAuthenticationManager myAuthenticationManager;

    public static String SHA = "SHA";

    @Override
    public boolean emailIsExist(String email) {
        return userMapper.findByEmail(email) != null;
    }

    @Override
    public boolean mobileIsExist(String mobile) {
        return userMapper.findByMobile(mobile) != null;
    }

    @Override
    public boolean loginNameIsExist(String loginName) {
        return userMapper.findByLoginName(loginName) != null;
    }

    @Override
    @Transactional
    public boolean registerUser(RegisterUserDto dto) {
        boolean loginNameIsExist = this.loginNameIsExist(dto.getLoginName().toLowerCase());
        boolean mobileIsExist = this.mobileIsExist(dto.getMobile());
        boolean referrerIsNotExist = !Strings.isNullOrEmpty(dto.getReferrer()) && !this.loginNameIsExist(dto.getReferrer());
        boolean verifyCaptchaFailed = !this.smsCaptchaService.verifyMobileCaptcha(dto.getMobile(), dto.getCaptcha(), CaptchaType.REGISTER_CAPTCHA);

        if (loginNameIsExist || mobileIsExist || referrerIsNotExist || verifyCaptchaFailed) {
            return false;
        }
        UserModel userModel = dto.convertToUserModel();
        String salt = myShaPasswordEncoder.generateSalt();
        String encodePassword = myShaPasswordEncoder.encodePassword(dto.getPassword(), salt);
        userModel.setSalt(salt);
        userModel.setPassword(encodePassword);
        this.userMapper.create(userModel);

        UserRoleModel userRoleModel = new UserRoleModel();
        userRoleModel.setLoginName(dto.getLoginName().toLowerCase());
        userRoleModel.setRole(Role.USER);
        List<UserRoleModel> userRoleModels = Lists.newArrayList();
        userRoleModels.add(userRoleModel);
        this.userRoleMapper.create(userRoleModels);

        if (StringUtils.isNotEmpty(dto.getReferrer())) {
            this.referrerRelationService.generateRelation(dto.getReferrer(), dto.getLoginName());
        }

        myAuthenticationManager.createAuthentication(dto.getLoginName());

        return true;
    }

    @Override
    public BaseDto<PayDataDto> registerAccount(RegisterAccountDto dto) {
        dto.setLoginName(LoginUserInfo.getLoginName());
        dto.setMobile(LoginUserInfo.getMobile());
        BaseDto<PayDataDto> baseDto = payWrapperClient.register(dto);
        myAuthenticationManager.createAuthentication(LoginUserInfo.getLoginName());

        return baseDto;
    }

    @Override
    @Transactional
    public boolean changePassword(String loginName, String oldPassword, String newPassword) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        if (userModel == null) {
            return false;
        }
        String encodedOldPassword = myShaPasswordEncoder.encodePassword(oldPassword, userModel.getSalt());
        if (!userModel.getPassword().equals(encodedOldPassword)) {
            return false;
        }
        String encodedNewPassword = myShaPasswordEncoder.encodePassword(newPassword, userModel.getSalt());
        userMapper.updatePasswordByLoginName(loginName, encodedNewPassword);
        smsWrapperClient.sendPasswordChangedNotify(userModel.getMobile());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUser(EditUserDto editUserDto, String ip) throws EditUserException {
        this.checkUpdateUserData(editUserDto);

        final String loginName = editUserDto.getLoginName();

        String mobile = editUserDto.getMobile();
        UserModel userModel = userMapper.findByLoginName(loginName);
        UserModel beforeUpdateUserModel;
        try {
            beforeUpdateUserModel = userModel.clone();
        } catch (CloneNotSupportedException e) {
            logger.error(e.getLocalizedMessage(), e);
            return;
        }

        // update referrer
        this.referrerRelationService.updateRelation(editUserDto.getReferrer(), editUserDto.getLoginName());

        // update role
        List<UserRoleModel> beforeUpdateUserRoleModels = userRoleMapper.findByLoginName(loginName);
        userRoleMapper.deleteByLoginName(loginName);
        List<UserRoleModel> afterUpdateUserRoleModels = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(editUserDto.getRoles())) {
            afterUpdateUserRoleModels = Lists.transform(editUserDto.getRoles(), new Function<Role, UserRoleModel>() {
                @Override
                public UserRoleModel apply(Role role) {
                    return new UserRoleModel(loginName, role);
                }
            });
            userRoleMapper.create(afterUpdateUserRoleModels);
        }

        userModel.setStatus(editUserDto.getStatus());
        userModel.setMobile(mobile);
        userModel.setEmail(editUserDto.getEmail());
        userModel.setReferrer(Strings.isNullOrEmpty(editUserDto.getReferrer()) ? null : editUserDto.getReferrer());
        userModel.setLastModifiedTime(new Date());
        userModel.setLastModifiedUser(LoginUserInfo.getLoginName());
        userMapper.updateUser(userModel);

        //generate audit
        userAuditLogService.generateAuditLog(beforeUpdateUserModel, beforeUpdateUserRoleModels, userModel, afterUpdateUserRoleModels, ip);

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (!mobile.equals(userModel.getMobile()) && accountModel != null) {
            RegisterAccountDto registerAccountDto = new RegisterAccountDto(userModel.getLoginName(),
                    mobile,
                    accountModel.getUserName(),
                    accountModel.getIdentityNumber());
            BaseDto<PayDataDto> baseDto = payWrapperClient.register(registerAccountDto);
            if (!baseDto.getData().getStatus()) {
                throw new EditUserException(baseDto.getData().getMessage());
            }
        }
    }

    @Override
    @Transactional
    public void updateUserStatus(String loginName, UserStatus userStatus, String ip) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        UserModel beforeUpdateUserModel;
        try {
            beforeUpdateUserModel = userModel.clone();
        } catch (CloneNotSupportedException e) {
            logger.error(e.getLocalizedMessage(), e);
            return;
        }

        userModel.setStatus(userStatus);
        userModel.setLastModifiedTime(new Date());
        userModel.setLastModifiedUser(LoginUserInfo.getLoginName());
        userMapper.updateUser(userModel);
        List<UserRoleModel> userRoles = userRoleMapper.findByLoginName(loginName);

        userAuditLogService.generateAuditLog(beforeUpdateUserModel, userRoles, userModel, userRoles, ip);
    }

    private void checkUpdateUserData(EditUserDto editUserDto) throws EditUserException {
        String loginName = editUserDto.getLoginName();
        UserModel editUserModel = userMapper.findByLoginName(loginName);

        if (editUserModel == null) {
            throw new EditUserException("该用户不存在");
        }

        String newEmail = editUserDto.getEmail();
        if (!Strings.isNullOrEmpty(newEmail) && !editUserModel.getLoginName().equalsIgnoreCase(userMapper.findByEmail(newEmail).getLoginName())) {
            throw new EditUserException("该邮箱已经存在");
        }

        String mobile = editUserDto.getMobile();
        if (!Strings.isNullOrEmpty(mobile) && !editUserModel.getLoginName().equalsIgnoreCase(userMapper.findByMobile(mobile).getLoginName())) {
            throw new EditUserException("该手机号已经存在");
        }
    }

    @Override
    public EditUserDto getEditUser(String loginName) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(loginName);
        List<Role> roles = Lists.newArrayList();
        for (UserRoleModel userRoleModel : userRoleModels) {
            roles.add(userRoleModel.getRole());
        }
        AccountModel accountModel = accountMapper.findByLoginName(loginName);

        return new EditUserDto(userModel, accountModel, roles);
    }

    @Override
    public BaseDto<BasePaginationDataDto> findAllUser(String loginName, String email, String mobile, Date beginTime, Date endTime, Role role, String referrer, Integer index, Integer pageSize) {
        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        List<UserModel> userModels = userMapper.findAllUser(loginName, email, mobile, beginTime, endTime, role, referrer, (index - 1) * pageSize, pageSize);
        List<UserItemDataDto> userItemDataDtos = Lists.newArrayList();
        for (UserModel userModel : userModels) {

            UserItemDataDto userItemDataDto = new UserItemDataDto(userModel);
            userItemDataDto.setUserRoles(userRoleMapper.findByLoginName(userModel.getLoginName()));
            userItemDataDtos.add(userItemDataDto);
        }
        int count = userMapper.findAllUserCount(loginName, email, mobile, beginTime, endTime, role, referrer);
        BasePaginationDataDto<UserItemDataDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, userItemDataDtos);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);
        return baseDto;

    }

    @Override
    public List<String> findLoginNameLike(String loginName) {
        return userMapper.findLoginNameLike(loginName);
    }

}
