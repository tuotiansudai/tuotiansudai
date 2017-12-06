package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRegisterInfo;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GrantWelfareActivityService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvestMapper investMapper;

    @Value("#{'${activity.grant.welfare.period}'.split('\\~')}")
    private List<String> grantWelfarePeriod = Lists.newArrayList();

    public int findReferrerCountByLoginName(String loginName) {
        int referrerCount = 0;
        if (Strings.isNullOrEmpty(loginName)) {
            return 0;
        }

        Date startTime = DateTime.parse(grantWelfarePeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(grantWelfarePeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<UserRegisterInfo> userModels = userMapper.findAllUsersByRegisterTimeAndReferrer(startTime, endTime, loginName);
        referrerCount = Integer.parseInt(String.valueOf(userModels.stream()
                .filter((n) -> {
                    InvestModel investModel = investMapper.findFirstInvestAmountByLoginName(n.getLoginName(), startTime, endTime);
                    return investModel != null && investModel.getAmount() >= 500000;
                }).count()));

        return referrerCount;
    }

    public String findReferrerSumInvestAmountByLoginName(String loginName) {
        long referrerSumInvestAmount = 0L;
        if (Strings.isNullOrEmpty(loginName)) {
            return "0";
        }

        Date startTime = DateTime.parse(grantWelfarePeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(grantWelfarePeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<UserRegisterInfo> userModels = userMapper.findAllUsersByRegisterTimeAndReferrer(startTime, endTime, loginName);
        for (UserRegisterInfo referrerUserModel : userModels) {
            referrerSumInvestAmount += investMapper.sumSuccessActivityInvestAmount(referrerUserModel.getLoginName(), null, startTime, endTime);
        }
        return AmountConverter.convertCentToString(referrerSumInvestAmount);
    }

}
