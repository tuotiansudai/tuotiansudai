package com.tuotiansudai.point.service.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.point.repository.mapper.UserPointMapper;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class PointServiceImpl implements PointService {
    static Logger logger = Logger.getLogger(PointServiceImpl.class);

    @Autowired
    private UserPointMapper userPointMapper;

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Value("#{'${activity.concrete.period}'.split('\\~')}")
    private List<String> activityConcretePeriod = Lists.newArrayList();

    @Override
    @Transactional
    public void obtainPointInvest(InvestModel investModel) {
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        int duration = loanModel.getDuration();
        long point = new BigDecimal((investModel.getAmount() * duration / InterestCalculator.DAYS_OF_YEAR)).divide(new BigDecimal(100), 0, BigDecimal.ROUND_DOWN).longValue();
        point = getMaterialActivityPoint(loanModel.getProductType(), loanModel.getActivityType(), point, investModel.getId());
        pointBillService.createPointBill(investModel.getLoginName(), investModel.getId(), PointBusinessType.INVEST, point);
        logger.info(MessageFormat.format("{0} has obtained point {1}", investModel.getId(), point));
    }

    @Override
    public long getAvailablePoint(String loginName) {
        return userPointMapper.getPointByLoginName(loginName, 0L);
    }

    @Override
    public long getUserPointByLoginName(String loginName) {
        return userPointMapper.getPointByLoginName(loginName, 0L);
    }

    private long getMaterialActivityPoint(ProductType productType, ActivityType activityType, long point, long investId) {
        if (productType == null || productType.equals(ProductType.EXPERIENCE) || activityType.equals(ActivityType.NEWBIE)) {
            return point;
        }
        Date nowDate = DateTime.now().toDate();
        Date activityBeginTime = DateTime.parse(activityConcretePeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date activityEndTime = DateTime.parse(activityConcretePeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        if (nowDate.before(activityEndTime) && nowDate.after(activityBeginTime)) {
            logger.info(MessageFormat.format("{0} has double obtained point {1}", investId, point));
            return point * 2;
        }
        return point;
    }
}
