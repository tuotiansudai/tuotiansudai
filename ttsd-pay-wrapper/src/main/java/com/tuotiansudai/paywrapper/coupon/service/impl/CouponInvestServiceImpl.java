package com.tuotiansudai.paywrapper.coupon.service.impl;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.paywrapper.coupon.service.CouponInvestService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CouponInvestServiceImpl implements CouponInvestService {

    static Logger logger = Logger.getLogger(CouponInvestServiceImpl.class);

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    @Transactional
    public void invest(long investId, List<Long> userCouponIds) {
        if (CollectionUtils.isEmpty(userCouponIds)) {
            return;
        }

        for (Long userCouponId : userCouponIds) {
            UserCouponModel userCouponModel = userCouponMapper.findById(userCouponId);
            userCouponModel.setStatus(InvestStatus.WAIT_PAY);
            userCouponModel.setInvestId(investId);
            userCouponModel.setLoanId(investMapper.findById(investId).getLoanId());
            userCouponModel.setUsedTime(new Date());
            userCouponMapper.update(userCouponModel);
        }
    }

    @Override
    public void cancelUserCoupon(long loanId) {
        try {
            userCouponMapper.findByLoanId(loanId, null).stream()
                    .filter(userCouponModel -> InvestStatus.SUCCESS.equals(userCouponModel.getStatus()))
                    .forEach(userCouponModel -> mqWrapperClient.sendMessage(MessageQueue.UserCouponReset, String.valueOf(userCouponModel.getId())));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
