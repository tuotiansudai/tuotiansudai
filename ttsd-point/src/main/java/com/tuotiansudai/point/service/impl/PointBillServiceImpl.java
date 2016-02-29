package com.tuotiansudai.point.service.impl;

import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.mapper.PointTaskMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.PointTaskModel;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;

@Service
public class PointBillServiceImpl implements PointBillService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PointBillMapper pointBillMapper;

    @Autowired
    private PointTaskMapper pointTaskMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Override
    @Transactional
    public void createPointBill(String loginName, Long orderId, PointBusinessType businessType, long point) {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        accountModel.setPoint(accountModel.getPoint() + point);
        String note = this.generatePointBillNote(businessType, orderId);
        pointBillMapper.create(new PointBillModel(loginName, orderId, point, businessType, note));
        accountMapper.update(accountModel);
    }

    private String generatePointBillNote(PointBusinessType businessType, Long orderId) {
        switch (businessType) {
            case SIGN_IN:
                return MessageFormat.format("{0} 签到", new DateTime().toString("yyyy-MM-dd"));
            case TASK:
                PointTaskModel pointTaskModel = pointTaskMapper.findById(orderId);
                return pointTaskModel.getName().getDescription();
            case EXCHANGE:
                CouponModel couponModel = couponMapper.findById(userCouponMapper.findById(orderId).getCouponId());
                switch (couponModel.getCouponType()) {
                    case INTEREST_COUPON:
                        double rate = new BigDecimal(couponModel.getRate()).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_UP).doubleValue();
                        return MessageFormat.format("{0}% {1}", String.valueOf(rate), couponModel.getCouponType().getName());
                    case INVEST_COUPON:
                        return MessageFormat.format("{0}元 {1}", AmountConverter.convertCentToString(couponModel.getAmount()), couponModel.getCouponType().getName());
                    default:
                        return null;
                }
            case INVEST:
                LoanModel loanModel = loanMapper.findById(investMapper.findById(orderId).getLoanId());
                return MessageFormat.format("投资项目：{0}", loanModel.getName());
        }

        return null;
    }
}
