package com.tuotiansudai.message.util;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMetaMapper;
import com.tuotiansudai.message.repository.model.MessageEventType;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.repository.mapper.LoginLogMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserMessageEventGenerator {

    private final static Map<String, String> COUPON_NAME_MAPPING = Maps.newHashMap(ImmutableMap.<String, String>builder()
            .put("RED_ENVELOPE", "现金红包")
            .put("NEWBIE_COUPON", "新手体验金")
            .put("INVEST_COUPON", "投资体验券")
            .put("INTEREST_COUPON", "加息券")
            .put("BIRTHDAY_COUPON", "生日福利券")
            .build());

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMessageMetaMapper userMessageMetaMapper;

    @Autowired
    private LoginLogMapper loginLogMapper;

    public void generateRegisterUserSuccessEvent(String loginName) {
        MessageModel registerUserSuccessMessage = messageMapper.findActiveByEventType(MessageEventType.REGISTER_USER_SUCCESS);
        //终于等到你，欢迎来到拓天速贷平台。
        UserMessageModel registerUserMessageModel = new UserMessageModel(registerUserSuccessMessage.getId(), loginName, registerUserSuccessMessage.getTitle(), registerUserSuccessMessage.getAppTitle(), null);
        userMessageMapper.create(registerUserMessageModel);

        UserModel userModel = userMapper.findByLoginName(loginName);
        if (!Strings.isNullOrEmpty(userModel.getReferrer())) {
            MessageModel recommendSuccessMessage = messageMapper.findActiveByEventType(MessageEventType.RECOMMEND_SUCCESS);
            //您推荐的好友 {0} 成功注册，若该好友进行投资，您即可获取现金奖励哦
            String titleTemplate = recommendSuccessMessage.getTitle();
            String title = MessageFormat.format(titleTemplate, userModel.getMobile());

            //您推荐的好友{0}成功注册，若该好友进行投资，您即可获取现金奖励哦
            String appTitleTemplate = recommendSuccessMessage.getAppTitle();
            String appTitle = MessageFormat.format(appTitleTemplate, userModel.getMobile());
            UserMessageModel userMessageModel = new UserMessageModel(recommendSuccessMessage.getId(), userModel.getReferrer(), title, appTitle, null);
            userMessageMapper.create(userMessageModel);
        }
    }

    @Transactional
    public void generateRegisterAccountSuccessEvent(String loginName) {
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.REGISTER_ACCOUNT_SUCCESS);
        //实名认证成功，您的支付密码已经由联动优势发送至注册手机号码，请牢记。
        String title = messageModel.getTitle();
        String appTitle = messageModel.getAppTitle();
        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, null);
        userMessageMapper.create(userMessageModel);
    }

    @Transactional
    public void generateRechargeSuccessEvent(long rechargeId) {
        Map<String, Object> recharge = userMessageMetaMapper.findRechargeById(rechargeId);
        if (recharge == null) {
            return;
        }
        long amount = (long) recharge.get("amount");
        String loginName = (String) recharge.get("login_name");
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.RECHARGE_SUCCESS);
        //充值成功：您已成功充值 {0} 元，及时<a href="/loan-list">投资赚取更多</a>哦。
        String titleTemplate = messageModel.getTitle();
        String title = MessageFormat.format(titleTemplate, AmountConverter.convertCentToString(amount));

        //充值成功：您已成功充值{0}元，及时投资赚取更多哦。
        String appTitleTemplate = messageModel.getAppTitle();
        String appTitle = MessageFormat.format(appTitleTemplate, AmountConverter.convertCentToString(amount));
        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, null);
        userMessageMapper.create(userMessageModel);
    }

    @Transactional
    public void generateWithdrawSuccessEvent(long withdrawId) {
        Map<String, Object> withdraw = userMessageMetaMapper.findWithdrawById(withdrawId);
        if (withdraw == null) {
            return;
        }
        long amount = (long) withdraw.get("amount");
        String loginName = (String) withdraw.get("login_name");

        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.WITHDRAW_SUCCESS);
        //提现成功：您已成功提现 {0} 元，选择拓天，共赢财富。
        String titleTemplate = messageModel.getTitle();
        String title = MessageFormat.format(titleTemplate, AmountConverter.convertCentToString(amount));

        //提现成功：您已成功提现{0}元，选择拓天，共赢财富。
        String appTitleTemplate = messageModel.getAppTitle();
        String appTitle = MessageFormat.format(appTitleTemplate, AmountConverter.convertCentToString(amount));
        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, null);
        userMessageMapper.create(userMessageModel);
    }

    @Transactional
    public void generateInvestSuccessEvent(long investId) {
        Map<String, Object> invest = userMessageMetaMapper.findInvestById(investId);
        long loanId = ((BigInteger) invest.get("loan_id")).longValue();
        long amount = ((BigInteger) invest.get("amount")).longValue();
        String loginName = (String) invest.get("login_name");

        String loanName = userMessageMetaMapper.findLoanNameById(loanId);
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.INVEST_SUCCESS);
        //投标成功：您在<a href="/loan/{0}">{1}</a>项目成功投资 {2} 元，不日即将放款。
        String titleTemplate = messageModel.getTitle();
        String title = MessageFormat.format(titleTemplate, String.valueOf(loanId), loanName, AmountConverter.convertCentToString(amount));

        //投标成功：您在{0}项目成功投资{1}元，即将放款生息。
        String appTitleTemplate = messageModel.getAppTitle();
        String appTitle = MessageFormat.format(appTitleTemplate, loanName, AmountConverter.convertCentToString(amount));
        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, null);
        userMessageMapper.create(userMessageModel);
    }

    @Transactional
    public void generateTransferSuccessEvent(long investId) {
        Map<String, Object> invest = userMessageMetaMapper.findTransferApplicationByInvestId(investId);
        long transferApplicationId = ((BigInteger) invest.get("id")).longValue();
        String name = (String) invest.get("name");
        String loginName = (String) invest.get("login_name");

        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.TRANSFER_SUCCESS);
        //您发起的转让项目<a href="/transfer/{0}">{1}</a>已经转让成功，资金已经到达您的账户。
        String titleTemplate = messageModel.getTitle();
        String title = MessageFormat.format(titleTemplate, String.valueOf(transferApplicationId), name);

        //您发起的转让项目{0}转让成功，资金已经到达您的账户。
        String appTitleTemplate = messageModel.getAppTitle();
        String appTitle = MessageFormat.format(appTitleTemplate, name);
        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, null);
        userMessageMapper.create(userMessageModel);
    }

    @Transactional
    public void generateLoanOutSuccessEvent(long loanId) {
        String loanName = userMessageMetaMapper.findLoanNameById(loanId);
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.LOAN_OUT_SUCCESS);
        //您投资的<a href="/loan/{0}">{1}</a>项目已经满额放款，快来<a href="/user-bill">查看收益</a>吧。
        String titleTemplate = messageModel.getTitle();
        String title = MessageFormat.format(titleTemplate, String.valueOf(loanId), loanName);

        //您投资的{0}项目已经满额放款，即日生息。
        String appTitleTemplate = messageModel.getAppTitle();
        String appTitle = MessageFormat.format(appTitleTemplate, loanName);

        Set<String> investorLoginNames = Sets.newHashSet(userMessageMetaMapper.findSuccessInvestorByLoanId(loanId));
        for (String investor : investorLoginNames) {
            UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), investor, title, appTitle, null);
            userMessageMapper.create(userMessageModel);
        }
    }

    @Transactional
    public void generateRepaySuccessEvent(long loanRepayId) {
        Map<String, Object> loan = userMessageMetaMapper.findLoanByLoanRepayId(loanRepayId);
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.REPAY_SUCCESS);
        //您投资的<a href="/loan/{0}">{1}</a>项目还款啦，赶快<a href="/user-bill">查看收益</a>吧。
        String titleTemplate = messageModel.getTitle();
        long loanId = ((BigInteger) loan.get("id")).longValue();
        String title = MessageFormat.format(titleTemplate, String.valueOf(loanId), loan.get("name"));

        //您投资的{0}项目还款啦，赶快查看收益吧。
        String appTitleTemplate = messageModel.getAppTitle();
        String appTitle = MessageFormat.format(appTitleTemplate, loan.get("name"));

        Set<String> investorLoginNames = Sets.newHashSet(userMessageMetaMapper.findSuccessInvestorByLoanId(loanId));
        for (String investor : investorLoginNames) {
            UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), investor, title, appTitle, null);
            userMessageMapper.create(userMessageModel);
        }
    }

    @Transactional
    public void generateRecommendAwardSuccessEvent(long loanId) {
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.RECOMMEND_AWARD_SUCCESS);
        //您推荐的好友 {0} 成功进行了投资，您获得了 {1} 元现金奖励，<a href="/user-bill">立即查看</a>。
        String titleTemplate = messageModel.getTitle();
        //您的好友{0}成功进行了投资，您获得了{1}元现金奖励。
        String appTitleTemplate = messageModel.getAppTitle();

        List<Map<String, Object>> investReferrerRewards = userMessageMetaMapper.findInvestReferrerRewardByLoanId(loanId);

        for (Map<String, Object> investReferrerReward : investReferrerRewards) {
            long amount = ((BigInteger) investReferrerReward.get("amount")).longValue();
            String title = MessageFormat.format(titleTemplate, investReferrerReward.get("mobile"), AmountConverter.convertCentToString(amount));
            String appTitle = MessageFormat.format(appTitleTemplate, investReferrerReward.get("mobile"), AmountConverter.convertCentToString(amount));
            UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), (String) investReferrerReward.get("referrer"), title, appTitle, null);
            userMessageMapper.create(userMessageModel);
        }
    }

    @Transactional
    public void generateCouponExpiredAlertEvent(String loginName) {
        long times = loginLogMapper.countSuccessTimesOnDate(loginName, new Date(), MessageFormat.format("login_log_{0}", new DateTime().toString("yyyyMM")));
        if (times == 1) {
            MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.COUPON_5DAYS_EXPIRED_ALERT);
            //您的{0}即将到期，使用可以获取额外收益哦，<a href="/my-treasure">立即使用</a>。
            String titleTemplate = messageModel.getTitle();

            //您的{0}即将到期，使用可以获取额外收益哦，快去我的宝藏查看吧。
            String appTitleTemplate = messageModel.getAppTitle();

            List<Map<String, Object>> userCouponModels = userMessageMetaMapper.findCouponWillExpire(loginName);
            for (Map<String, Object> userCoupon : userCouponModels) {
                String title;
                String appTitle;
                String couponType = (String) userCoupon.get("type");
                switch (couponType) {
                    case "RED_ENVELOPE":
                    case "NEWBIE_COUPON":
                    case "INVEST_COUPON":
                        long amount = (long) userCoupon.get("amount");
                        title = MessageFormat.format(titleTemplate, AmountConverter.convertCentToString(amount) + "元" + COUPON_NAME_MAPPING.get(couponType));
                        appTitle = MessageFormat.format(appTitleTemplate, AmountConverter.convertCentToString(amount) + "元" + COUPON_NAME_MAPPING.get(couponType));
                        break;
                    case "INTEREST_COUPON":
                        double rate = (double) userCoupon.get("rate");
                        title = MessageFormat.format(titleTemplate, new BigDecimal(rate).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%" + COUPON_NAME_MAPPING.get(couponType));
                        appTitle = MessageFormat.format(appTitleTemplate, new BigDecimal(rate).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%" + COUPON_NAME_MAPPING.get(couponType));
                        break;
                    default:
                        title = MessageFormat.format(titleTemplate, COUPON_NAME_MAPPING.get(couponType));
                        appTitle = MessageFormat.format(appTitleTemplate, COUPON_NAME_MAPPING.get(couponType));
                        break;
                }
                UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, null);
                userMessageMapper.create(userMessageModel);
            }
        }
    }

    @Transactional
    public void generateAssignCouponSuccessEvent(long userCouponId) {
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.ASSIGN_COUPON_SUCCESS);
        //您获得了{0}，有效期{1}至{2}，<a href="/my-treasure">立即查看</a>。
        String titleTemplate = messageModel.getTitle();

        //您获得了{0}，有效期{1}至{2}。
        String appTitleTemplate = messageModel.getAppTitle();

        Map<String, Object> userCoupon = userMessageMetaMapper.findAssignUserCoupon(userCouponId);
        String startTime = new DateTime(userCoupon.get("start_time")).toString("yyyy-MM-dd");
        String endTime = new DateTime(userCoupon.get("end_time")).toString("yyyy-MM-dd");
        String title;
        String appTitle;

        String couponType = (String) userCoupon.get("type");
        switch (couponType) {
            case "RED_ENVELOPE":
            case "NEWBIE_COUPON":
            case "INVEST_COUPON":
                long amount = (long) userCoupon.get("amount");
                title = MessageFormat.format(titleTemplate,
                        AmountConverter.convertCentToString(amount) + "元" + COUPON_NAME_MAPPING.get(couponType),
                        startTime,
                        endTime);

                appTitle = MessageFormat.format(appTitleTemplate,
                        AmountConverter.convertCentToString(amount) + "元" + COUPON_NAME_MAPPING.get(couponType),
                        startTime,
                        endTime);
                break;
            case "INTEREST_COUPON":
                double rate = (double) userCoupon.get("rate");
                title = MessageFormat.format(titleTemplate,
                        new BigDecimal(rate).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%" + COUPON_NAME_MAPPING.get(couponType),
                        startTime,
                        endTime);

                appTitle = MessageFormat.format(appTitleTemplate,
                        new BigDecimal(rate).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%" + COUPON_NAME_MAPPING.get(couponType),
                        startTime,
                        endTime);
                break;
            default:
                title = MessageFormat.format(titleTemplate, COUPON_NAME_MAPPING.get(couponType), startTime, endTime);
                appTitle = MessageFormat.format(appTitleTemplate, COUPON_NAME_MAPPING.get(couponType), startTime, endTime);
                break;
        }
        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), (String) userCoupon.get("login_name"), title, appTitle, null);
        userMessageMapper.create(userMessageModel);
    }

    public void generateMembershipExpiredEvent(String loginName) {
        boolean isExisted = userMessageMetaMapper.isExpiredLevelFiveMembershipExisted(loginName);
        if (!isExisted) {
            return;
        }

        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.MEMBERSHIP_EXPIRED);
        //会员到期提醒
        String title = messageModel.getTitle();
        //会员到期提醒
        String appTitle = messageModel.getAppTitle();
        //您购买的V5会员已到期，V5会员可享受服务费7折优惠，记得及时购买哦！
        String content = messageModel.getTemplate();

        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, content);
        userMessageMapper.create(userMessageModel);
    }
}
