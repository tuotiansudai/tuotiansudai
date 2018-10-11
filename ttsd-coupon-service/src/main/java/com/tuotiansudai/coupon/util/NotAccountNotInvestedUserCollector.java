package com.tuotiansudai.coupon.util;


import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotAccountNotInvestedUserCollector implements UserCollector {

    @Autowired
    private InvestMapper investMapper;

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        return userModel != null && investMapper.sumSuccessInvestAmountByLoginName(null, userModel.getLoginName(), true) == 0;
    }

}
