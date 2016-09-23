package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.EditUserException;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.*;
import com.tuotiansudai.util.MobileLocationUtils;
import com.tuotiansudai.util.MyShaPasswordEncoder;
import com.tuotiansudai.util.RandomStringGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
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
    private BindBankCardService bindBankCardService;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private PrepareUserMapper prepareUserMapper;

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    public static String SHA = "SHA";

    private final static int LOGIN_NAME_LENGTH = 8;

    @Override
    public String getMobile(String loginName) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        return userModel != null ? userModel.getMobile() : null;
    }

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
    @Transactional(rollbackFor = Exception.class)
    public boolean registerUser(RegisterUserDto dto) throws ReferrerRelationException {
        boolean loginNameIsExist = false;
        String loginName;
        if (StringUtils.isNotEmpty(dto.getLoginName())) {
            loginName = dto.getLoginName();
            loginNameIsExist = this.loginNameIsExist(loginName);
        } else {
            int count = 0;
            loginName = RandomStringGenerator.generate(LOGIN_NAME_LENGTH);
            while (loginNameIsExist(loginName)) {
                loginName = RandomStringGenerator.generate(LOGIN_NAME_LENGTH);
                ++count;
                if (count > 20) {
                    logger.debug(MessageFormat.format("[UserServiceImpl][registerUser] generate loginName failed! mobile:{0}", dto.getMobile()));
                    return false;
                }
            }
            dto.setLoginName(loginName);
        }
        boolean mobileIsExist = this.mobileIsExist(dto.getMobile());
        PrepareUserModel prepareUserModel = prepareUserMapper.findByMobile(dto.getMobile());
        if (prepareUserModel != null) {
            dto.setReferrer(prepareUserModel.getReferrerMobile());
        }
        boolean referrerIsNotExist = !Strings.isNullOrEmpty(dto.getReferrer()) && !this.loginNameOrMobileIsExist(dto.getReferrer());
        boolean verifyCaptchaFailed = !this.smsCaptchaService.verifyMobileCaptcha(dto.getMobile(), dto.getCaptcha(), CaptchaType.REGISTER_CAPTCHA);

        if (loginNameIsExist || mobileIsExist || referrerIsNotExist || verifyCaptchaFailed) {
            return false;
        }

        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setMobile(dto.getMobile());
        userModel.setSource(dto.getSource());
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
        userModel.setLastModifiedTime(new Date());
        this.userMapper.create(userModel);

        UserRoleModel userRoleModel = new UserRoleModel();
        userRoleModel.setLoginName(userModel.getLoginName().toLowerCase());
        userRoleModel.setRole(Role.USER);
        List<UserRoleModel> userRoleModels = Lists.newArrayList();
        userRoleModels.add(userRoleModel);
        this.userRoleMapper.create(userRoleModels);

        if (!Strings.isNullOrEmpty(dto.getReferrer())) {
            this.referrerRelationService.generateRelation(userMapper.findByLoginNameOrMobile(dto.getReferrer()).getLoginName(), userModel.getLoginName());
        }

        MembershipModel membershipModel = membershipMapper.findByLevel(0);
        UserMembershipModel userMembershipModel = UserMembershipModel.createUpgradeUserMembershipModel(userModel.getLoginName(), membershipModel.getId());
        userMembershipMapper.create(userMembershipModel);

        return true;
    }

    @Override
    public BaseDto<PayDataDto> registerAccount(RegisterAccountDto dto) {
        dto.setMobile(userMapper.findByLoginName(dto.getLoginName()).getMobile());
        return payWrapperClient.register(dto);
    }

    @Override
    @Transactional
    public boolean changePassword(String loginName, String originalPassword, String newPassword, String ip, String platform, String deviceId) {

        boolean correct = this.verifyPasswordCorrect(loginName, originalPassword);

        if (!correct) {
            return false;
        }

        UserModel userModel = userMapper.findByLoginName(loginName);
        String mobile = userModel.getMobile();

        userModel.setPassword(myShaPasswordEncoder.encodePassword(newPassword, userModel.getSalt()));
        userMapper.updateUser(userModel);
        smsWrapperClient.sendPasswordChangedNotify(mobile);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUser(String operatorLoginName, EditUserDto editUserDto, String ip) throws EditUserException, ReferrerRelationException {
        this.checkUpdateUserData(editUserDto);

        final String loginName = editUserDto.getLoginName();

        String mobile = editUserDto.getMobile();
        UserModel userModel = userMapper.findByLoginName(loginName);
        String beforeUpdateUserMobile = userModel.getMobile();

        // update role
        userRoleMapper.deleteByLoginName(loginName);
        if (CollectionUtils.isNotEmpty(editUserDto.getRoles())) {
            List<UserRoleModel> afterUpdateUserRoleModels = Lists.transform(editUserDto.getRoles(), new Function<Role, UserRoleModel>() {
                @Override
                public UserRoleModel apply(Role role) {
                    return new UserRoleModel(loginName, role);
                }
            });
            userRoleMapper.create(afterUpdateUserRoleModels);
        }

        // update referrer
        this.referrerRelationService.generateRelation(editUserDto.getReferrer(), editUserDto.getLoginName());

        userModel.setStatus(editUserDto.getStatus());
        userModel.setMobile(mobile);
        userModel.setEmail(editUserDto.getEmail());
        userModel.setReferrer(Strings.isNullOrEmpty(editUserDto.getReferrer()) ? null : editUserDto.getReferrer());
        userModel.setLastModifiedTime(new Date());
        userModel.setLastModifiedUser(operatorLoginName);
        userMapper.updateUser(userModel);

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (!mobile.equals(beforeUpdateUserMobile) && accountModel != null) {
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

    private void checkUpdateUserData(EditUserDto editUserDto) throws EditUserException {
        String loginName = editUserDto.getLoginName();
        UserModel editUserModel = userMapper.findByLoginName(loginName);

        if (editUserModel == null) {
            throw new EditUserException("该用户不存在");
        }

        String newEmail = editUserDto.getEmail();
        if (!Strings.isNullOrEmpty(newEmail) && userMapper.findByEmail(newEmail) != null && !editUserModel.getLoginName().equalsIgnoreCase(userMapper.findByEmail(newEmail).getLoginName())) {
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

        AutoInvestPlanModel autoInvestPlanModel = autoInvestPlanMapper.findByLoginName(loginName);

        EditUserDto editUserDto = new EditUserDto(userModel, accountModel, roles, autoInvestPlanModel);

        BankCardModel bankCard = bindBankCardService.getPassedBankCard(loginName);
        if (bankCard != null) {
            editUserDto.setBankCardNumber(bankCard.getCardNumber());
        }

        if (userRoleMapper.findByLoginNameAndRole(userModel.getReferrer(), Role.STAFF.name()) != null) {
            editUserDto.setReferrerStaff(true);
        }
        return editUserDto;
    }

    @Override
    public boolean verifyPasswordCorrect(String loginName, String password) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        return userModel.getPassword().equals(myShaPasswordEncoder.encodePassword(password, userModel.getSalt()));
    }

    @Override
    public List<String> findAllUserChannels() {
        return userMapper.findAllUserChannels();
    }

    @Transactional
    @Override
    public void refreshAreaByMobile(List<UserModel> userModels) {
        for (UserModel userModel : userModels) {
            String phoneMobile = userModel.getMobile();
            if (StringUtils.isNotEmpty(phoneMobile)) {
                String[] provinceAndCity = MobileLocationUtils.locateMobileNumber(phoneMobile);
                if (StringUtils.isEmpty(provinceAndCity[0])) {
                    provinceAndCity[0] = "未知";
                }
                if (StringUtils.isEmpty(provinceAndCity[1])) {
                    provinceAndCity[1] = "未知";
                }
                userModel.setProvince(provinceAndCity[0]);
                userModel.setCity(provinceAndCity[1]);
            } else {
                userModel.setProvince("未知");
                userModel.setCity("未知");
            }
            userModel.setLastModifiedTime(new Date());
            userMapper.updateUser(userModel);
        }
    }

    @Override
    public void refreshAreaByMobileInJob() {
        while (true) {
            List<UserModel> userModels = userMapper.findUsersByProvince();
            if (CollectionUtils.isEmpty(userModels)) {
                break;
            }
            ((UserService) AopContext.currentProxy()).refreshAreaByMobile(userModels);
        }
    }

    @Override
    public boolean mobileIsRegister(String mobile) {
        return mobileIsExist(mobile) || prepareUserMapper.findByMobile(mobile) != null;
    }

    @Override
    public UserModel findByMobile(String mobile) {
        return userMapper.findByMobile(mobile);
    }

    @Override
    public boolean resetUmpayPassword(String loginName, String identityNumber) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel == null || !accountModel.getIdentityNumber().equals(identityNumber)) {
            return false;
        }
        ResetUmpayPasswordDto resetUmpayPasswordDto = new ResetUmpayPasswordDto(loginName, identityNumber);
        return payWrapperClient.resetUmpayPassword(resetUmpayPasswordDto);
    }
}
