package com.tuotiansudai.membership.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Ints;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserMembershipEvaluatorImpl implements UserMembershipEvaluator {

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private AccountMapper accountMapper;


    @Override
    public MembershipModel evaluate(String loginName) {
        List<UserMembershipModel> userMembershipModels = userMembershipMapper.findByLoginName(loginName);

        if (CollectionUtils.isEmpty(userMembershipModels)) {
            return null;
        }

        UnmodifiableIterator<UserMembershipModel> filter = Iterators.filter(userMembershipModels.iterator(), new Predicate<UserMembershipModel>() {
            @Override
            public boolean apply(UserMembershipModel input) {
                return input.getExpiredTime().after(new Date());
            }
        });

        UserMembershipModel max = new Ordering<UserMembershipModel>() {
            @Override
            public int compare(UserMembershipModel left, UserMembershipModel right) {
                return Ints.compare(membershipMapper.findById(left.getMembershipId()).getLevel(), membershipMapper.findById(right.getMembershipId()).getLevel());
            }
        }.max(filter);

        return membershipMapper.findById(max.getMembershipId());
    }

    @Override
    public MembershipModel getMembershipByLevel(int level){
        return membershipMapper.findByLevel(level);
    }

    @Override
    public int getProgressBarPercent(String loginName) {
        int progressBarPercent = 0;
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        //分两部分 1.国定 V0 20 V1 40 V3 60 V4 80 V5 100
        //        2. 变化部分  计算公式  用还需成长值/当前等级的最大值
        MembershipModel membershipModel = this.evaluate(loginName);
        MembershipModel NextLevelMembershipModel = this.getMembershipByLevel(membershipModel.getLevel() + 1);
        double changeable = (accountModel.getMembershipPoint() - membershipModel.getExperience())/(NextLevelMembershipModel.getExperience() - membershipModel.getExperience())*0.2*100;
        switch (membershipModel.getLevel()){
           case 0:
               progressBarPercent = 0;
               break;
           case 1:
               progressBarPercent = 20;
               break;
           case 2:
               progressBarPercent = 40;
               break;
           case 3:
               progressBarPercent = 60;
               break;
           case 4:
               progressBarPercent = 80;
               break;
           case 5:
               progressBarPercent = 100;
               break;
           default:
               progressBarPercent = 0;
               break;
       }
        return (int)(progressBarPercent + changeable);
    }

}
