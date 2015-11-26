package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.CreateUserException;
import com.tuotiansudai.exception.EditUserException;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.security.MyAuthenticationManager;
import com.tuotiansudai.service.ReferrerRelationService;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.AuditLogService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.MyShaPasswordEncoder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
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
    private AuditLogService auditLogService;

    @Autowired
    private MyAuthenticationManager myAuthenticationManager;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Value("${web.login.max.failed.times}")
    private int times;

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
    public boolean loginNameOrMobileIsExist(String loginNameOrMobile) {
        return userMapper.findByLoginNameOrMobile(loginNameOrMobile) != null;
    }

    @Override
    @Transactional
    public boolean registerUser(RegisterUserDto dto) {
        String loginName = dto.getLoginName();
        boolean loginNameIsExist = this.loginNameIsExist(loginName);
        boolean mobileIsExist = this.mobileIsExist(dto.getMobile());
        boolean referrerIsNotExist = !Strings.isNullOrEmpty(dto.getReferrer()) && !this.loginNameOrMobileIsExist(dto.getReferrer());
        boolean verifyCaptchaFailed = !this.smsCaptchaService.verifyMobileCaptcha(dto.getMobile(), dto.getCaptcha(), CaptchaType.REGISTER_CAPTCHA);

        if (loginNameIsExist || mobileIsExist || referrerIsNotExist || verifyCaptchaFailed) {
            return false;
        }
        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setMobile(dto.getMobile());
        if (!Strings.isNullOrEmpty(dto.getReferrer())) {
            userModel.setReferrer(userMapper.findByLoginNameOrMobile(dto.getReferrer()).getLoginName());
        }
        if (!Strings.isNullOrEmpty(dto.getChannel())) {
            userModel.setChannel(dto.getChannel());
        }
        String salt = myShaPasswordEncoder.generateSalt();
        String encodePassword = myShaPasswordEncoder.encodePassword(dto.getPassword(), salt);
        userModel.setSalt(salt);
        userModel.setPassword(encodePassword);
        this.userMapper.create(userModel);

        UserRoleModel userRoleModel = new UserRoleModel();
        userRoleModel.setLoginName(loginName.toLowerCase());
        userRoleModel.setRole(Role.USER);
        List<UserRoleModel> userRoleModels = Lists.newArrayList();
        userRoleModels.add(userRoleModel);
        this.userRoleMapper.create(userRoleModels);

        if (StringUtils.isNotEmpty(dto.getReferrer())) {
            this.referrerRelationService.generateRelation(userMapper.findByLoginNameOrMobile(dto.getReferrer()).getLoginName(), loginName);
        }

        myAuthenticationManager.createAuthentication(loginName);

        return true;
    }

    @Override
    public BaseDto<PayDataDto> registerAccount(RegisterAccountDto dto) {
        BaseDto<PayDataDto> baseDto = payWrapperClient.register(dto);
        myAuthenticationManager.createAuthentication(dto.getLoginName());

        return baseDto;
    }

    @Override
    @Transactional
    public boolean changePassword(String loginName, String mobile, String originalPassword, String newPassword) {

        boolean correct = this.verifyPasswordCorrect(loginName, originalPassword);

        if (!correct) {
            return false;
        }

        UserModel userModel = userMapper.findByLoginName(loginName);

        String encodedNewPassword = myShaPasswordEncoder.encodePassword(newPassword, userModel.getSalt());
        userMapper.updatePasswordByLoginName(loginName, encodedNewPassword);
        smsWrapperClient.sendPasswordChangedNotify(mobile);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(String operatorLoginName, EditUserDto dto, String ip) throws CreateUserException {
        checkCreateUserData(dto);

        final String loginName = dto.getLoginName();

        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setMobile(dto.getMobile());
        userModel.setEmail(dto.getEmail());
        if (!Strings.isNullOrEmpty(dto.getReferrer())) {
            userModel.setReferrer(userMapper.findByLoginNameOrMobile(dto.getReferrer()).getLoginName());
        }
        String rndPassword = myShaPasswordEncoder.generateSalt();
        String salt = myShaPasswordEncoder.generateSalt();
        String encodePassword = myShaPasswordEncoder.encodePassword(rndPassword, salt);
        userModel.setSalt(salt);
        userModel.setPassword(encodePassword);
        this.userMapper.create(userModel);

        List<UserRoleModel> userRoleModels = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dto.getRoles())) {
            userRoleModels = Lists.transform(dto.getRoles(), new Function<Role, UserRoleModel>() {
                @Override
                public UserRoleModel apply(Role role) {
                    return new UserRoleModel(loginName, role);
                }
            });
            userRoleMapper.create(userRoleModels);
        }

        if (StringUtils.isNotEmpty(dto.getReferrer())) {
            this.referrerRelationService.generateRelation(userMapper.findByLoginNameOrMobile(dto.getReferrer()).getLoginName(), loginName);
        }

        auditLogService.generateAuditLog(operatorLoginName, null, null, userModel, userRoleModels, ip);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUser(String operatorLoginName, EditUserDto editUserDto, String ip) throws EditUserException {
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
        userModel.setLastModifiedUser(operatorLoginName);
        userMapper.updateUser(userModel);

        //generate audit
        auditLogService.generateAuditLog(operatorLoginName, beforeUpdateUserModel, beforeUpdateUserRoleModels, userModel, afterUpdateUserRoleModels, ip);

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
    public void updateUserStatus(String loginName, UserStatus userStatus, String ip, String operatorLoginName) {
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
        userModel.setLastModifiedUser(operatorLoginName);
        userMapper.updateUser(userModel);
        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", loginName);
        if (userStatus == UserStatus.ACTIVE) {
            redisWrapperClient.del(redisKey);
        } else {
            redisWrapperClient.set(redisKey,String.valueOf(times));
        }
        List<UserRoleModel> userRoles = userRoleMapper.findByLoginName(loginName);

        auditLogService.generateAuditLog(operatorLoginName, beforeUpdateUserModel, userRoles, userModel, userRoles, ip);
    }

    private void checkCreateUserData(EditUserDto editUserDto) throws CreateUserException {
        String loginName = editUserDto.getLoginName();
        if (StringUtils.isEmpty(loginName)){
            throw new CreateUserException("登录名不能为空");
        }
        UserModel editUserModel = userMapper.findByLoginName(loginName);
        if (editUserModel != null) {
            throw new CreateUserException("登录名已经存在");
        }

        String email = editUserDto.getEmail();
        if (StringUtils.isEmpty(email)){
            throw new CreateUserException("邮箱不能为空");
        }
        UserModel userModelByEmail = userMapper.findByEmail(email);
        if (userModelByEmail != null) {
            throw new CreateUserException("邮箱已经存在");
        }

        String mobile = editUserDto.getMobile();
        if (StringUtils.isEmpty(mobile)){
            throw new CreateUserException("手机号不能为空");
        }
        UserModel userModelByMobile = userMapper.findByMobile(mobile);
        if (userModelByMobile != null) {
            throw new CreateUserException("手机号已经存在");
        }

        if (editUserDto.getRoles().contains(Role.STAFF) && !Strings.isNullOrEmpty(editUserDto.getReferrer())) {
            throw new CreateUserException("业务员不能设置推荐人");
        }
    }

    private void checkUpdateUserData(EditUserDto editUserDto) throws EditUserException {
        String loginName = editUserDto.getLoginName();
        UserModel editUserModel = userMapper.findByLoginName(loginName);

        if (editUserModel == null) {
            throw new EditUserException("该用户不存在");
        }

        String newEmail = editUserDto.getEmail();
        UserModel userModelByEmail = userMapper.findByEmail(newEmail);
        if (!Strings.isNullOrEmpty(newEmail) && userModelByEmail != null && !editUserModel.getLoginName().equalsIgnoreCase(userModelByEmail.getLoginName())) {
            throw new EditUserException("该邮箱已经存在");
        }

        String mobile = editUserDto.getMobile();
        UserModel userModelByMobile = userMapper.findByMobile(mobile);
        if (!Strings.isNullOrEmpty(mobile) && userModelByMobile != null && !editUserModel.getLoginName().equalsIgnoreCase(userModelByMobile.getLoginName())) {
            throw new EditUserException("该手机号已经存在");
        }

        if (editUserDto.getRoles().contains(Role.STAFF) && !Strings.isNullOrEmpty(editUserDto.getReferrer())) {
            throw new EditUserException("业务员不能设置推荐人");
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
    public BaseDto<BasePaginationDataDto> findAllUser(String loginName, String email, String mobile, Date beginTime, Date endTime, Role role, String referrer, String channel, Integer index, Integer pageSize) {
        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        List<UserModel> userModels = userMapper.findAllUser(loginName, email, mobile, beginTime, endTime, role, referrer, channel, (index - 1) * pageSize, pageSize);
        List<UserItemDataDto> userItemDataDtos = Lists.newArrayList();
        for (UserModel userModel : userModels) {

            UserItemDataDto userItemDataDto = new UserItemDataDto(userModel);
            userItemDataDto.setUserRoles(userRoleMapper.findByLoginName(userModel.getLoginName()));
            userItemDataDtos.add(userItemDataDto);
        }
        int count = userMapper.findAllUserCount(loginName, email, mobile, beginTime, endTime, role, referrer, channel);
        BasePaginationDataDto<UserItemDataDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, userItemDataDtos);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);
        return baseDto;

    }

    @Override
    public int findUserCount() {
        return userMapper.findUserCount();
    }

    @Override
    public List<String> findLoginNameFromAccountLike(String loginName) {
        return accountMapper.findAllLoginNamesByLike(loginName);
    }

    @Override
    public List<String> findLoginNameLike(String loginName) {
        return userMapper.findLoginNameLike(loginName);
    }

    @Override
    public boolean verifyPasswordCorrect(String loginName, String password) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        return userModel.getPassword().equals(myShaPasswordEncoder.encodePassword(password, userModel.getSalt()));
    }

    @Override
    public List<String> findAllChannels() {
        List<String> channelList = userMapper.findAllChannels();
        return channelList;
    }

}
