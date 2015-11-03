package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.UserAuditLogMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.UserAuditLogModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.UserAuditLogService;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserAuditLogServiceImpl implements UserAuditLogService {

    @Autowired
    private UserAuditLogMapper userAuditLogMapper;

    private static String AUDIT_LOG_TEMPLATE = "'{'loginName:{0}, password:{1}, mobile:{2}, email:{3}, referrer:{4}, status:{5}, roles:[{6}]'}'";

    @Override
    @Transactional
    public void generateAuditLog(UserModel beforeUpdateUserModel, List<UserRoleModel> beforeUpdateUserRoleModels,
                                 UserModel afterUpdateUserModel, List<UserRoleModel> afterUpdateUserRoleModels,
                                 String userIp) {
        String beforeUpdate = MessageFormat.format(AUDIT_LOG_TEMPLATE,
                beforeUpdateUserModel.getLoginName(),
                beforeUpdateUserModel.getPassword(),
                beforeUpdateUserModel.getMobile(),
                beforeUpdateUserModel.getEmail(),
                beforeUpdateUserModel.getReferrer(),
                beforeUpdateUserModel.getStatus().name(),
                Joiner.on(",").join(Lists.transform(beforeUpdateUserRoleModels, new Function<UserRoleModel, String>() {
                    @Override
                    public String apply(UserRoleModel input) {
                        return input.getRole().name();
                    }
                })));

        String afterUpdate = MessageFormat.format(AUDIT_LOG_TEMPLATE,
                afterUpdateUserModel.getLoginName(),
                afterUpdateUserModel.getPassword(),
                afterUpdateUserModel.getMobile(),
                afterUpdateUserModel.getEmail(),
                afterUpdateUserModel.getReferrer(),
                afterUpdateUserModel.getStatus().name(),
                Joiner.on(",").join(Lists.transform(afterUpdateUserRoleModels, new Function<UserRoleModel, String>() {
                    @Override
                    public String apply(UserRoleModel input) {
                        return input.getRole().name();
                    }
                })));

        UserAuditLogModel log = new UserAuditLogModel();
        log.setId(new IdGenerator().generate());
        log.setOperatorLoginName(LoginUserInfo.getLoginName());
        log.setIp(userIp);
        log.setDescription(beforeUpdate + " => " + afterUpdate);
        userAuditLogMapper.create(log);
    }

}
