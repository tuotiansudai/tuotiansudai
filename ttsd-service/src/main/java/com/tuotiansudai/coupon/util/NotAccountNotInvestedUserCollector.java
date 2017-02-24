package com.tuotiansudai.coupon.util;


import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotAccountNotInvestedUserCollector implements UserCollector {

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<String> collect(long couponId) {
        List<String> investorLoginNames = investMapper.findInvestorLoginNames();
        List<String> userLoginNames = userMapper.findAllLoginNames();
        userLoginNames.removeAll(investorLoginNames);
        return userLoginNames;
    }

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        return userModel != null && investMapper.sumSuccessInvestAmountByLoginName(null, userModel.getLoginName(),true) == 0;
    }

}
