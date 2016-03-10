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
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.security.MyAuthenticationManager;
import com.tuotiansudai.service.*;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.task.TaskConstant;
import com.tuotiansudai.util.MobileLocationUtils;
import com.tuotiansudai.util.MyShaPasswordEncoder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.tuotiansudai.task.OperationType;
//import com.tuotiansudai.task.aspect.ApplicationAspect;

@Service
public class UserServiceImpl implements UserService {

    static Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleService userRoleService;

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

    @Autowired
    private BindBankCardService bindBankCardService;

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
    @Transactional(rollbackFor = Exception.class)
    public boolean registerUser(RegisterUserDto dto) throws ReferrerRelationException {
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
        userRoleModel.setLoginName(loginName.toLowerCase());
        userRoleModel.setRole(Role.USER);
        List<UserRoleModel> userRoleModels = Lists.newArrayList();
        userRoleModels.add(userRoleModel);
        this.userRoleMapper.create(userRoleModels);

        if (!Strings.isNullOrEmpty(dto.getReferrer())) {
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
    public boolean changePassword(String loginName, String originalPassword, String newPassword) {

        boolean correct = this.verifyPasswordCorrect(loginName, originalPassword);

        if (!correct) {
            return false;
        }

        UserModel userModel = userMapper.findByLoginName(loginName);
        String mobile = userModel.getMobile();

        String encodedNewPassword = myShaPasswordEncoder.encodePassword(newPassword, userModel.getSalt());
        userMapper.updatePasswordByLoginName(loginName, encodedNewPassword);
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
        UserModel beforeUpdateUserModel;
        try {
            beforeUpdateUserModel = userModel.clone();
        } catch (CloneNotSupportedException e) {
            logger.error(e.getLocalizedMessage(), e);
            return;
        }

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

        // update referrer
        this.referrerRelationService.generateRelation(editUserDto.getReferrer(), editUserDto.getLoginName());

        userModel.setStatus(editUserDto.getStatus());
        userModel.setMobile(mobile);
        userModel.setEmail(editUserDto.getEmail());
        userModel.setReferrer(Strings.isNullOrEmpty(editUserDto.getReferrer()) ? null : editUserDto.getReferrer());
        userModel.setLastModifiedTime(new Date());
        userModel.setLastModifiedUser(operatorLoginName);
        userMapper.updateUser(userModel);

        //generate audit
//        auditLogService.createUserActiveLog(operatorLoginName, beforeUpdateUserModel, beforeUpdateUserRoleModels, userModel, afterUpdateUserRoleModels, ip);

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (!mobile.equals(beforeUpdateUserModel.getMobile()) && accountModel != null) {
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
        userModel.setStatus(userStatus);
        userModel.setLastModifiedTime(new Date());
        userModel.setLastModifiedUser(operatorLoginName);
        userMapper.updateUser(userModel);
        String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", loginName);
        if (userStatus == UserStatus.ACTIVE) {
            redisWrapperClient.del(redisKey);
        } else {
            redisWrapperClient.set(redisKey, String.valueOf(times));
        }

        auditLogService.createUserActiveLog(loginName, operatorLoginName, userStatus, ip);
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

        EditUserDto editUserDto = new EditUserDto(userModel, accountModel, roles);

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
    public List<String> findStaffNameFromUserLike(String loginName) {
        return userMapper.findStaffByLikeLoginName(loginName);
    }

    @Override
    public List<String> findAllLoanerLikeLoginName(String loginName) {
        return accountMapper.findAllLoanerLikeLoginName(loginName);
    }

    @Override
    public List<String> findAccountLikeLoginName(String loginName) {
        return accountMapper.findAccountLikeLoginName(loginName);
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
        return userMapper.findAllChannels();
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
            List<UserModel> userModels = userMapper.findUserByProvince();
            if (CollectionUtils.isEmpty(userModels)) {
                break;
            }
            ((UserService) AopContext.currentProxy()).refreshAreaByMobile(userModels);
        }
    }

    @Override
    public List<UserModel> searchAllUsers(String loginName, String referrer, String mobile, String identityNumber) {
        return userMapper.searchAllUsers(loginName, referrer, mobile, identityNumber);
    }

    @Override
    public List<UserItemDataDto> findUsersAccountBalance(String loginName, String balanceMin, String balanceMax, int currentPageNo, int pageSize) {
        int[] balance = parseBalanceInt(balanceMin, balanceMax);
        List<UserModel> userModels =  userMapper.findUsersAccountBalance(loginName,balance[0], balance[1],  (currentPageNo - 1) * pageSize, pageSize);

        List<UserItemDataDto> userItemDataDtoList = new ArrayList<>();
        for(UserModel userModel : userModels) {
            UserItemDataDto userItemDataDto = new UserItemDataDto(userModel);
            userItemDataDto.setStaff(userRoleService.judgeUserRoleExist(userModel.getLoginName(), Role.STAFF));
            userItemDataDtoList.add(userItemDataDto);
        }
        return userItemDataDtoList;
    }

    @Override
    public int findUsersAccountBalanceCount(String loginName, String balanceMin, String balanceMax) {
        int[] balance = parseBalanceInt(balanceMin, balanceMax);
        return userMapper.findUsersAccountBalanceCount(loginName, balance[0], balance[1]);
    }

    @Override
    public long findUsersAccountBalanceSum(String loginName, String balanceMin, String balanceMax) {
        int[] balance = parseBalanceInt(balanceMin, balanceMax);
        return userMapper.findUsersAccountBalanceSum(loginName, balance[0], balance[1]);
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

    private int[] parseBalanceInt(String balanceMin, String balanceMax) {
        int min, max;
        try {
            min = Integer.parseInt(balanceMin) * 100;
        } catch (NumberFormatException e) {
            min = 0;
            logger.warn("user account balance search parameter wrong, balanceMin is not an integer, balanceMin:" + balanceMin);
        }

        try {
            max = Integer.parseInt(balanceMax) * 100;
        } catch (NumberFormatException e) {
            max = Integer.MAX_VALUE;
            logger.warn("user account balance search parameter wrong, balanceMax is not an integer, balanceMax:" + balanceMax);
        }
        return new int[]{min, max};
    }

}
