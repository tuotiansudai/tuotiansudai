package com.tuotiansudai.point.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
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

import java.io.IOException;
import java.text.MessageFormat;


@Service
public class SignInServiceImpl implements SignInService {
    static Logger logger = Logger.getLogger(SignInServiceImpl.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private PointBillService pointBillService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String POINT_SIGN_IN_KEY = "web:point:signin";

    @Override
    @Transactional
    public SignInPointDto signIn(String loginName) {
        DateTime today = new DateTime().withTimeAtStartOfDay();

        try {
            SignInPointDto signInPointDto = new SignInPointDto(SignInPoint.FIRST_SIGN_IN.getTimes(), today.toDate(), SignInPoint.FIRST_SIGN_IN.getPoint(),SignInPoint.SECOND_SIGN_IN.getPoint());;
            String redisValue = redisWrapperClient.hget(POINT_SIGN_IN_KEY, loginName);
            SignInPointDto lastSignInPointDto = Strings.isNullOrEmpty(redisValue) ? null : (SignInPointDto) objectMapper.readValue(redisValue, new TypeReference<SignInPointDto>() {
            });

            if (lastSignInPointDto != null) {
                if (Days.daysBetween(new DateTime(lastSignInPointDto.getSignInDate()), new DateTime().withTimeAtStartOfDay()) == Days.ZERO) {
                    return lastSignInPointDto;
                }
                if (Days.daysBetween(new DateTime(lastSignInPointDto.getSignInDate()), today) == Days.ONE) {
                    int signInCount = lastSignInPointDto.getSignInCount() + 1;
                    signInPointDto = new SignInPointDto(signInCount, today.toDate(), SignInPoint.getPointByTimes(signInCount),SignInPoint.getPointByTimes(signInCount + 1));
                }
            }
            redisWrapperClient.hset(POINT_SIGN_IN_KEY, loginName, objectMapper.writeValueAsString(signInPointDto));
            pointBillService.createPointBill(loginName, null, PointBusinessType.SIGN_IN, signInPointDto.getSignInPoint());
            logger.debug(MessageFormat.format("{0} sign in success {1} 次", loginName, signInPointDto.getSignInCount()));
            return signInPointDto;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;

    }

    @Override
    public boolean signInIsSuccess(String loginName) {
        try {
            if (!redisWrapperClient.hexists(POINT_SIGN_IN_KEY, loginName)) {
                return false;
            } else {
                String redisValue = redisWrapperClient.hget(POINT_SIGN_IN_KEY, loginName);
                SignInPointDto signInPointDto = objectMapper.readValue(redisValue, new TypeReference<SignInPointDto>() {
                });

                return Days.daysBetween(new DateTime(signInPointDto.getSignInDate()), new DateTime().withTimeAtStartOfDay()) == Days.ZERO;
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return true;

    }
}
