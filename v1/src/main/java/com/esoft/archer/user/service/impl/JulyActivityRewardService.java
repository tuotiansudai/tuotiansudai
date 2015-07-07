package com.esoft.archer.user.service.impl;

import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.model.JulyActivityReward;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.HttpClientUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.model.Recharge;
import com.esoft.jdp2p.risk.service.SystemBillService;
import com.esoft.jdp2p.trusteeship.TrusteeshipConstants;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.umpay.sign.util.UmPaySignUtil;
import com.esoft.umpay.trusteeship.UmPayConstants;
import com.google.common.base.Strings;
import com.umpay.api.common.ReqData;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JulyActivityRewardService {

    @Logger
    Log log;

    @Autowired
    private UserBillBO userBillBO;

    @Autowired
    private SystemBillService systemBillService;

    @Resource
    private HibernateTemplate ht;

    private static String SUCCESS_CODE = "0000";

    private static int userCertifiedReward = 5;
    private static int userRechargeReward = 5;
    private static int referrerCertifiedReward = 10;
    private static int referrerRechargeReward = 10;
    private static int referrerInvestReward = 30;


    @Transactional
    public void createActivityRewards() {
        DateTime firstJuly = new DateTime().withDate(2015, 6, 1).withTimeAtStartOfDay();

        DetachedCriteria accountCriteria = DetachedCriteria.forClass(TrusteeshipAccount.class);
        accountCriteria.createAlias("user", "user")
                .add(Restrictions.ge("user.registerTime", firstJuly.toDate()))
                .add(Restrictions.ge("createTime", firstJuly.toDate()));
        List<TrusteeshipAccount> accounts = ht.findByCriteria(accountCriteria);

        for (TrusteeshipAccount account : accounts) {
            User user = account.getUser();
            String userId = user.getId();
            try {
                DetachedCriteria rewardCriteria = DetachedCriteria.forClass(JulyActivityReward.class);
                rewardCriteria.add(Restrictions.eq("user.id", userId));
                JulyActivityReward reward = (JulyActivityReward) DataAccessUtils.uniqueResult(ht.findByCriteria(rewardCriteria));

                if (reward == null) {
                    reward = new JulyActivityReward();
                    reward.setUser(user);
                    if (!Strings.isNullOrEmpty(user.getReferrer())) {
                        DetachedCriteria referrerCriteria = DetachedCriteria.forClass(User.class);
                        referrerCriteria.add(Restrictions.eq("id", user.getReferrer()));
                        User referrer = (User) DataAccessUtils.uniqueResult(ht.findByCriteria(referrerCriteria));
                        reward.setReferrer(referrer);
                    }
                    ht.save(reward);
                    String template = "Create activity reward success: rewardId = {0}, userId = {1}";
                    log.info(MessageFormat.format(template, Long.toString(reward.getId()), userId));
                }
            } catch (Exception e) {
                String template = "Create activity reward Failed: userId = {0}";
                log.error(MessageFormat.format(template, userId));
                log.error(e);
            }
        }
    }

    public void reward() {
        List<JulyActivityReward> rewards = ht.find("from JulyActivityReward");

        for (JulyActivityReward reward : rewards) {
            String userId = reward.getUser().getId();
            boolean successRechargeExist = this.isSuccessRechargeExist(userId);
            boolean successInvestExist = this.isSuccessInvestExist(userId);

            this.rewardUser(reward, successRechargeExist);
            if (reward.getReferrer() != null) {
                this.rewardReferrer(reward, successRechargeExist, successInvestExist);
            }
        }
    }

    private void rewardUser(JulyActivityReward reward, boolean successRechargeExist) {
        String billType = "activity_reward";
        String certOrderIdTemplate = "1JulyActivity{0}";
        String rechargeOrderIdTemplate = "2JulyActivity{0}";

        String rewardId = Long.toString(reward.getId());
        String userId = reward.getUser().getId();
        String accountId = this.getAccount(userId);

        if (Strings.isNullOrEmpty(accountId) || !this.isSuccessBindBankCard(userId)) {
            String template = "User account is not Exist or no bank card: userId = {0}, rewardId = {1}";
            log.error(MessageFormat.format(template, userId, rewardId));
            return;
        }

        if (!reward.getCertifiedReward()) {
            String orderId = MessageFormat.format(certOrderIdTemplate, rewardId);
            boolean result = transferMerToUser(orderId, accountId, Integer.toString(userCertifiedReward));
            if (result) {
                reward.setCertifiedReward(true);
                reward.setCertifiedRewardTime(new Date());
                updateActivityReward(reward);
                try {
                    userBillBO.transferIntoBalance(userId, userCertifiedReward / 100D, billType, "新用户实名认证奖励");
                } catch (Exception e) {
                    String template = "Create user bill certified reward failed: rewardId = {0}";
                    log.error(MessageFormat.format(template, rewardId));
                    log.error(e);
                }
                try {
                    String systemBillDetailTemplate = "新用户实名认证奖励: orderId = {0}, userId = {1}";
                    systemBillService.transferOut(userCertifiedReward / 100D, billType, MessageFormat.format(systemBillDetailTemplate, orderId, userId));
                } catch (Exception e) {
                    String template = "Create system bill certified reward failed: rewardId = {0}";
                    log.error(MessageFormat.format(template, rewardId));
                    log.error(e);
                }
            }
        }

        if (!reward.getFirstRechargeReward() && successRechargeExist) {
            String orderId = MessageFormat.format(rechargeOrderIdTemplate, rewardId);
            boolean result = transferMerToUser(orderId, accountId, Integer.toString(userRechargeReward));
            if (result) {
                reward.setFirstRechargeReward(true);
                reward.setFirstRechargeRewardTime(new Date());
                updateActivityReward(reward);
                try {
                    userBillBO.transferIntoBalance(userId, userRechargeReward / 100D, billType, "新用户充值成功奖励");
                } catch (Exception e) {
                    String template = "Create user bill recharge reward failed: rewardId = {0}";
                    log.error(MessageFormat.format(template, rewardId));
                    log.error(e);
                }
                try {
                    String systemBillDetailTemplate = "新用户充值成功奖励: orderId = {0}, userId = {1}";
                    systemBillService.transferOut(userRechargeReward / 100D, billType, MessageFormat.format(systemBillDetailTemplate, orderId, userId));
                } catch (Exception e) {
                    String template = "Create system bill recharge reward failed: rewardId = {0}";
                    log.error(MessageFormat.format(template, rewardId));
                    log.error(e);
                }
            }
        }
    }

    private void rewardReferrer(JulyActivityReward reward, boolean successRechargeExist, boolean successInvestExist) {
        String billType = "activity_reward";
        String referrerCertOrderIdTemplate = "3JulyActivity{0}";
        String referrerRechargeOrderIdTemplate = "4JulyActivity{0}";
        String referrerInvestOrderIdTemplate = "5JulyActivity{0}";

        String rewardId = Long.toString(reward.getId());
        String userId = reward.getUser().getId();
        String referrerId = reward.getReferrer().getId();
        String accountId = this.getAccount(referrerId);


        if (Strings.isNullOrEmpty(accountId) || !this.isSuccessBindBankCard(referrerId)) {
            String template = "Referrer account is not exist or no bank card: referrerId = {0}, rewardId = {1}";
            log.error(MessageFormat.format(template, referrerId, rewardId));
            return;
        }

        if (!reward.getReferrerCertifiedReward()) {
            String orderId = MessageFormat.format(referrerCertOrderIdTemplate, rewardId);
            boolean result = transferMerToUser(orderId, accountId, Integer.toString(referrerCertifiedReward));
            if (result) {
                reward.setReferrerCertifiedReward(true);
                reward.setReferrerCertifiedRewardTime(new Date());
                updateActivityReward(reward);
                try {
                    String operatorDetail = "推荐新用户（{0}）实名认证奖励";
                    userBillBO.transferIntoBalance(referrerId, referrerCertifiedReward / 100D, billType, MessageFormat.format(operatorDetail, userId));
                } catch (Exception e) {
                    String template = "Create referrer bill certified reward failed: rewardId = {0}";
                    log.error(MessageFormat.format(template, rewardId));
                    log.error(e);
                }
                try {
                    String systemBillDetailTemplate = "推荐新用户实名认证奖励: orderId = {0}, referrerId = {1}, userId = {2}";
                    systemBillService.transferOut(referrerCertifiedReward / 100D, billType, MessageFormat.format(systemBillDetailTemplate, orderId, referrerId, userId));
                } catch (Exception e) {
                    String template = "Create system bill certified reward failed: rewardId = {0}";
                    log.error(MessageFormat.format(template, rewardId));
                    log.error(e);
                }
            }
        }

        if (!reward.getReferrerFirstRechargeReward() && successRechargeExist) {
            String orderId = MessageFormat.format(referrerRechargeOrderIdTemplate, rewardId);
            boolean result = transferMerToUser(orderId, accountId, Integer.toString(referrerRechargeReward));
            if (result) {
                reward.setReferrerFirstRechargeReward(true);
                reward.setReferrerFirstInvestRewardTime(new Date());
                updateActivityReward(reward);
                try {
                    String operatorDetail = "推荐新用户（{0}）充值成功奖励";
                    userBillBO.transferIntoBalance(referrerId, referrerRechargeReward / 100D, billType, MessageFormat.format(operatorDetail, userId));
                } catch (Exception e) {
                    String template = "Create referrer bill recharge reward failed: rewardId = {0}";
                    log.error(MessageFormat.format(template, rewardId));
                    log.error(e);
                }
                try {
                    String systemBillDetailTemplate = "推荐新用户充值成功奖励: orderId = {0}, rechargeId= {1} referrerId = {2}, userId = {3}";
                    systemBillService.transferOut(referrerRechargeReward / 100D, billType, MessageFormat.format(systemBillDetailTemplate, orderId, accountId, referrerId, userId));
                } catch (Exception e) {
                    String template = "Create system bill recharge reward failed: rewardId = {0}";
                    log.error(MessageFormat.format(template, rewardId));
                    log.error(e);
                }
            }
        }

        if (!reward.getReferrerFirstInvestReward() && successInvestExist) {
            String orderId = MessageFormat.format(referrerInvestOrderIdTemplate, rewardId);
            boolean result = transferMerToUser(orderId, accountId, Integer.toString(referrerInvestReward));
            if (result) {
                reward.setReferrerFirstInvestReward(true);
                reward.setReferrerFirstInvestRewardTime(new Date());
                updateActivityReward(reward);
                try {
                    String operatorDetail = "推荐新用户（{0}）投资成功奖励";
                    userBillBO.transferIntoBalance(referrerId, referrerInvestReward / 100D, billType, MessageFormat.format(operatorDetail, userId));
                } catch (Exception e) {
                    String template = "Create referrer bill invest reward failed: rewardId = {0}";
                    log.error(MessageFormat.format(template, rewardId));
                    log.error(e);
                }
                try {
                    String systemBillDetailTemplate = "推荐新用户投资成功奖励: orderId = {0}, investId= {1}, referrerId = {2}, userId = {3}";
                    systemBillService.transferOut(referrerInvestReward / 100D, billType, MessageFormat.format(systemBillDetailTemplate, orderId, accountId, referrerId, userId));
                } catch (Exception e) {
                    String template = "Create system bill invest reward failed: rewardId = {0}";
                    log.error(MessageFormat.format(template, rewardId));
                    log.error(e);
                }
            }
        }
    }

    private boolean isSuccessInvestExist(String userId) {
        try {
            DetachedCriteria investCriteria = DetachedCriteria.forClass(Invest.class);
            investCriteria.createAlias("loan", "loan")
                    .add(Restrictions.eq("user.id", userId))
                    .add(Restrictions.eq("status", InvestConstants.InvestStatus.BID_SUCCESS))
                    .add(Restrictions.or(Restrictions.and(Restrictions.eq("loan.loanActivityType", LoanConstants.LoanActivityType.XS), Restrictions.ge("money", 500D)),
                            Restrictions.and(Restrictions.not(Restrictions.eq("loan.loanActivityType", LoanConstants.LoanActivityType.XS)), Restrictions.ge("money", 1000D))))
                    .addOrder(Order.asc("time"));
            List<Invest> invests = ht.findByCriteria(investCriteria, 0, 1);
            return CollectionUtils.isNotEmpty(invests);
        } catch (Exception e) {
            String template = "Query user success invest failed: user = {0}";
            log.error(MessageFormat.format(template, userId));
            log.error(e);
        }
        return false;
    }

    private boolean isSuccessRechargeExist(String userId) {
        try {
            DetachedCriteria rechargeCriteria = DetachedCriteria.forClass(Recharge.class);
            rechargeCriteria.add(Restrictions.eq("user.id", userId))
                    .add(Restrictions.eq("status", UserConstants.RechargeStatus.SUCCESS))
                    .add(Restrictions.not(Restrictions.eq("rechargeWay", "admin")))
                    .addOrder(Order.asc("successTime"));

            List<Recharge> recharges = ht.findByCriteria(rechargeCriteria, 0, 1);
            return CollectionUtils.isNotEmpty(recharges);
        } catch (Exception e) {
            String template = "Query user success recharge failed: user = {0}";
            log.error(MessageFormat.format(template, userId));
            log.error(e);
        }
        return false;
    }


    private boolean transferMerToUser(String orderId, String accountId, String cent) {
        ReqData reqData = generateReqData(orderId, accountId, cent);
        if (reqData == null) {
            return false;
        }

        TrusteeshipOperation operation = createTrusteeshipOperation(orderId, reqData);
        if (operation == null) {
            return false;
        }

        try {
            log.info("Request Url: " + reqData.getUrl());
            String responseBodyAsString = HttpClientUtil.getResponseBodyAsString(reqData.getUrl());
            Map<String, String> resData = Plat2Mer_v40.getResData(responseBodyAsString);
            log.info("Response Data" + resData);
            return updateTrusteeshipOperationStatus(operation, resData);
        } catch (Exception e) {
            String template = "Activity reward transfer failed: orderId = {0}, accountId = {1}, cent = {2}";
            log.error(MessageFormat.format(template, orderId, accountId, cent));
            log.error(e);
        }
        return false;
    }

    private ReqData generateReqData(String orderId, String accountId, String cent) {
        try {
            Map<String, String> sendMap = UmPaySignUtil.getSendMapDate(UmPayConstants.OperationType.TRANSFER);
            sendMap.put("order_id", orderId);
            sendMap.put("partic_acc_type", UmPayConstants.TransferProjectStatus.PARTIC_ACC_TYPE_PERSON); // 转账方账户类型(实际收款方)
            sendMap.put("trans_action", UmPayConstants.TransferProjectStatus.TRANS_ACTION_OUT); // 转账方向
            sendMap.put("partic_user_id", accountId); //转账方用户号(实际收款方)
            sendMap.put("amount", cent); // 单位为分
            sendMap.put("mer_date", DateUtil.DateToString(new Date(), DateStyle.YYYYMMDD));
            ReqData reqData = Mer2Plat_v40.makeReqDataByGet(sendMap);
            log.info("reqDate: " + reqData.getPlain());
            return reqData;
        } catch (Exception e) {
            String template = "Generate req data failed: accountId = {0}, cent = {cent}";
            log.error(MessageFormat.format(template, accountId, cent));
            log.error(e);
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    private TrusteeshipOperation createTrusteeshipOperation(String markId, ReqData reqData) {
        try {
            TrusteeshipOperation operation = new TrusteeshipOperation();
            operation.setId(IdGenerator.randomUUID());
            operation.setMarkId("JulyActivity" + markId);
            operation.setOperator("activityJob");
            operation.setRequestUrl(reqData.getUrl());
            operation.setCharset("utf-8");
            operation.setRequestData(reqData.getPlain());
            operation.setType(UmPayConstants.OperationType.PROJECT_TRANSFER);
            operation.setTrusteeship("umpay");
            operation.setRequestTime(new Date());
            operation.setStatus(TrusteeshipConstants.Status.SENDED);
            ht.save(operation);
            return operation;
        } catch (Exception e) {
            log.error("Create operation failed: " + reqData.getPlain());
            log.error(e);
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
            log.error(e);
        }
        return SUCCESS_CODE.equals(resData.get("ret_code"));
    }

    @Transactional
    private boolean updateActivityReward(JulyActivityReward reward) {
        try {
            ht.saveOrUpdate(reward);
            return true;
        } catch (Exception e) {
            log.error("Update JulyActivityReward failed: " + reward.toString());
            log.error(e);
        }
        return false;
    }

    private String getAccount(String userId) {
        try {
            DetachedCriteria accountCriteria = DetachedCriteria.forClass(TrusteeshipAccount.class);
            accountCriteria.add(Restrictions.eq("user.id", userId));
            TrusteeshipAccount account = (TrusteeshipAccount) DataAccessUtils.uniqueResult(ht.findByCriteria(accountCriteria, 0, 1));
            return account.getId();
        } catch (Exception e) {
            String template = "Query umpay account failed: userId = {0}";
            log.error(MessageFormat.format(template, userId));
            log.error(e);
        }
        return null;
    }

    private boolean isSuccessBindBankCard(String userId) {
        try {
            DetachedCriteria bankCardCriteria = DetachedCriteria.forClass(BankCard.class);
            bankCardCriteria.add(Restrictions.eq("user.id", userId))
                    .add(Restrictions.eq("status", "passed"));
            List<BankCard> bankCards = ht.findByCriteria(bankCardCriteria, 0, 1);
            boolean successBindBankCard = CollectionUtils.isNotEmpty(bankCards);
            if (!successBindBankCard) {
                String template = "Not bind bank card: userId = {0}";
                log.error(MessageFormat.format(template, userId));
            }
            return successBindBankCard;
        } catch (Exception e) {
            String template = "Query bank card failed: userId = {0}";
            log.error(MessageFormat.format(template, userId));
            log.error(e);
        }
        return false;
    }

}
