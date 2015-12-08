package com.tuotiansudai.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.ReferrerRelationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Service
public class ReferrerRelationServiceImpl implements ReferrerRelationService {

    static Logger logger = Logger.getLogger(ReferrerRelationServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Value("#{'${pay.user.reward}'.split('\\|')}")
    private List<Double> referrerUserRoleReward;

    @Value("#{'${pay.staff.reward}'.split('\\|')}")
    private List<Double> referrerStaffRoleReward;

    @Override
    @Transactional
    public void generateRelation(String newReferrerLoginName, String loginName) throws ReferrerRelationException {
        UserModel userModel = userMapper.findByLoginName(loginName);
        if (userModel == null) {
            logger.error(MessageFormat.format("update referrer failed, due to updated user ({0}) is not exist", loginName));
            throw new ReferrerRelationException("用户不存在");
        }

        UserModel newReferrerModel = userMapper.findByLoginName(newReferrerLoginName);
        if (!Strings.isNullOrEmpty(newReferrerLoginName) && newReferrerModel == null) {
            logger.error(MessageFormat.format("update referrer failed, due to new referrer ({0}) is not exist", newReferrerLoginName));
            throw new ReferrerRelationException("推荐人不存在");
        }

        if (loginName.equalsIgnoreCase(newReferrerLoginName)) {
            logger.error(MessageFormat.format("update referrer failed, due to new referrer ({0}) is same as updated user ({1})", newReferrerLoginName, loginName));
            throw new ReferrerRelationException("不能将推荐人设置为自己");
        }

        // 查找所有 loginName 推荐的人
        Map<String, Integer> allLowerRelations = this.findAllLowerRelations(loginName);

        if (allLowerRelations.containsKey(newReferrerLoginName)) {
            logger.error(MessageFormat.format("update referrer failed, due to update user {0} is new referrer {1}'s referrer ", loginName));
            throw new ReferrerRelationException("推荐人与该用户存在间接推荐关系");
        }

        // 查找所有 loginName 的推荐人关系
        Map<String, Integer>  allUpperRelations = this.findAllUpperRelations(loginName);

        // 删除推荐关系
        for (String upperReferrer : allUpperRelations.keySet()) {
            for (String lowerUser : allLowerRelations.keySet()) {
                referrerRelationMapper.delete(upperReferrer, lowerUser);
            }
        }

        for (Map.Entry<String, Integer> lowerUserRelation : allLowerRelations.entrySet()) {
            String userLoginName = lowerUserRelation.getKey();
            if (!loginName.equalsIgnoreCase(userLoginName)) {
                List<UserRoleModel> roleModels = userRoleMapper.findByLoginName(loginName);
                Optional<UserRoleModel> isStaff = Iterators.tryFind(roleModels.iterator(), new Predicate<UserRoleModel>() {
                    @Override
                    public boolean apply(UserRoleModel input) {
                        return input.getRole() == Role.STAFF;
                    }
                });
                int maxLevel = isStaff.isPresent() ? this.referrerStaffRoleReward.size() : this.referrerUserRoleReward.size();
                if (lowerUserRelation.getValue() <= maxLevel) {
                    ReferrerRelationModel newRelation = new ReferrerRelationModel();
                    newRelation.setReferrerLoginName(loginName.toLowerCase());
                    newRelation.setLoginName(userLoginName.toLowerCase());
                    newRelation.setLevel(lowerUserRelation.getValue());
                    referrerRelationMapper.create(newRelation);
                }
            }
        }

        if (Strings.isNullOrEmpty(newReferrerLoginName)) {
            return;
        }

        // 查找所有 newReferrerLoginName 的推荐人关系
        Map<String, Integer> allNewUpperRelations = this.findAllUpperRelations(newReferrerLoginName);
        for (Map.Entry<String, Integer> newUpperRelation : allNewUpperRelations.entrySet()) {
            for (Map.Entry<String, Integer> lowerUserRelation : allLowerRelations.entrySet()) {
                String referrerLoginName = newUpperRelation.getKey();
                String userLoginName = lowerUserRelation.getKey();
                if (!referrerLoginName.equalsIgnoreCase(userLoginName)) {
                    List<UserRoleModel> roleModels = userRoleMapper.findByLoginName(referrerLoginName);
                    Optional<UserRoleModel> isStaff = Iterators.tryFind(roleModels.iterator(), new Predicate<UserRoleModel>() {
                        @Override
                        public boolean apply(UserRoleModel input) {
                            return input.getRole() == Role.STAFF;
                        }
                    });
                    int maxLevel = isStaff.isPresent() ? this.referrerStaffRoleReward.size() : this.referrerUserRoleReward.size();
                    int level = newUpperRelation.getValue() + lowerUserRelation.getValue() + 1;
                    if (level <= maxLevel) {
                        ReferrerRelationModel newRelation = new ReferrerRelationModel();
                        newRelation.setReferrerLoginName(referrerLoginName.toLowerCase());
                        newRelation.setLoginName(userLoginName.toLowerCase());
                        newRelation.setLevel(level);
                        referrerRelationMapper.create(newRelation);
                    }
                }
            }
        }


    }

    private Map<String, Integer> findAllUpperRelations(String loginName) {
        Map<Integer, List<String>> allUpperUsers = Maps.newHashMap(ImmutableMap.<Integer, List<String>>builder()
                .put(0, Lists.newArrayList(loginName))
                .put(1, Lists.<String>newArrayList())
                .put(2, Lists.<String>newArrayList())
                .put(3, Lists.<String>newArrayList())
                .put(4, Lists.<String>newArrayList())
                .build());

        Map<String, Integer> relations = Maps.newHashMap();
        relations.put(loginName, 0);
        for (int level = 1; level <= 4; level++) {
            List<String> upperLoginNames = allUpperUsers.get(level - 1);
            for (String upperLoginName : upperLoginNames) {
                List<ReferrerRelationModel> referrerRelationModels = referrerRelationMapper.findByLoginNameAndLevel(upperLoginName, 1);
                for (ReferrerRelationModel referrerRelationModel : referrerRelationModels) {
                    allUpperUsers.get(level).add(referrerRelationModel.getReferrerLoginName());
                    relations.put(referrerRelationModel.getReferrerLoginName(), level);
                }
            }
        }
        return relations;
    }

    private Map<String, Integer> findAllLowerRelations(String loginName) {
        Map<Integer, List<String>> allLowerUsers = Maps.newHashMap(ImmutableMap.<Integer, List<String>>builder()
                .put(0, Lists.newArrayList(loginName))
                .put(1, Lists.<String>newArrayList())
                .put(2, Lists.<String>newArrayList())
                .put(3, Lists.<String>newArrayList())
                .put(4, Lists.<String>newArrayList())
                .build());

        Map<String, Integer> relations = Maps.newHashMap();
        relations.put(loginName, 0);
        for (int level = 1; level <= 4; level++) {
            List<String> lowerLoginNames = allLowerUsers.get(level - 1);
            for (String lowerLoginName : lowerLoginNames) {
                List<ReferrerRelationModel> referrerRelationModels = referrerRelationMapper.findByReferrerLoginNameAndLevel(lowerLoginName, 1);
                for (ReferrerRelationModel referrerRelationModel : referrerRelationModels) {
                    allLowerUsers.get(level).add(referrerRelationModel.getLoginName());
                    relations.put(referrerRelationModel.getLoginName(), level);
                }
            }
        }
        return relations;
    }
}
