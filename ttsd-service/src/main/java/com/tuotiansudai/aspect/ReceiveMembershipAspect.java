package com.tuotiansudai.aspect;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.dto.SmsUserReceiveMembershipDto;
import com.tuotiansudai.membership.repository.mapper.MembershipGiveMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipGiveModel;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    SmsWrapperClient smsWrapperClient;

    @AfterReturning(value = "execution(* *..com.tuotiansudai.service.UserService.registerUser(*))", returning = "returnValue")
    public void registerUserPointcut(JoinPoint joinPoint, boolean returnValue) throws Throwable {
        if(!returnValue) {
            return;
        }

        RegisterUserDto dto = (RegisterUserDto) joinPoint.getArgs()[0];
        String mobile = dto.getMobile();

        List<MembershipModel> membershipModels = membershipMapper.findAllMembership();
        Map<Long, Integer> map = new HashMap<>();
        for (MembershipModel membershipModel : membershipModels) {
            map.put(membershipModel.getId(), membershipModel.getLevel());
        }

        List<MembershipGiveModel> membershipGiveModels = membershipGiveMapper.findAllCurrentNewUserGivePlans();
        for (MembershipGiveModel membershipGiveModel : membershipGiveModels) {
            if (membershipGiveModel.isSmsNotify()) {
                smsWrapperClient.sendNewUserReceiveMembership(new SmsUserReceiveMembershipDto(mobile, map.get(membershipGiveModel.getMembershipId())));
            }
        }
    }
}
