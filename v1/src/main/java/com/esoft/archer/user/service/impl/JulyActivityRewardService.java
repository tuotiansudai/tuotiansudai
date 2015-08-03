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
import com.google.common.collect.Lists;
import com.umpay.api.common.ReqData;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
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

    private List<String> multipleBankCardUsers = Lists.newArrayList();

    private static String SUCCESS_CODE = "0000";

    private static Date activityStartTime = new DateTime(2015, 7, 1, 0, 0, 0).toDate();
    private static Date activityEndTime = new DateTime(2015, 7, 14, 23, 59, 59).toDate();
    private static int USER_CERTIFIED_REWARD = 500;
    private static int USER_RECHARGE_REWARD = 500;
    private static int REFERRER_CERTIFIED_REWARD = 1000;
    private static int REFERRER_RECHARGE_REWARD = 1000;
    private static int REFERRER_INVEST_REWARD = 3000;
    private static int TOTAL_REWARD = 0;
    private static final String NEW_REGISTER_USER_SQL = "SELECT u.id FROM user u INNER JOIN trusteeship_account ta ON u.id = ta.user_id AND(u.register_time BETWEEN '2015-07-01 00:00:00' AND '2015-07-14 23:59:59') AND (ta.create_time BETWEEN '2015-07-01 00:00:00' AND '2015-07-14 23:59:59') WHERE NOT EXISTS (SELECT 1 FROM july_activity_reward jar where jar.user_id=u.id) AND EXISTS (SELECT 1 FROM bank_card bc WHERE u.id=bc.user_id AND bc.status='passed' AND (bc.time BETWEEN '2015-07-01 00:00:00' AND '2015-07-14 23:59:59'))";
    private static final String MULTIPLE_BANK_CARD_USER_SQL = "select a.user_id from (select distinct card_no, user_id from bank_card where status ='passed' and card_no in (select card_no from bank_card where status ='passed' group by card_no having count(1) > 1)) as a group by a.card_no having count(1) > 1";

    @Transactional
    public void createActivityRewards() {
        log.info(MessageFormat.format("Scan register time from {0}", activityStartTime.toString()));

        Query query = ht.getSessionFactory().getCurrentSession().createSQLQuery(NEW_REGISTER_USER_SQL);
        List<String> userIdList = query.list();

        log.info(MessageFormat.format("Found {0} register user", CollectionUtils.isEmpty(userIdList) ? 0 : userIdList.size()));

        for (String userId : userIdList) {
            try {
                JulyActivityReward reward = new JulyActivityReward();
                DetachedCriteria userCriteria = DetachedCriteria.forClass(User.class);
                userCriteria.add(Restrictions.eq("id", userId));
                User user = (User) DataAccessUtils.uniqueResult(ht.findByCriteria(userCriteria));
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
            } catch (Exception e) {
                String template = "Create activity reward failed: userId = {0}";
                log.error(MessageFormat.format(template, userId));
                log.error(e.getLocalizedMessage(), e);
            }
        }

        this.multipleBankCardUsers = this.getUserBoundMultipleBankCards();

    }

    public void reward() {
        log.info("start reward:");
        TOTAL_REWARD = 0;
        int start = 0;
        int limit = 50;
        boolean loop = true;

        while (loop) {
            if (TOTAL_REWARD > 450000 * 100) {
                log.error("No enough money! Stop reward!");
                break;
            }
            List<JulyActivityReward> rewards = this.fetchRewardList(start);
            loop = CollectionUtils.isNotEmpty(rewards);
            if (loop) {
                log.info(MessageFormat.format("Reward index form {0} to {1}", start, start + limit));
                for (JulyActivityReward reward : rewards) {
                    String userId = reward.getUser().getId();
                    String referrerId = reward.getReferrer() != null ? reward.getReferrer().getId() : "";

                    if (multipleBankCardUsers.indexOf(userId) == -1 && (Strings.isNullOrEmpty(referrerId) || multipleBankCardUsers.indexOf(referrerId) == -1)) {
                        boolean successRechargeExist = this.isSuccessRechargeExist(userId);
                        boolean successInvestExist = this.isSuccessInvestExist(userId);
                        this.rewardUser(reward, successRechargeExist);

                        if (!Strings.isNullOrEmpty(referrerId)) {
                            this.rewardReferrer(reward, successRechargeExist, successInvestExist);
                        }
                    } else {
                        log.info(MessageFormat.format("user = {0} or referrer = {1} have bound multiple bank cards.", userId, referrerId));
                    }
                }
                start += limit;
            }
        }

        log.info("Total Reward: " + TOTAL_REWARD / 100);
    }

    private void rewardUser(JulyActivityReward reward, boolean successRechargeExist) {
        String billType = "activity_reward";
        String certOrderIdTemplate = "1JulyActivity{0}";
        String rechargeOrderIdTemplate = "2JulyActivity{0}";

        String rewardId = Long.toString(reward.getId());
        String userId = reward.getUser().getId();

        if (!this.isSuccessBindBankCard(userId, true)) {
            String template = "User have no bank card: userId = {0}, rewardId = {1}";
            log.error(MessageFormat.format(template, userId, rewardId));
            return;
        }

        String accountId = this.getAccount(userId);
        if (!reward.getCertifiedReward()) {
            String orderId = MessageFormat.format(certOrderIdTemplate, rewardId);
            boolean result = transferMerToUser(orderId, accountId, Integer.toString(USER_CERTIFIED_REWARD));
            if (result) {
                TOTAL_REWARD += USER_CERTIFIED_REWARD;
                reward.setCertifiedReward(true);
                reward.setCertifiedRewardTime(new Date());
                updateActivityReward(reward);
                try {
                    certifiedReward(billType, rewardId, userId, orderId);
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }

        if (!reward.getFirstRechargeReward() && successRechargeExist) {
            String orderId = MessageFormat.format(rechargeOrderIdTemplate, rewardId);
            boolean result = transferMerToUser(orderId, accountId, Integer.toString(USER_RECHARGE_REWARD));
            if (result) {
                TOTAL_REWARD += USER_RECHARGE_REWARD;
                reward.setFirstRechargeReward(true);
                reward.setFirstRechargeRewardTime(new Date());
                updateActivityReward(reward);
                try {
                    firstRechargeReward(billType, rewardId, userId, orderId);
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    @Transactional
    private void firstRechargeReward(String billType, String rewardId, String userId, String orderId) throws Exception {
        try {
            userBillBO.transferIntoBalance(userId, USER_RECHARGE_REWARD / 100D, billType, "新用户充值成功奖励");
        } catch (Exception e) {
            String template = "Create user bill recharge reward failed: rewardId = {0}";
            log.error(MessageFormat.format(template, rewardId));
            throw e;
        }
        try {
            String systemBillDetailTemplate = "新用户充值成功奖励: orderId = {0}, userId = {1}";
            systemBillService.transferOut(USER_RECHARGE_REWARD / 100D, billType, MessageFormat.format(systemBillDetailTemplate, orderId, userId));
        } catch (Exception e) {
            String template = "Create system bill recharge reward failed: rewardId = {0}";
            log.error(MessageFormat.format(template, rewardId));
            throw e;
        }
    }

    @Transactional
    private void certifiedReward(String billType, String rewardId, String userId, String orderId) throws Exception {
        try {
            userBillBO.transferIntoBalance(userId, USER_CERTIFIED_REWARD / 100D, billType, "新用户实名认证奖励");
        } catch (Exception e) {
            String template = "Create user bill certified reward failed: rewardId = {0}";
            log.error(MessageFormat.format(template, rewardId));
            throw e;
        }
        try {
            String systemBillDetailTemplate = "新用户实名认证奖励: orderId = {0}, userId = {1}";
            systemBillService.transferOut(USER_CERTIFIED_REWARD / 100D, billType, MessageFormat.format(systemBillDetailTemplate, orderId, userId));
        } catch (Exception e) {
            String template = "Create system bill certified reward failed: rewardId = {0}";
            log.error(MessageFormat.format(template, rewardId));
            throw e;

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

        if (!this.isSuccessBindBankCard(referrerId, false) || !this.isSuccessBindBankCard(userId, true)) {
            String template = "Referrer or user have no bank card: referrerId = {0}, userId = {1}, rewardId = {2}";
            log.error(MessageFormat.format(template, referrerId, userId, rewardId));
            return;
        }

        String accountId = this.getAccount(referrerId);
        if (!reward.getReferrerCertifiedReward()) {
            String orderId = MessageFormat.format(referrerCertOrderIdTemplate, rewardId);

            boolean result = transferMerToUser(orderId, accountId, Integer.toString(REFERRER_CERTIFIED_REWARD));
            if (result) {
                TOTAL_REWARD += REFERRER_CERTIFIED_REWARD;
                reward.setReferrerCertifiedReward(true);
                reward.setReferrerCertifiedRewardTime(new Date());
                updateActivityReward(reward);
                try {
                    referrerCertifiedReward(billType, rewardId, userId, referrerId, orderId);
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }

        if (!reward.getReferrerFirstRechargeReward() && successRechargeExist) {
            String orderId = MessageFormat.format(referrerRechargeOrderIdTemplate, rewardId);
            boolean result = transferMerToUser(orderId, accountId, Integer.toString(REFERRER_RECHARGE_REWARD));
            if (result) {
                TOTAL_REWARD += REFERRER_RECHARGE_REWARD;
                reward.setReferrerFirstRechargeReward(true);
                reward.setReferrerFirstRechargeRewardTime(new Date());
                updateActivityReward(reward);
                try {
                    referrerFirstRechargeReward(billType, rewardId, userId, referrerId, orderId);
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }

        if (!reward.getReferrerFirstInvestReward() && successInvestExist) {
            String orderId = MessageFormat.format(referrerInvestOrderIdTemplate, rewardId);
            boolean result = transferMerToUser(orderId, accountId, Integer.toString(REFERRER_INVEST_REWARD));
            if (result) {
                TOTAL_REWARD += REFERRER_INVEST_REWARD;
                reward.setReferrerFirstInvestReward(true);
                reward.setReferrerFirstInvestRewardTime(new Date());
                updateActivityReward(reward);
                try {
                    referrerFirstInvestReward(billType, rewardId, userId, referrerId, accountId, orderId);
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    @Transactional
    private void referrerFirstInvestReward(String billType, String rewardId, String userId, String referrerId, String accountId, String orderId) throws Exception {
        try {
            String operatorDetail = "推荐新用户（{0}）投资成功奖励";
            userBillBO.transferIntoBalance(referrerId, REFERRER_INVEST_REWARD / 100D, billType, MessageFormat.format(operatorDetail, userId));
        } catch (Exception e) {
            String template = "Create referrer bill invest reward failed: rewardId = {0}";
            log.error(MessageFormat.format(template, rewardId));
            throw e;
        }
        try {
            String systemBillDetailTemplate = "推荐新用户投资成功奖励: orderId = {0}, referrerId = {1}, userId = {2}";
            systemBillService.transferOut(REFERRER_INVEST_REWARD / 100D, billType, MessageFormat.format(systemBillDetailTemplate, orderId, accountId, referrerId, userId));
        } catch (Exception e) {
            String template = "Create system bill invest reward failed: rewardId = {0}";
            log.error(MessageFormat.format(template, rewardId));
            throw e;
        }
    }

    @Transactional
    private void referrerFirstRechargeReward(String billType, String rewardId, String userId, String referrerId, String orderId) throws Exception {
        try {
            String operatorDetail = "推荐新用户（{0}）充值成功奖励";
            userBillBO.transferIntoBalance(referrerId, REFERRER_RECHARGE_REWARD / 100D, billType, MessageFormat.format(operatorDetail, userId));
        } catch (Exception e) {
            String template = "Create referrer bill recharge reward failed: rewardId = {0}";
            log.error(MessageFormat.format(template, rewardId));
            throw e;
        }
        try {
            String systemBillDetailTemplate = "推荐新用户充值成功奖励: orderId = {0}, referrerId = {1}, userId = {2}";
            systemBillService.transferOut(REFERRER_RECHARGE_REWARD / 100D, billType, MessageFormat.format(systemBillDetailTemplate, orderId, referrerId, userId));
        } catch (Exception e) {
            String template = "Create system bill recharge reward failed: rewardId = {0}";
            log.error(MessageFormat.format(template, rewardId));
            throw e;
        }
    }

    @Transactional
    private void referrerCertifiedReward(String billType, String rewardId, String userId, String referrerId, String orderId) throws Exception {
        try {
            String operatorDetail = "推荐新用户（{0}）实名认证奖励";
            userBillBO.transferIntoBalance(referrerId, REFERRER_CERTIFIED_REWARD / 100D, billType, MessageFormat.format(operatorDetail, userId));
        } catch (Exception e) {
            String template = "Create referrer bill certified reward failed: rewardId = {0}";
            log.error(MessageFormat.format(template, rewardId));
            throw e;
        }
        try {
            String systemBillDetailTemplate = "推荐新用户实名认证奖励: orderId = {0}, referrerId = {1}, userId = {2}";
            systemBillService.transferOut(REFERRER_CERTIFIED_REWARD / 100D, billType, MessageFormat.format(systemBillDetailTemplate, orderId, referrerId, userId));
        } catch (Exception e) {
            String template = "Create system bill certified reward failed: rewardId = {0}";
            log.error(MessageFormat.format(template, rewardId));
            throw e;
        }
    }

    public boolean isSuccessInvestExist(String userId) {
        try {
            DetachedCriteria investCriteria = DetachedCriteria.forClass(Invest.class);
            investCriteria.createAlias("loan", "loan")
                    .add(Restrictions.eq("user.id", userId))
                    .add(Restrictions.between("time", activityStartTime, activityEndTime))
                    .add(Restrictions.in("status", Lists.newArrayList(InvestConstants.InvestStatus.BID_SUCCESS,
                            InvestConstants.InvestStatus.CANCEL,
                            InvestConstants.InvestStatus.COMPLETE,
                            InvestConstants.InvestStatus.REPAYING)))
                    .add(Restrictions.or(Restrictions.and(Restrictions.eq("loan.loanActivityType", LoanConstants.LoanActivityType.XS), Restrictions.ge("money", 500D)),
                            Restrictions.and(Restrictions.not(Restrictions.eq("loan.loanActivityType", LoanConstants.LoanActivityType.XS)), Restrictions.ge("money", 1000D))));
            List<Invest> invests = ht.findByCriteria(investCriteria, 0, 1);
            return CollectionUtils.isNotEmpty(invests);
        } catch (Exception e) {
            String template = "Query user success invest failed: user = {0}";
            log.error(MessageFormat.format(template, userId));
            log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    public boolean isSuccessRechargeExist(String userId) {
        try {
            DetachedCriteria rechargeCriteria = DetachedCriteria.forClass(Recharge.class);
            rechargeCriteria.add(Restrictions.eq("user.id", userId))
                    .add(Restrictions.eq("status", UserConstants.RechargeStatus.SUCCESS))
                    .add(Restrictions.between("time", activityStartTime, activityEndTime))
                    .add(Restrictions.not(Restrictions.eq("rechargeWay", "admin")));

            List<Recharge> recharges = ht.findByCriteria(rechargeCriteria, 0, 1);
            return CollectionUtils.isNotEmpty(recharges);
        } catch (Exception e) {
            String template = "Query user success recharge failed: user = {0}";
            log.error(MessageFormat.format(template, userId));
            log.error(e.getLocalizedMessage(), e);
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
            String responseBodyAsString = HttpClientUtil.getResponseBodyAsString(reqData.getUrl());
            Map<String, String> resData = Plat2Mer_v40.getResData(responseBodyAsString);
            log.info(MessageFormat.format("Pay orderId = {0}, returnCode = {1}", orderId, resData.get("ret_code")));
            return updateTrusteeshipOperationStatus(operation, resData);
        } catch (Exception e) {
            String template = "Activity reward transfer failed: orderId = {0}, accountId = {1}, cent = {2}";
            log.error(MessageFormat.format(template, orderId, accountId, cent));
            log.error(e.getLocalizedMessage(), e);
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

    @Transactional
    private boolean updateActivityReward(JulyActivityReward reward) {
        try {
            ht.saveOrUpdate(reward);
            return true;
        } catch (Exception e) {
            log.error("Update JulyActivityReward failed: " + reward.toString());
            log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    private String getAccount(String userId) {
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

    private boolean isSuccessBindBankCard(String userId, boolean isUser) {
        try {
            DetachedCriteria bankCardCriteria = DetachedCriteria.forClass(BankCard.class);
            bankCardCriteria.add(Restrictions.eq("user.id", userId))
                    .add(Restrictions.eq("status", "passed"));
            if (isUser) {
                bankCardCriteria.add(Restrictions.between("time", activityStartTime, activityEndTime));
            } else {
                bankCardCriteria.add(Restrictions.lt("time", activityEndTime));
            }

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
            log.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    private List<JulyActivityReward> fetchRewardList(int start) {
        int limit = 50;
        DetachedCriteria rewardCriteria = DetachedCriteria.forClass(JulyActivityReward.class);
        rewardCriteria.addOrder(Order.asc("id"));
        List<JulyActivityReward> rewardList = ht.findByCriteria(rewardCriteria, start, limit);
        return rewardList;
    }

    public List<String> getUserBoundMultipleBankCards() {
        List<String> multipleBankCardUsers = Lists.newArrayList();
        try {
            Query query = ht.getSessionFactory().getCurrentSession().createSQLQuery(MULTIPLE_BANK_CARD_USER_SQL);
            multipleBankCardUsers = query.list();
        } catch (HibernateException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        log.info(MessageFormat.format("User have bound multiple bank cards: {0}", multipleBankCardUsers.size()));
        return multipleBankCardUsers;
    }
}