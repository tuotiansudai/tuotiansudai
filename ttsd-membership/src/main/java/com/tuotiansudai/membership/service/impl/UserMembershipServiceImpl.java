package com.tuotiansudai.membership.service.impl;

import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.membership.service.UserMembershipService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserMembershipServiceImpl implements UserMembershipService {

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Override
    public MembershipModel getMembershipByLevel(int level) {
        return membershipMapper.findByLevel(level);
    }

    @Override
    public int getProgressBarPercent(String loginName) {
        int progressBarPercent = 0;
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        //分两部分 1.国定 V0 20 V1 40 V3 60 V4 80 V5 100
        //        2. 变化部分  计算公式  用还需成长值/当前等级的最大值
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        MembershipModel NextLevelMembershipModel = this.getMembershipByLevel(membershipModel.getLevel() >= 5?membershipModel.getLevel():(membershipModel.getLevel() + 1));
        double changeable = ((accountModel.getMembershipPoint() - membershipModel.getExperience()) / (double) (NextLevelMembershipModel.getExperience() - membershipModel.getExperience())) * 0.2 * 100;
        switch (membershipModel.getLevel()) {
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
        return (int) (progressBarPercent == 100?progressBarPercent:(progressBarPercent + changeable));
    }

    @Override
    public List<String> getPrivilege(String loginName) {
        List<String> privilegeList = new ArrayList<String>();
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        switch (membershipModel.getLevel()) {
            case 0:
                privilegeList.add("multi-ensuring");
                privilegeList.add("anytime-withdraw");
                privilegeList.add("service-fee-discount");
                break;
            case 1:
                privilegeList.add("multi-ensuring");
                privilegeList.add("anytime-withdraw");
                privilegeList.add("birthday-privilege");
                privilegeList.add("service-fee-discount");
                break;
            case 2:
                privilegeList.add("multi-ensuring");
                privilegeList.add("anytime-withdraw");
                privilegeList.add("birthday-privilege");
                privilegeList.add("membership-giftbag");
                privilegeList.add("service-fee-discount");
                break;
            case 3:
                privilegeList.add("multi-ensuring");
                privilegeList.add("anytime-withdraw");
                privilegeList.add("birthday-privilege");
                privilegeList.add("membership-giftbag");
                privilegeList.add("vip-service");
                privilegeList.add("service-fee-discount");
                break;
            case 4:
                privilegeList.add("multi-ensuring");
                privilegeList.add("anytime-withdraw");
                privilegeList.add("birthday-privilege");
                privilegeList.add("membership-giftbag");
                privilegeList.add("vip-service");
                privilegeList.add("spec-financial-advisor");
                privilegeList.add("service-fee-discount");
                break;
            case 5:
                privilegeList.add("multi-ensuring");
                privilegeList.add("anytime-withdraw");
                privilegeList.add("birthday-privilege");
                privilegeList.add("membership-giftbag");
                privilegeList.add("vip-service");
                privilegeList.add("spec-financial-advisor");
                privilegeList.add("birthday-benefits");
                privilegeList.add("service-fee-discount");
                break;
            default:
                privilegeList.add("multi-ensuring");
                privilegeList.add("anytime-withdraw");
                privilegeList.add("service-fee-discount");
                break;
        }
        return privilegeList;
    }

    @Override
    public String[] showDisable(String loginName) {
        String[] privilegeShow = new String[8];
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        switch (membershipModel.getLevel()) {
            case 0:
                privilegeShow[0] = "";
                privilegeShow[1] = "";
                privilegeShow[2] = "-hui";
                privilegeShow[3] = "-hui";
                privilegeShow[4] = "-hui";
                privilegeShow[5] = "-hui";
                privilegeShow[6] = "-hui";
                privilegeShow[7] = "-hui";
                break;
            case 1:
                privilegeShow[0] = "";
                privilegeShow[1] = "";
                privilegeShow[2] = "";
                privilegeShow[3] = "-hui";
                privilegeShow[4] = "-hui";
                privilegeShow[5] = "-hui";
                privilegeShow[6] = "-hui";
                privilegeShow[7] = "-hui";
                break;
            case 2:
                privilegeShow[0] = "";
                privilegeShow[1] = "";
                privilegeShow[2] = "";
                privilegeShow[3] = "";
                privilegeShow[4] = "-hui";
                privilegeShow[5] = "-hui";
                privilegeShow[6] = "-hui";
                privilegeShow[7] = "-hui";
                break;
            case 3:
                privilegeShow[0] = "";
                privilegeShow[1] = "";
                privilegeShow[2] = "";
                privilegeShow[3] = "";
                privilegeShow[4] = "";
                privilegeShow[5] = "-hui";
                privilegeShow[6] = "-hui";
                privilegeShow[7] = "-hui";
                break;
            case 4:
                privilegeShow[0] = "";
                privilegeShow[1] = "";
                privilegeShow[2] = "";
                privilegeShow[3] = "";
                privilegeShow[4] = "";
                privilegeShow[5] = "";
                privilegeShow[6] = "-hui";
                privilegeShow[7] = "-hui";
                break;
            case 5:
                privilegeShow[0] = "";
                privilegeShow[1] = "";
                privilegeShow[2] = "";
                privilegeShow[3] = "";
                privilegeShow[4] = "";
                privilegeShow[5] = "";
                privilegeShow[6] = "";
                privilegeShow[7] = "";
                break;
            default:
                privilegeShow[0] = "";
                privilegeShow[1] = "";
                privilegeShow[2] = "-hui";
                privilegeShow[3] = "-hui";
                privilegeShow[4] = "-hui";
                privilegeShow[5] = "-hui";
                privilegeShow[6] = "-hui";
                privilegeShow[7] = "-hui";
                break;
        }
        return privilegeShow;
    }

    @Override
    public int getExpireDayByLoginName(String loginName) {
        long leftDays = 0;
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        if(membershipModel != null){
            UserMembershipModel  userMembershipModel = userMembershipMapper.findByMembershipId(membershipModel.getId());
            leftDays = DateUtil.differenceDay(new Date(), userMembershipModel.getExpiredTime());
        }
        return (int)leftDays;
    }
}
