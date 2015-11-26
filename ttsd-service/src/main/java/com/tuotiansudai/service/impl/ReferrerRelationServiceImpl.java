package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.exception.EditUserException;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.ReferrerRelationService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
public class ReferrerRelationServiceImpl implements ReferrerRelationService {

    static Logger logger = Logger.getLogger(ReferrerRelationServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    private static final int MAX_REFERRER_RELATION_LEVEL = 4;

    @Override
    @Transactional
    public void generateRelation(String referrerLoginName, String loginName) {
        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();
        referrerRelationModel.setReferrerLoginName(referrerLoginName);
        referrerRelationModel.setLoginName(loginName);
        referrerRelationModel.setLevel(1);
        referrerRelationMapper.create(referrerRelationModel);

        List<ReferrerRelationModel> list = referrerRelationMapper.findByLoginName(referrerLoginName);

        for (ReferrerRelationModel model : list) {
            int level = model.getLevel() + 1;
            if (level <= MAX_REFERRER_RELATION_LEVEL) {
                ReferrerRelationModel upperRelation = new ReferrerRelationModel();
                upperRelation.setReferrerLoginName(model.getReferrerLoginName());
                upperRelation.setLoginName(loginName);
                upperRelation.setLevel(level);
                referrerRelationMapper.create(upperRelation);
            }
        }
    }

    @Override
    @Transactional
    public void updateRelation(String newReferrerLoginName, String loginName) throws EditUserException {
        UserModel userModel = userMapper.findByLoginName(loginName);
        if (userModel == null) {
            logger.error(MessageFormat.format("update referrer failed, due to updated user ({0}) is not exist", loginName));
            throw new EditUserException("用户不存在");
        }

        UserModel newReferrerModel = userMapper.findByLoginName(newReferrerLoginName);
        if (!Strings.isNullOrEmpty(newReferrerLoginName) && newReferrerModel == null) {
            logger.error(MessageFormat.format("update referrer failed, due to new referrer ({0}) is not exist", newReferrerLoginName));
            throw new EditUserException("推荐人不存在");
        }

        if (loginName.equalsIgnoreCase(newReferrerLoginName)) {
            logger.error(MessageFormat.format("update referrer failed, due to new referrer ({0}) is same as updated user ({1})", newReferrerLoginName, loginName));
            throw new EditUserException("不能将推荐人设置为自己");
        }

        // 查找所有 loginName 推荐的人关系
        List<ReferrerRelationModel> allLowerRelations = referrerRelationMapper.findByReferrerLoginName(loginName);
        // 所有 loginName 推荐的人
        List<String> allLowerUsers = Lists.newArrayList(loginName);
        if (CollectionUtils.isNotEmpty(allLowerRelations)) {
            allLowerUsers.addAll(Lists.transform(allLowerRelations, new Function<ReferrerRelationModel, String>() {
                @Override
                public String apply(ReferrerRelationModel input) {
                    return input.getLoginName();
                }
            }));
        }

        if (allLowerUsers.contains(newReferrerLoginName)) {
            logger.error(MessageFormat.format("update referrer failed, due to update user {0} is new referrer {1}'s referrer ", loginName));
            throw new EditUserException("推荐人与该用户存在间接推荐关系");
        }

        // 查找所有 loginName 的推荐人关系
        List<ReferrerRelationModel> allUpperRelations = referrerRelationMapper.findByLoginName(loginName);
        // 所有 loginName 的推荐人
        List<String> allUpperReferrers = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(allUpperRelations)) {
            allUpperReferrers = Lists.transform(allUpperRelations, new Function<ReferrerRelationModel, String>() {
                @Override
                public String apply(ReferrerRelationModel input) {
                    return input.getReferrerLoginName();
                }
            });
        }

        // 删除推荐关系
        for (String upperReferrer : allUpperReferrers) {
            for (String lowerUser : allLowerUsers) {
                referrerRelationMapper.delete(upperReferrer, lowerUser);
            }
        }

        if (Strings.isNullOrEmpty(newReferrerLoginName)) {
            return;
        }

        // 查找所有 newReferrerLoginName 的推荐人关系
        List<ReferrerRelationModel> allNewUpperRelations = referrerRelationMapper.findByLoginName(newReferrerLoginName);
        // 所有 newReferrerLoginName 推荐的人
        List<String> allNewUpperReferrers = Lists.newArrayList(newReferrerLoginName);
        if (CollectionUtils.isNotEmpty(allNewUpperRelations)) {
            allNewUpperReferrers.addAll(Lists.transform(allNewUpperRelations, new Function<ReferrerRelationModel, String>() {
                @Override
                public String apply(ReferrerRelationModel input) {
                    return input.getReferrerLoginName();
                }
            }));
        }

        List<ReferrerRelationModel> newRelations = Lists.newArrayList();
        for (String newUpperReferrer : allNewUpperReferrers) {
            ReferrerRelationModel newUpperRelation = referrerRelationMapper.findByReferrerAndLoginName(newUpperReferrer, newReferrerLoginName);
            for (String lowerUser : allLowerUsers) {
                ReferrerRelationModel newLowerRelation = referrerRelationMapper.findByReferrerAndLoginName(loginName, lowerUser);
                int level = (newUpperRelation != null ? newUpperRelation.getLevel() : 0) + (newLowerRelation != null ? newLowerRelation.getLevel() : 0) + 1;
                if (level <= MAX_REFERRER_RELATION_LEVEL) {
                    ReferrerRelationModel newRelation = new ReferrerRelationModel();
                    newRelation.setReferrerLoginName(newUpperReferrer);
                    newRelation.setLoginName(lowerUser);
                    newRelation.setLevel(level);
                    newRelations.add(newRelation);
                }
            }
        }

        for (ReferrerRelationModel newRelation : newRelations) {
            referrerRelationMapper.create(newRelation);
        }
    }
}
