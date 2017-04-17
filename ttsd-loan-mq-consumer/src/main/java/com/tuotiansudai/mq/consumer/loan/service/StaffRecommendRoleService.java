package com.tuotiansudai.mq.consumer.loan.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffRecommendRoleService {

    private final static Logger logger = Logger.getLogger(StaffRecommendRoleService.class);

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    // 更新 NOT_STAFF_RECOMMEND SD_STAFF_RECOMMEND ZC_STAFF_RECOMMEND
    public void generateStaffRole(String newReferrerLoginName, String loginName) {
        logger.info(MessageFormat.format("[StaffRecommendRole] referrer:{0}, user:{1}", newReferrerLoginName, loginName));
        Role role = Role.NOT_STAFF_RECOMMEND;
        if (Strings.isNullOrEmpty(newReferrerLoginName)) {
            List<Role> userRoles = userRoleMapper.findByLoginName(loginName).stream().map(UserRoleModel::getRole).collect(Collectors.toList());
            if (userRoles.contains(Role.ZC_STAFF)) {
                role = Role.ZC_STAFF_RECOMMEND;
            }

            if (userRoles.contains(Role.SD_STAFF)) {
                role = Role.SD_STAFF_RECOMMEND;
            }
        } else {
            List<Role> newReferrerRoles = userRoleMapper.findByLoginName(newReferrerLoginName).stream().map(UserRoleModel::getRole).collect(Collectors.toList());
            if (newReferrerRoles.contains(Role.ZC_STAFF) || newReferrerRoles.contains(Role.ZC_STAFF_RECOMMEND)) {
                role = Role.ZC_STAFF_RECOMMEND;
            }

            if (newReferrerRoles.contains(Role.SD_STAFF) || newReferrerRoles.contains(Role.SD_STAFF_RECOMMEND)) {
                role = Role.SD_STAFF_RECOMMEND;
            }
        }

        //newReferrerLoginName 推荐的所有用户
        List<String> allUsers = Lists.newArrayList(loginName);
        //当前需要计算的推荐人，需要计算这些用户推荐的一级用户
        List<String> currentLevelReferrers = Lists.newArrayList(loginName);
        do {
            //currentLevelReferrers 这些用户推荐的所有一级用户
            List<String> users = Lists.newArrayList();
            for (String currentLevelReferrer : currentLevelReferrers) {
                users.addAll(referrerRelationMapper.findByReferrerLoginNameAndLevel(currentLevelReferrer, 1).stream().map(ReferrerRelationModel::getLoginName).collect(Collectors.toList()));
            }

            currentLevelReferrers = users;
            allUsers.addAll(users);
        } while (CollectionUtils.isNotEmpty(currentLevelReferrers));

        List<UserRoleModel> userRoleModels = Lists.newArrayList();

        for (String user : allUsers) {
            if (user.equalsIgnoreCase(loginName)
                    && userRoleMapper.findByLoginName(loginName)
                    .stream()
                    .anyMatch(userRoleModel -> Lists.newArrayList(Role.SD_STAFF, Role.ZC_STAFF).contains(userRoleModel.getRole()))) {
                continue;
            }
            userRoleMapper.deleteByLoginNameAndRole(user, Role.NOT_STAFF_RECOMMEND);
            userRoleMapper.deleteByLoginNameAndRole(user, Role.ZC_STAFF_RECOMMEND);
            userRoleMapper.deleteByLoginNameAndRole(user, Role.SD_STAFF_RECOMMEND);
            userRoleModels.add(new UserRoleModel(user, role));
            logger.info(MessageFormat.format("[StaffRecommendRole] user:{0}, lower user:{1}, role:{2}", loginName, user, role.name()));
        }

        if (CollectionUtils.isNotEmpty(userRoleModels)) {
            userRoleMapper.create(userRoleModels);
        }
    }
}
