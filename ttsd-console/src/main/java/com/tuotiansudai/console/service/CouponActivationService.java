package com.tuotiansudai.console.service;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.log.service.AuditLogService;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class CouponActivationService {

    static Logger logger = Logger.getLogger(CouponActivationService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private ExchangeCodeService exchangeCodeService;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Transactional
    public void inactive(String loginName, long couponId, String ip) {
        CouponModel couponModel = couponMapper.findById(couponId);
        if (!couponModel.isActive()) {
            return;
        }
        couponModel.setActive(false);
        couponModel.setActivatedBy(loginName);
        couponModel.setActivatedTime(new Date());
        couponMapper.updateCoupon(couponModel);

        UserModel auditor = userMapper.findByLoginName(loginName);
        String auditorRealName = auditor == null ? loginName : auditor.getUserName();

        UserModel operator = userMapper.findByLoginName(couponModel.getCreatedBy());
        String operatorRealName = operator == null ? couponModel.getCreatedBy() : operator.getUserName();

        String description = MessageFormat.format("{0} 撤销了 {1} 创建的 {2}", auditorRealName, operatorRealName, couponModel.getCouponType().getName());
        auditLogService.createAuditLog(loginName, couponModel.getCreatedBy(), OperationType.COUPON, String.valueOf(couponId), description, ip);
    }

    @Transactional
    public void exchangeInactive(String loginName, long couponId, String ip) {
        CouponModel couponModel = couponMapper.findById(couponId);
        couponModel.setActive(false);
        couponModel.setActivatedBy(loginName);
        couponModel.setActivatedTime(new Date());
        couponMapper.updateCoupon(couponModel);
    }

    @Transactional
    public void active(String loginName, long couponId, String ip) {
        CouponModel couponModel = couponMapper.findById(couponId);
        if (couponModel.isActive()) {
            return;
        }

        couponModel.setActive(true);
        couponModel.setActivatedBy(loginName);
        couponModel.setActivatedTime(new Date());
        couponMapper.updateCoupon(couponModel);

        UserModel auditor = userMapper.findByLoginName(loginName);
        String auditorRealName = auditor == null ? loginName : auditor.getUserName();

        UserModel operator = userMapper.findByLoginName(couponModel.getCreatedBy());
        String operatorRealName = operator == null ? couponModel.getCreatedBy() : operator.getUserName();

        if (couponModel.getUserGroup() == UserGroup.EXCHANGER_CODE) {
            exchangeCodeService.generateExchangeCode(couponModel.getId(), couponModel.getTotalCount().intValue());
        }

        if (couponModel.isSmsAlert()) {
            logger.info(MessageFormat.format("coupon active sms alert, couponId:", String.valueOf(couponId)));
            mqWrapperClient.sendMessage(MessageQueue.CouponSmsNotify, String.valueOf(couponId));
        }

        String description = MessageFormat.format("{0} 激活了 {1} 创建的 {2}", auditorRealName, operatorRealName, couponModel.getCouponType().getName());
        auditLogService.createAuditLog(loginName, couponModel.getCreatedBy(), OperationType.COUPON, String.valueOf(couponId), description, ip);
    }
}
