package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.UserInfoLogService;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.security.MyAuthenticationManager;
import com.tuotiansudai.utils.LoginUserInfo;
import com.tuotiansudai.utils.MyShaPasswordEncoder;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserInfoLogService userInfoLogService;

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
        boolean verifyCaptchaFailed = !this.smsCaptchaService.verifyRegisterCaptcha(dto.getMobile(), dto.getCaptcha());

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
        this.userRoleMapper.create(userRoleModel);

        String referrerId = dto.getReferrer();
        if (StringUtils.isNotEmpty(referrerId)) {
            saveReferrerRelations(referrerId, dto.getLoginName());
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
    public void saveReferrerRelations(String referrerLoginName, String loginName) {
        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();
        referrerRelationModel.setReferrerLoginName(referrerLoginName);
        referrerRelationModel.setLoginName(loginName);
        referrerRelationModel.setLevel(1);
        referrerRelationMapper.create(referrerRelationModel);

        List<ReferrerRelationModel> list = referrerRelationMapper.findByLoginName(referrerLoginName);

        for (ReferrerRelationModel model : list) {
            ReferrerRelationModel upperRelation = new ReferrerRelationModel();
            upperRelation.setReferrerLoginName(model.getReferrerLoginName());
            upperRelation.setLoginName(loginName);
            upperRelation.setLevel(model.getLevel() + 1);
            referrerRelationMapper.create(upperRelation);
        }
    }

    @Override
    public void updatePassword(String mobile, String password) {
        userMapper.updatePassword(mobile, password);
    }

    @Override
    @Transactional
    public boolean changePassword(String loginName, String oldPassword, String newPassword) {
        if (StringUtils.isBlank(loginName)) {
            return false;
        }
        UserModel userModel = userMapper.findByLoginName(loginName);
        if (userModel == null) {
            return false;
        }
        String encOldPassword = myShaPasswordEncoder.encodePassword(oldPassword, userModel.getSalt());
        if (!StringUtils.equals(encOldPassword, userModel.getPassword())) {
            return false;
        }
        String encNewPassword = myShaPasswordEncoder.encodePassword(newPassword, userModel.getSalt());
        userMapper.updatePassword(userModel.getMobile(), encNewPassword);
        smsWrapperClient.sendPasswordChangedNotify(userModel.getMobile());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> editUser(EditUserDto editUserDto,HttpServletRequest request) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        String message = checkUser(editUserDto);
        if(StringUtils.isNotEmpty(message)){
            baseDto.setSuccess(true);
            payDataDto.setStatus(false);
            payDataDto.setMessage(message);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        String newMobile = editUserDto.getMobile();
        UserModel userModel = userMapper.findByLoginName(editUserDto.getLoginName());
        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(editUserDto.getLoginName());
        AccountModel accountModel = accountMapper.findByLoginNameOrderByTime(editUserDto.getLoginName());
        if(userModel != null && !newMobile.equals(userModel.getMobile()) && accountModel!=null){
            RegisterAccountDto registerAccountDto = new RegisterAccountDto(userModel.getLoginName(),
                                                                        newMobile,
                                                                        accountModel.getUserName(),
                                                                        accountModel.getIdentityNumber());
            BaseDto<PayDataDto> accountBaseDto = this.registerAccount(registerAccountDto);
            if(!accountBaseDto.getData().getStatus()){
                baseDto.setSuccess(true);
                payDataDto.setStatus(false);
                payDataDto.setMessage(accountBaseDto.getData().getMessage());
                baseDto.setData(payDataDto);
                return baseDto;
            }
        }

        try {
            UserModel userModelEdit = (UserModel)this.copy(userModel);
            userModelEdit.convert(editUserDto);
            userModelEdit.setLastModifiedUser(LoginUserInfo.getLoginName());
            userMapper.updateUser(userModelEdit);
            userRoleMapper.delete(editUserDto.getLoginName());
            userRoleMapper.createUserRoles(editUserDto.getUserRoles());
            String userString = userInfoLogService.generateUserInfoString(userModelEdit, editUserDto.getUserRoles(),
                    userModel, userRoleModels);
            userInfoLogService.logUserOperation(editUserDto.getLoginName(), "修改了用户信息：" + userString, true,request);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(),e);
            baseDto.setSuccess(true);
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getMessage());
            baseDto.setData(payDataDto);
            return baseDto;

        }

        payDataDto.setStatus(true);
        baseDto.setSuccess(true);
        baseDto.setData(payDataDto);
        return baseDto;
    }

    private String checkUser(EditUserDto editUserDto) {
        String email = editUserDto.getEmail();
        if (StringUtils.isNotEmpty(email)) {
            UserModel userModel = userMapper.findByEmail(email);
            if (userModel != null) {
                return "该邮箱已经存在";
            }
        }
        String mobile = editUserDto.getMobile();
        if (StringUtils.isNotEmpty(mobile)) {
            UserModel userModel = userMapper.findByMobile(mobile);
            if (userModel != null && !userModel.getLoginName().equals(editUserDto.getLoginName())) {
                return "该手机号已经存在";
            }
        }
        return checkReferrer(editUserDto);
    }

    private String checkReferrer(EditUserDto editUserDto) {
        String referrer = editUserDto.getReferrer();
        if (StringUtils.isBlank(referrer)) {
            return null;
        } else {
            UserModel userModel = userMapper.findByLoginName(referrer);
            if (userModel == null){
                return "设置的推荐人不存在";
            }
            if (editUserDto.getLoginName().equalsIgnoreCase(editUserDto.getReferrer())){
                return "不能将推荐人设置为自已";
            }

        }

        List<UserRoleModel> userRoleModels = editUserDto.getUserRoles();
        if (userRoleModels == null || userRoleModels.size() == 0) {
            return null;
        }
        for (UserRoleModel userRoleModel : userRoleModels) {
            if (userRoleModel.getRole() == Role.MERCHANDISER) {
                return "有推荐人的用户不允许添加业务员角色!";

            }
        }

        if (hasDiffReferrerRelation(editUserDto.getLoginName(), referrer)){
            return "设置的推荐人与本用户存在间接推荐关系，不能设置为本用户的推荐人";
        }
        return null;

    }
    private boolean hasDiffReferrerRelation(String loginName,String referrer){

        long count = referrerRelationMapper.findByLoginNameAndReferrer(loginName, referrer);
        if (count > 0){
            return true;
        }
        return false;

    }

    @Override
    public EditUserDto getUser(String loginName) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(loginName);
        AccountModel accountModel = accountMapper.findByLoginName(loginName);

        return new EditUserDto(userModel, accountModel, userRoleModels);
    }

    public Object copy(Object object) throws Exception
    {
        Class<?> classType = object.getClass();

        Object objectCopy = classType.getConstructor(new Class[]{}).newInstance(new Object[]{});

        //获得对象的所有成员变量
        Field[] fields = classType.getDeclaredFields();
        for(Field field : fields)
        {
            //获取成员变量的名字
            String name = field.getName();    //获取成员变量的名字，此处为id，name,age
            //System.out.println(name);

            //获取get和set方法的名字
            String firstLetter = name.substring(0,1).toUpperCase();    //将属性的首字母转换为大写
            String getMethodName = "get" + firstLetter + name.substring(1);
            String setMethodName = "set" + firstLetter + name.substring(1);
            //System.out.println(getMethodName + "," + setMethodName);

            //获取方法对象
            Method getMethod = classType.getMethod(getMethodName, new Class[]{});
            Method setMethod = classType.getMethod(setMethodName, new Class[]{field.getType()});//注意set方法需要传入参数类型

            //调用get方法获取旧的对象的值
            Object value = getMethod.invoke(object, new Object[]{});
            //调用set方法将这个值复制到新的对象中去
            setMethod.invoke(objectCopy, new Object[]{value});

        }

        return objectCopy;

    }


}
