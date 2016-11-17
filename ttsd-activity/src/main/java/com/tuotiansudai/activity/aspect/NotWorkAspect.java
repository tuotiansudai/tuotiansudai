package com.tuotiansudai.activity.aspect;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.service.NotWorkService;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

public class NotWorkAspect {
    @Autowired
    UserMapper userMapper;

    @Autowired
    NotWorkService notWorkService;

    public void afterInvest(JoinPoint joinPoint) {
        String loginName = "";
        long amount = 0;

        notWorkService.userInvest(loginName, amount);
        UserModel userModel = userMapper.findByLoginName(loginName);
        if (!Strings.isNullOrEmpty(userModel.getReferrer())) {
            notWorkService.recommendedInvest(loginName, amount);
        }
    }

    public void afterRegister(JoinPoint joinPoint) {
        String loginName = "";
        notWorkService.recommendedRegister(loginName);
    }

    public void afterIdentify(JoinPoint joinPoint) {
        String loginName = "";

        notWorkService.recommendedIdentify(loginName);
    }
}
