package com.tuotiansudai.point.service.impl;


import com.tuotiansudai.point.repository.mapper.UserPointMapper;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class PointServiceImpl implements PointService {
    private final static Logger logger = Logger.getLogger(PointServiceImpl.class);

    @Autowired
    private UserPointMapper userPointMapper;

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Override
    @Transactional
    public void obtainPointInvest(long investId) {
        InvestModel investModel = investMapper.findById(investId);
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        int duration = loanModel.getDuration();
        long point = new BigDecimal((investModel.getAmount() * duration / InterestCalculator.DAYS_OF_YEAR)).divide(new BigDecimal(100), 0, BigDecimal.ROUND_DOWN).longValue();
        pointBillService.createPointBill(investModel.getLoginName(), investModel.getId(), PointBusinessType.INVEST, point);
        logger.info(MessageFormat.format("{0} has obtained point {1}", investModel.getId(), point));
    }

    @Override
    public long getAvailablePoint(String loginName) {
        return userPointMapper.getPointByLoginName(loginName, 0L);
    }

    @Override
    public Map<String, String> findAllChannel() {
        Map<String, String> channelMap = new LinkedHashMap<>();
        channelMap.put(PointBillService.CHANNEL_SUDAI, "拓天速贷");
        userPointMapper.findAllChannel().forEach(c -> channelMap.put(c, c));
        return channelMap;
    }
}
