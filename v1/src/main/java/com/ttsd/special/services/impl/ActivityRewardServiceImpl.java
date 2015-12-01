package com.ttsd.special.services.impl;

import com.esoft.archer.user.UserBillConstants;
import com.esoft.archer.user.service.impl.UserBillBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.HttpClientUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.risk.service.SystemBillService;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.ttsd.special.services.ActivityRewardService;
import com.umpay.api.common.ReqData;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;
import org.apache.commons.logging.Log;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;


@Service
public class ActivityRewardServiceImpl implements ActivityRewardService {

    private static String SUCCESS_CODE = "0000";
    @Logger
    Log log;

    @Autowired
    private UserBillBO userBillBO;

    @Autowired
    private SystemBillService systemBillService;

    @Resource
    private HibernateTemplate ht;

    @Override
    @Transactional
    public boolean payActivityReward(String orderId, String userId, double reward, String detail) {
        String accountId = this.getAccount(userId);
        boolean result = this.transferMerToUser(orderId, accountId, reward);
        if (result) {
            try {
                this.recordReward(userId, orderId, reward, detail);
                return true;
            } catch (Exception e) {
                log.debug(e.getLocalizedMessage(), e);
            }
        } else {
            log.debug(MessageFormat.format("project_transfer if failed orderId :{0},userId:{1},", orderId, userId));
        }
        return false;
    }

    @Override
    public boolean transferMerToUser(String orderId, String accountId, double reward) {
        ReqData reqData = generateReqData(orderId, accountId, reward);
        if (reqData == null) {
            return false;
        }

        TrusteeshipOperation operation = createTrusteeshipOperation(orderId, reqData);
        if (operation == null) {
            return false;
        }

        try {
            String responseBodyAsString = HttpClientUtil.getResponseBodyAsString(reqData.getUrl());
            Map<String, String> resData = Plat2Mer_v40.getResData(responseBodyAsString);
            log.info(MessageFormat.format("Pay orderId = {0}, returnCode = {1}", orderId, resData.get("ret_code")));
            return updateTrusteeshipOperationStatus(operation, resData);
        } catch (Exception e) {
            String template = "Activity reward transfer failed: orderId = {0}, accountId = {1}, reward = {2}";
            log.error(MessageFormat.format(template, orderId, accountId, reward));
            log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    private ReqData generateReqData(String orderId, String accountId, double cent) {
        try {
            DecimalFormat currentNumberFormat = new DecimalFormat("#");
            Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.TRANSFER);
            sendMap.put("order_id", orderId);
            sendMap.put("partic_acc_type", UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_PERSON); // 转账方账户类型(实际收款方)
            sendMap.put("trans_action", UmPayConstants.TransferProjectStatus.TRANS_ACTION_OUT); // 转账方向
            sendMap.put("partic_user_id", accountId); //转账方用户号(实际收款方)
            sendMap.put("amount", currentNumberFormat.format(cent * 100)); // 单位为分
            sendMap.put("mer_date", DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
            ReqData reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
            return reqData;
        } catch (Exception e) {
            String template = "Generate req data failed: accountId = {0}, cent = {cent}";
            log.error(MessageFormat.format(template, accountId, cent));
            log.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    private TrusteeshipOperation createTrusteeshipOperation(String markId, ReqData reqData) {
        try {
            TrusteeshipOperation operation = new TrusteeshipOperation();
            operation.setId(IdGenerator.randomUUID());
            operation.setMarkId(markId);
            operation.setOperator(UserBillConstants.OperatorInfo.ACTIVITY_REWARD);
            operation.setRequestUrl(reqData.getUrl());
            operation.setCharset("utf-8");
            operation.setRequestData(reqData.getPlain());
            operation.setType(UmPayConstants.OperationType.TRANSFER);
            operation.setTrusteeship("umpay");
            operation.setRequestTime(new Date());
            operation.setStatus(TrusteeshipConstants.Status.SENDED);
            ht.save(operation);
            return operation;
        } catch (Exception e) {
            log.error("Create operation failed: " + reqData.getPlain());
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateTrusteeshipOperationStatus(TrusteeshipOperation operation, Map<String, String> resData) {
        try {
            operation.setStatus(SUCCESS_CODE.equals(resData.get("ret_code")) ? TrusteeshipConstants.Status.PASSED : TrusteeshipConstants.Status.REFUSED);
            operation.setResponseData(resData.toString());
            operation.setResponseTime(new Date());
            ht.saveOrUpdate(operation);
        } catch (Exception e) {
            String template = "Update operation failed: operationId = {0}, resDate = {1}";
            log.error(MessageFormat.format(template, operation.getId(), resData.toString()));
            log.error(e.getLocalizedMessage(), e);
        }
        return SUCCESS_CODE.equals(resData.get("ret_code"));
    }

    public String getAccount(String userId) {
        try {
            DetachedCriteria accountCriteria = DetachedCriteria.forClass(TrusteeshipAccount.class);
            accountCriteria.add(Restrictions.eq("user.id", userId));
            TrusteeshipAccount account = (TrusteeshipAccount) DataAccessUtils.uniqueResult(ht.findByCriteria(accountCriteria, 0, 1));
            return account != null ? account.getId() : null;
        } catch (Exception e) {
            String template = "Query umpay account failed: userId = {0}";
            log.error(MessageFormat.format(template, userId));
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    private void recordReward(String userId, String orderId, double reward, String detail) throws Exception {
        try {
            userBillBO.transferIntoBalance(userId, reward, UserBillConstants.OperatorInfo.ACTIVITY_REWARD, detail);
        } catch (Exception e) {
            String template = "Create user bill  reward failed: orderId:{0} detail = {1}";
            log.error(MessageFormat.format(template, orderId, detail),e);
            throw e;
        }
        try {
            systemBillService.transferOut(reward, UserBillConstants.OperatorInfo.ACTIVITY_REWARD, detail);
        } catch (Exception e) {
            String template = "Create system bill  reward failed : orderId:{0} detail = {0} ";
            log.error(MessageFormat.format(template, orderId, detail),e);
            throw e;

        }
    }


}
