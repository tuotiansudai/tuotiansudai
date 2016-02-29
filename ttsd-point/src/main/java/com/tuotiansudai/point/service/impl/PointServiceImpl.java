package com.tuotiansudai.point.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponExchangeMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponExchangeModel;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.point.dto.SignInPointDto;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.point.util.SignInPoint;
import com.tuotiansudai.util.IdGenerator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class PointServiceImpl implements PointService {
    static Logger logger = Logger.getLogger(PointServiceImpl.class);

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponExchangeMapper couponExchangeMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private PointBillService pointBillService;
    @Autowired
    private IdGenerator idGenerator;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String POINT_SIGNIN_IN_KEY = "web:point:signIn";

    @Override
    @Transactional
    public void createCouponAndExchange(String loginName, ExchangeCouponDto exchangeCouponDto) {
        CouponModel couponModel = new CouponModel(exchangeCouponDto);
        couponModel.setCreatedBy(loginName);
        couponModel.setCreatedTime(new Date());
        couponMapper.create(couponModel);

        CouponExchangeModel couponExchangeModel = new CouponExchangeModel();
        couponExchangeModel.setCouponId(couponModel.getId());
        couponExchangeModel.setExchangePoint(exchangeCouponDto.getExchangePoint());
        couponExchangeMapper.create(couponExchangeModel);

    }

    @Override
    @Transactional
    public SignInPointDto signIn(String loginName) {

        try {
            SignInPointDto signInPointDto;
            if (!redisWrapperClient.hexists(POINT_SIGNIN_IN_KEY, loginName)) {
                Date today = new DateTime().withTimeAtStartOfDay().toDate();
                signInPointDto = new SignInPointDto(SignInPoint.firstSignIn, today,
                        SignInPoint.signInPointMap.get(SignInPoint.firstSignIn));
                redisWrapperClient.hset(POINT_SIGNIN_IN_KEY, loginName, objectMapper.writeValueAsString(signInPointDto));
            } else {
                String redisValue = redisWrapperClient.hget(POINT_SIGNIN_IN_KEY, loginName);
                SignInPointDto lastSignInPointDto = objectMapper.readValue(redisValue, new TypeReference<SignInPointDto>() {
                });
                signInPointDto = SignInPoint.calculateSignInPoint(lastSignInPointDto);
                redisWrapperClient.hset(POINT_SIGNIN_IN_KEY, loginName, objectMapper.writeValueAsString(signInPointDto));
            }
            pointBillService.createPointBill(loginName, null, PointBusinessType.SIGN_IN, signInPointDto.getPoint());
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
            if(!redisWrapperClient.hexists(POINT_SIGNIN_IN_KEY, loginName)){
                return false;
            }else{
                String redisValue = redisWrapperClient.hget(POINT_SIGNIN_IN_KEY,loginName);
                SignInPointDto signInPointDto = objectMapper.readValue(redisValue, new TypeReference<SignInPointDto>() {
                });

                return new DateTime(signInPointDto.getSignInDate()).compareTo(new DateTime().withTimeAtStartOfDay()) == 0;
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return true;

    }


}
