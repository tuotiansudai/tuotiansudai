package com.tuotiansudai.point.service.impl;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.point.dto.SignInPoint;
import com.tuotiansudai.point.dto.SignInPointDto;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.SignInService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
public class SignInServiceImpl implements SignInService {
    static Logger logger = Logger.getLogger(SignInServiceImpl.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private PointBillService pointBillService;

    private static final String POINT_SIGN_IN_KEY = "web:point:signin";

    @Override
    @Transactional
    public SignInPointDto signIn(String loginName) {
        DateTime today = new DateTime().withTimeAtStartOfDay();

        SignInPointDto signInPointDto = new SignInPointDto(SignInPoint.FIRST_SIGN_IN.getTimes(), today.toDate(), SignInPoint.FIRST_SIGN_IN.getPoint(),SignInPoint.SECOND_SIGN_IN.getPoint());

        SignInPointDto lastSignInPointDto = (SignInPointDto) redisWrapperClient.hgetSeri(POINT_SIGN_IN_KEY, loginName);

        if (lastSignInPointDto != null) {
            if (Days.daysBetween(new DateTime(lastSignInPointDto.getSignInDate()), today) == Days.ZERO) {
                lastSignInPointDto.setStatus(false);
                lastSignInPointDto.setMessage("今天已经签到过了，请明天再来！");
                return lastSignInPointDto;
            }
            if (Days.daysBetween(new DateTime(lastSignInPointDto.getSignInDate()), today) == Days.ONE) {
                int signInCount = lastSignInPointDto.getSignInCount() + 1;
                signInPointDto = new SignInPointDto(signInCount, today.toDate(), SignInPoint.getPointByTimes(signInCount),SignInPoint.getPointByTimes(signInCount + 1));
            }
        }
        signInPointDto.setStatus(true);
        redisWrapperClient.hsetSeri(POINT_SIGN_IN_KEY, loginName, signInPointDto);
        pointBillService.createPointBill(loginName, null, PointBusinessType.SIGN_IN, signInPointDto.getSignInPoint());
        logger.debug(MessageFormat.format("{0} sign in success {1} 次", loginName, signInPointDto.getSignInCount()));
        return signInPointDto;
    }

    @Override
    public boolean signInIsSuccess(String loginName) {
        SignInPointDto signInPointDto = (SignInPointDto) redisWrapperClient.hgetSeri(POINT_SIGN_IN_KEY, loginName);
        return signInPointDto != null && Days.daysBetween(new DateTime(signInPointDto.getSignInDate()), new DateTime().withTimeAtStartOfDay()) == Days.ZERO;
    }

    @Override
    public SignInPointDto getLastSignIn(String loginName) {
        return (SignInPointDto) redisWrapperClient.hgetSeri(POINT_SIGN_IN_KEY, loginName);
    }
}
