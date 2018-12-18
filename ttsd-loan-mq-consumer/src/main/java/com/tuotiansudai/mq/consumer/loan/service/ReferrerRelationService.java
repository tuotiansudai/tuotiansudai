package com.tuotiansudai.mq.consumer.loan.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ReferrerRelationService {

    private final static Logger logger = Logger.getLogger(ReferrerRelationService.class);

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Value("#{'${pay.user.reward}'.split('\\|')}")
    private List<Double> referrerUserRoleReward;

    @Value("#{'${pay.staff.reward}'.split('\\|')}")
    private List<Double> referrerStaffRoleReward;

    @Transactional
    public void generateRelation(String newReferrerLoginName, String loginName) {
        // 查找所有 loginName 推荐的人
        Map<String, Integer> allLowerRelations = this.findAllLowerRelations(loginName);

        // 查找所有 loginName 的推荐人关系
        Map<String, Integer> allUpperRelations = this.findAllUpperRelations(loginName);

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
                boolean isStaff = roleModels.stream().anyMatch(roleModel -> Lists.newArrayList(Role.ZC_STAFF, Role.SD_STAFF).contains(roleModel.getRole()));
                int maxLevel = isStaff ? this.referrerStaffRoleReward.size() : this.referrerUserRoleReward.size();
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
                    boolean isStaff = roleModels.stream().anyMatch(roleModel -> Lists.newArrayList(Role.ZC_STAFF, Role.SD_STAFF).contains(roleModel.getRole()));
                    int maxLevel = isStaff ? this.referrerStaffRoleReward.size() : this.referrerUserRoleReward.size();
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

    public String findStaffReferrerMobileByLoginName(String loginName){
        return referrerRelationMapper.findStaffReferrerMobileByLoginName(loginName);
    }
}
