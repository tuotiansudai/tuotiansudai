package com.tuotiansudai.aspect;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.dto.SmsUserReceiveMembershipDto;
import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipGiveMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class ReceiveMembershipAspect {

    @Autowired
    MembershipMapper membershipMapper;

    @Autowired
    MembershipGiveMapper membershipGiveMapper;

    @Autowired
    UserMembershipMapper userMembershipMapper;

    @Autowired
    MembershipExperienceBillMapper membershipExperienceBillMapper;

    @Autowired
    SmsWrapperClient smsWrapperClient;

    @AfterReturning(value = "execution(* com.tuotiansudai.service.UserService.registerUser(..))", returning = "returnValue")
    public void registerUserPointcut(JoinPoint joinPoint, boolean returnValue) throws Throwable {
        if(!returnValue) {
            return;
        }

        RegisterUserDto dto = (RegisterUserDto) joinPoint.getArgs()[0];
        String mobile = dto.getMobile();
        String loginName = dto.getLoginName();

        List<MembershipModel> membershipModels = membershipMapper.findAllMembership();
        Map<Long, Integer> map = new HashMap<>();
        for (MembershipModel membershipModel : membershipModels) {
            map.put(membershipModel.getId(), membershipModel.getLevel());
        }

        List<MembershipGiveModel> membershipGiveModels = membershipGiveMapper.findAllCurrentNewUserGivePlans();
        for (MembershipGiveModel membershipGiveModel : membershipGiveModels) {
            UserMembershipModel userMembershipModel = new UserMembershipModel(loginName, membershipGiveModel.getMembershipId(),
                    DateTime.now().withTimeAtStartOfDay().plusDays(1).plusDays(membershipGiveModel.getValidPeriod()).plusSeconds(-1).toDate(),
                    UserMembershipType.GIVEN, membershipGiveModel.getId());
            userMembershipMapper.create(userMembershipModel);

            long totalExperience = 0;
            MembershipExperienceBillModel membershipExperienceBillModel = new MembershipExperienceBillModel(loginName,
                    0L, totalExperience, new Date(),
                    MessageFormat.format("获赠期限为{0}天的V{1}会员", membershipGiveModel.getValidPeriod(), map.get(membershipGiveModel.getMembershipId())));
            membershipExperienceBillMapper.create(membershipExperienceBillModel);

            if (membershipGiveModel.isSmsNotify()) {
                smsWrapperClient.sendNewUserReceiveMembership(new SmsUserReceiveMembershipDto(mobile, map.get(membershipGiveModel.getMembershipId())));
            }
        }
    }
}
