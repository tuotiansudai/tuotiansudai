package com.tuotiansudai.point.service.impl;

import com.tuotiansudai.point.dto.SignInPoint;
import com.tuotiansudai.point.dto.SignInPointDto;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.SignInService;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
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
    private PointBillService pointBillService;

    @Autowired
    private PointBillMapper pointBillMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    @Transactional
    public SignInPointDto signIn(String loginName) {
        DateTime today = new DateTime().withTimeAtStartOfDay();

        SignInPointDto signInPointDto = new SignInPointDto(SignInPoint.FIRST_SIGN_IN.getTimes(), today.toDate(), SignInPoint.FIRST_SIGN_IN.getPoint(),SignInPoint.SECOND_SIGN_IN.getPoint());

        SignInPointDto lastSignInPointDto = obtainSignInPointDto(loginName);
        if (lastSignInPointDto != null) {
            if (Days.daysBetween(new DateTime(lastSignInPointDto.getSignInDate()).withTimeAtStartOfDay(), today) == Days.ZERO) {
                lastSignInPointDto.setStatus(false);
                lastSignInPointDto.setMessage("今天已经签到过了，请明天再来！");
                return lastSignInPointDto;
            }
            if (Days.daysBetween(new DateTime(lastSignInPointDto.getSignInDate()).withTimeAtStartOfDay(), today) == Days.ONE) {
                int signInCount = lastSignInPointDto.getSignInCount() + 1;
                signInPointDto = new SignInPointDto(signInCount, today.toDate(), SignInPoint.getPointByTimes(signInCount),SignInPoint.getPointByTimes(signInCount + 1));
            }
        }
        signInPointDto.setStatus(true);
        UserModel userModel = userMapper.findByLoginName(loginName);
        userModel.setSignInCount(signInPointDto.getSignInCount());
        userMapper.updateUser(userModel);
        pointBillService.createPointBill(loginName, null, PointBusinessType.SIGN_IN, signInPointDto.getSignInPoint());
        logger.debug(MessageFormat.format("{0} sign in success {1} 次", loginName, signInPointDto.getSignInCount()));
        return signInPointDto;
    }

    @Override
    public boolean signInIsSuccess(String loginName) {
        SignInPointDto signInPointDto = obtainSignInPointDto(loginName) ;

        return signInPointDto != null && Days.daysBetween(new DateTime(signInPointDto.getSignInDate()).withTimeAtStartOfDay(), new DateTime().withTimeAtStartOfDay()) == Days.ZERO;
    }

    @Override
    public SignInPointDto getLastSignIn(String loginName) {
        return obtainSignInPointDto(loginName);
    }

    private SignInPointDto obtainSignInPointDto(String loginName){
        SignInPointDto signInPointDto = null ;
        PointBillModel pointBillModel = pointBillMapper.findSignInPointBillByLoginName(loginName);
        UserModel userModel = userMapper.findByLoginName(loginName);
        if(pointBillModel != null){
            signInPointDto = new SignInPointDto(pointBillModel,userModel);
        }
        return signInPointDto;
    }
}
