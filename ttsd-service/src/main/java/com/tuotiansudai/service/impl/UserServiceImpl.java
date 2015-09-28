package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.utils.MyShaPasswordEncoder;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return true;
    }

    @Override
    public BaseDto<PayDataDto> registerAccount(RegisterAccountDto dto) {
        return payWrapperClient.register(dto);
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
    @Transactional
    public void editUser() {


    }

    @Override
    public EditUserDto getUser(long id) {


    }


}
