package com.tuotiansudai.message.util;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tuotiansudai.jpush.repository.model.JPushAlertModel;
import com.tuotiansudai.jpush.service.JPushAlertNewService;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMetaMapper;
import com.tuotiansudai.message.repository.model.MessageEventType;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.repository.mapper.LoginLogMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.WithdrawStatus;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.tuotiansudai.repository.model.WithdrawStatus.APPLY_SUCCESS;
import static com.tuotiansudai.repository.model.WithdrawStatus.SUCCESS;

@Service
public class UserMessageEventGenerator {

    private static Logger logger = Logger.getLogger(UserMessageEventGenerator.class);

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

    @Autowired
    private JPushAlertNewService jPushAlertNewService;

    private void sendJPushByUserMessageModel(UserMessageModel userMessageModel) {
        try {
            Optional<JPushAlertModel> jPushAlertModelOptional = Optional.of(jPushAlertNewService.findJPushAlertModelByMessageId(userMessageModel.getMessageId()));
            if (jPushAlertModelOptional.isPresent()) {
                JPushAlertModel jPushAlertModel = jPushAlertModelOptional.get();
                jPushAlertModel.setContent(userMessageModel.getAppTitle());
                jPushAlertNewService.autoJPushAlertSend(jPushAlertModel);
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("jPush send fail! userMessageId:{0} content:{1}", userMessageModel.getId(), userMessageModel.getContent()));
        }
    }

    public void generateRegisterUserSuccessEvent(String loginName) {
        MessageModel registerUserSuccessMessage = messageMapper.findActiveByEventType(MessageEventType.REGISTER_USER_SUCCESS);
        //Title:5888元体验金已存入您的账户，请查收！
        //AppTitle:5888元体验金已存入您的账户，请查收！
        //Content:哇，您终于来啦！初次见面，岂能无礼？5888元体验金双手奉上，【立即体验】再拿588元红包和3%加息券！
        UserMessageModel registerUserMessageModel = new UserMessageModel(registerUserSuccessMessage.getId(), loginName, registerUserSuccessMessage.getTitle(), registerUserSuccessMessage.getAppTitle(), registerUserSuccessMessage.getTemplateTxt());
        userMessageMapper.create(registerUserMessageModel);
        sendJPushByUserMessageModel(registerUserMessageModel);

        UserModel userModel = userMapper.findByLoginName(loginName);
        if (!Strings.isNullOrEmpty(userModel.getReferrer())) {
            MessageModel recommendSuccessMessage = messageMapper.findActiveByEventType(MessageEventType.RECOMMEND_SUCCESS);
            //Title:您推荐的好友 {0} 已成功注册
            //AppTitle:您推荐的好友 {0} 已成功注册
            //Content:尊敬的用户，您推荐的好友 {0} 已成功注册，【邀请好友投资】您还能再拿1%现金奖励哦！
            String title = MessageFormat.format(recommendSuccessMessage.getTitle(), userModel.getMobile());
            String appTitle = MessageFormat.format(recommendSuccessMessage.getAppTitle(), userModel.getMobile());
            String content = MessageFormat.format(recommendSuccessMessage.getTemplate(), userModel.getMobile());

            //您推荐的好友{0}成功注册，若该好友进行投资，您即可获取现金奖励哦
            UserMessageModel userMessageModel = new UserMessageModel(recommendSuccessMessage.getId(), userModel.getReferrer(), title, appTitle, content);
            userMessageMapper.create(userMessageModel);
            sendJPushByUserMessageModel(userMessageModel);
        }
    }

    @Transactional
    public void generateRegisterAccountSuccessEvent(String loginName) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.REGISTER_ACCOUNT_SUCCESS);
        //Title:恭喜您认证成功
        //AppTitle:恭喜您认证成功
        //Content:尊敬的{0}女士/先生，恭喜您认证成功，您的支付密码已经由联动优势发送至注册手机号码中,马上【绑定银行卡】开启赚钱之旅吧！
        String title = messageModel.getTitle();
        String appTitle = messageModel.getAppTitle();
        String content = MessageFormat.format(messageModel.getTemplate(), userModel.getMobile());

        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, content);
        userMessageMapper.create(userMessageModel);
        sendJPushByUserMessageModel(userMessageModel);
    }

    @Transactional
    public void generateWithdrawSuccessOrApplicationSuccessEvent(long withdrawId) {
        Map<String, Object> withdraw = userMessageMetaMapper.findWithdrawById(withdrawId);
        if (withdraw == null) {
            return;
        }
        String status = (String) withdraw.get("status");
        if (Strings.isNullOrEmpty(status)) {
            return;
        }

        WithdrawStatus withdrawStatus = WithdrawStatus.valueOf(status);

        long amount = ((BigInteger) withdraw.get("amount")).longValue();
        String loginName = (String) withdraw.get("login_name");
        MessageModel messageModel = null;
        if (withdrawStatus.equals(SUCCESS)) {
            messageModel = messageMapper.findActiveByEventType(MessageEventType.WITHDRAW_SUCCESS);
            //Title:您的{0}元提现已到账,请查收
            //AppTitle:您的{0}元提现已到账,请查收
            //Content:尊敬的用户，您提交的{0}元提现申请已成功通过审核，请及时查收款项，感谢您选择拓天速贷。

        } else if (withdrawStatus.equals(APPLY_SUCCESS)) {
            messageModel = messageMapper.findActiveByEventType(MessageEventType.WITHDRAW_APPLICATION_SUCCESS);
            //Title:您的{0}元提现申请已提交成功
            //AppTitle:您的{0}元提现申请已提交成功
            //Content:尊敬的用户，您提交了{0}元提现申请，联动优势将会在1个工作日内进行审批，请耐心等待。
        }

        if (null == messageModel) {
            return;
        }

        String title = MessageFormat.format(messageModel.getTitle(), AmountConverter.convertCentToString(amount));
        String appTitle = MessageFormat.format(messageModel.getAppTitle(), AmountConverter.convertCentToString(amount));
        String content = MessageFormat.format(messageModel.getTemplate(), AmountConverter.convertCentToString(amount));
        long messageId = messageModel.getId();
        UserMessageModel userMessageModel = new UserMessageModel(messageId, loginName, title, appTitle, content);
        userMessageMapper.create(userMessageModel);
        sendJPushByUserMessageModel(userMessageModel);
    }

    @Transactional
    public void generateInvestSuccessEvent(long investId) {
        Map<String, Object> invest = userMessageMetaMapper.findInvestById(investId);
        long amount = ((BigInteger) invest.get("amount")).longValue();
        String loginName = (String) invest.get("login_name");

        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.INVEST_SUCCESS);
        //Title:恭喜您成功投资{0}元
        //AppTitle:恭喜您成功投资{0}元
        //Content:尊敬的用户，您已成功投资房产/车辆抵押借款{0}元，独乐不如众乐，马上【邀请好友投资】还能额外拿1%现金奖励哦！
        String title = MessageFormat.format(messageModel.getTitle(), AmountConverter.convertCentToString(amount));
        String appTitle = MessageFormat.format(messageModel.getAppTitle(), AmountConverter.convertCentToString(amount));
        String content = MessageFormat.format(messageModel.getTemplate(), AmountConverter.convertCentToString(amount));

        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, content);
        userMessageMapper.create(userMessageModel);
        sendJPushByUserMessageModel(userMessageModel);
    }

    @Transactional
    public void generateTransferSuccessEvent(long investId) {
        Map<String, Object> invest = userMessageMetaMapper.findTransferApplicationByInvestId(investId);
        String name = (String) invest.get("name");
        long amount = ((BigInteger) invest.get("transfer_amount")).longValue();
        String loginName = (String) invest.get("login_name");

        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.TRANSFER_SUCCESS);
        //Title:您发起的转让项目转让成功，{0}元已发放至您的账户！
        //AppTitle:您发起的转让项目转让成功，{0}元已发放至您的账户！
        //Content:尊敬的用户，您发起的转让项目{0}已经转让成功，资金已经到达您的账户，感谢您选择拓天速贷。
        String title = MessageFormat.format(messageModel.getTitle(), AmountConverter.convertCentToString(amount));
        String appTitle = MessageFormat.format(messageModel.getAppTitle(), AmountConverter.convertCentToString(amount));
        String content = MessageFormat.format(messageModel.getTemplate(), name);

        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, content);
        userMessageMapper.create(userMessageModel);
        sendJPushByUserMessageModel(userMessageModel);
    }

    public void generateTransferFailEvent(long transferApplicationId) {
        Map<String, Object> transferApplication = userMessageMetaMapper.findTransferApplicationById(transferApplicationId);
        String loginName = (String) transferApplication.get("login_name");
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.TRANSFER_FAIL);
        //Title:您提交的债权转让到期取消，请查看！
        //AppTitle:您提交的债权转让到期取消，请查看！
        //Content:尊敬的用户，我们遗憾地通知您，您发起的转让项目没有转让成功。如有疑问，请致电客服热线400-169-1188，感谢您选择拓天速贷。

        String title = messageModel.getTitle();
        String appTitle = messageModel.getAppTitle();
        String content = messageModel.getTemplate();

        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, content);
        userMessageMapper.create(userMessageModel);
        sendJPushByUserMessageModel(userMessageModel);
    }

    @Transactional
    public void generateLoanOutSuccessEvent(long loanId) {
        Map<String, Object> loan = userMessageMetaMapper.findLoanById(loanId);
        String loanName = (String) loan.get("name");
        double baseRate = (double) loan.get("baseRate");
        double activityRate = (double) loan.get("activityRate");
        double rate = baseRate + activityRate;

        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.LOAN_OUT_SUCCESS);
        //Title:您投资的{0}已经满额放款，预期年化收益{1}%
        //AppTitle:您投资的{0}已经满额放款，预期年化收益{1}%
        //Content:尊敬的用户，您投资的{0}项目已经满额放款，预期年化收益{1}%，快来查看收益吧。
        String title = MessageFormat.format(messageModel.getTitle(), loanName, rate * 100);
        String appTitle = MessageFormat.format(messageModel.getAppTitle(), loanName, rate * 100);
        String content = MessageFormat.format(messageModel.getTemplate(), loanName, rate * 100);

        Set<String> investorLoginNames = Sets.newHashSet(userMessageMetaMapper.findSuccessInvestorByLoanId(loanId));
        for (String investor : investorLoginNames) {
            UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), investor, title, appTitle, content);
            userMessageMapper.create(userMessageModel);
            sendJPushByUserMessageModel(userMessageModel);
        }
    }

    @Transactional
    public void generateRepaySuccessEvent(long loanRepayId) {
        Map<String, Object> loan = userMessageMetaMapper.findLoanByLoanRepayId(loanRepayId);
        Map<String, Object> loanRepay = userMessageMetaMapper.findLoanRepayById(loanRepayId);
        List<Map<String, Object>> invests = userMessageMetaMapper.findInvestsByLoanId(((BigInteger) loan.get("id")).longValue());
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.REPAY_SUCCESS);
        //Title:您投资的{0}已回款{1}元，请前往账户查收！
        //AppTitle:您投资的{0}已回款{1}元，请前往账户查收！
        //Content:尊敬的用户，您投资的{0}项目已回款，期待已久的收益已奔向您的账户，快来查看吧。

        for (Map<String, Object> invest : invests) {
            Map<String, Object> investRepay = userMessageMetaMapper.findInvestRepayByInvestIdAndPeriod(((BigInteger) invest.get("id")).longValue(), (int) loanRepay.get("period"));
            String title = MessageFormat.format(messageModel.getTitle(), loan.get("name"), AmountConverter.convertCentToString(((BigInteger) investRepay.get("amount")).longValue()));
            String appTitle = MessageFormat.format(messageModel.getAppTitle(), loan.get("name"), AmountConverter.convertCentToString(((BigInteger) investRepay.get("amount")).longValue()));
            String content = MessageFormat.format(messageModel.getTemplate(), loan.get("name"));

            UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), (String) invest.get("loginName"), title, appTitle, content);
            userMessageMapper.create(userMessageModel);
            sendJPushByUserMessageModel(userMessageModel);
        }
    }

    @Transactional
    public void generateAdvancedRepaySuccessEvent(long loanRepayId) {
        Map<String, Object> loan = userMessageMetaMapper.findLoanByLoanRepayId(loanRepayId);
        Map<String, Object> loanRepay = userMessageMetaMapper.findLoanRepayById(loanRepayId);
        List<Map<String, Object>> invests = userMessageMetaMapper.findInvestsByLoanId(((BigInteger) loan.get("id")).longValue());
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.ADVANCED_REPAY);
        //Title:您投资的{0}提前还款，{1}元已返还至您的账户！
        //AppTitle:您投资的{0}提前还款，{1}元已返还至您的账户！
        //Content:尊敬的用户，您在{0}投资的房产/车辆抵押借款因借款人放弃借款而提前终止，您的收益与本金已返还至您的账户，您可以【看看其他优质项目】

        for (Map<String, Object> invest : invests) {
            Map<String, Object> investRepay = userMessageMetaMapper.findInvestRepayByInvestIdAndPeriod(((BigInteger) invest.get("id")).longValue(), (int) loanRepay.get("period"));
            String title = MessageFormat.format(messageModel.getTitle(), loan.get("name"), AmountConverter.convertCentToString(((BigInteger) investRepay.get("amount")).longValue()));
            String appTitle = MessageFormat.format(messageModel.getAppTitle(), loan.get("name"), AmountConverter.convertCentToString(((BigInteger) investRepay.get("amount")).longValue()));
            String content = MessageFormat.format(messageModel.getTemplate(), loan.get("name"));

            UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), (String) invest.get("loginName"), title, appTitle, content);
            userMessageMapper.create(userMessageModel);
            sendJPushByUserMessageModel(userMessageModel);
        }
    }

    @Transactional
    public void generateRecommendAwardSuccessEvent(long loanId) {
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.RECOMMEND_AWARD_SUCCESS);
        //Title:{0}元推荐奖励已存入您的账户，请查收！
        //AppTitle:{0}元推荐奖励已存入您的账户，请查收！
        //Content:尊敬的用户，您推荐的好友{0}投资成功，您已获得{1}元现金奖励。

        List<Map<String, Object>> investReferrerRewards = userMessageMetaMapper.findInvestReferrerRewardByLoanId(loanId);

        for (Map<String, Object> investReferrerReward : investReferrerRewards) {
            long amount = ((BigInteger) investReferrerReward.get("amount")).longValue();

            String title = MessageFormat.format(messageModel.getTitle(), AmountConverter.convertCentToString(amount));
            String appTitle = MessageFormat.format(messageModel.getAppTitle(), AmountConverter.convertCentToString(amount));
            String content = MessageFormat.format(messageModel.getTemplate(), investReferrerReward.get("mobile"), AmountConverter.convertCentToString(amount));

            UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), (String) investReferrerReward.get("referrer"), title, appTitle, content);
            userMessageMapper.create(userMessageModel);
            sendJPushByUserMessageModel(userMessageModel);
        }
    }

    @Transactional
    public void generateCouponExpiredAlertEvent(String loginName) {
        long times = loginLogMapper.countSuccessTimesOnDate(loginName, new Date(), MessageFormat.format("login_log_{0}", new DateTime().toString("yyyyMM")));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String endTime = simpleDateFormat.format(DateTime.now().plusDays(5).withTimeAtStartOfDay().toDate());
        if (times == 1) {
            MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.COUPON_5DAYS_EXPIRED_ALERT);
            //Title:您有一张{0}即将失效
            //AppTitle: 您有一张{0}即将失效
            //Content:尊敬的用户，您有一张{0}即将失效(有效期至:{1})，请尽快使用！

            List<Map<String, Object>> userCouponModels = userMessageMetaMapper.findCouponWillExpire(loginName);
            for (Map<String, Object> userCoupon : userCouponModels) {
                String title;
                String appTitle;
                String content;
                String couponType = (String) userCoupon.get("type");
                switch (couponType) {
                    case "RED_ENVELOPE":
                    case "NEWBIE_COUPON":
                    case "INVEST_COUPON":
                        long amount = (long) userCoupon.get("amount");
                        title = MessageFormat.format(messageModel.getTitle(), AmountConverter.convertCentToString(amount) + "元" + COUPON_NAME_MAPPING.get(couponType));
                        appTitle = MessageFormat.format(messageModel.getAppTitle(), AmountConverter.convertCentToString(amount) + "元" + COUPON_NAME_MAPPING.get(couponType));
                        content = MessageFormat.format(messageModel.getTemplate(), AmountConverter.convertCentToString(amount) + "元" + COUPON_NAME_MAPPING.get(couponType), endTime);
                        break;
                    case "INTEREST_COUPON":
                        double rate = (double) userCoupon.get("rate");
                        title = MessageFormat.format(messageModel.getTitle(), new BigDecimal(rate).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%" + COUPON_NAME_MAPPING.get(couponType));
                        appTitle = MessageFormat.format(messageModel.getAppTitle(), new BigDecimal(rate).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%" + COUPON_NAME_MAPPING.get(couponType));
                        content = MessageFormat.format(messageModel.getAppTitle(), new BigDecimal(rate).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%" + COUPON_NAME_MAPPING.get(couponType), endTime);
                        break;
                    default:
                        title = MessageFormat.format(messageModel.getTitle(), COUPON_NAME_MAPPING.get(couponType));
                        appTitle = MessageFormat.format(messageModel.getAppTitle(), COUPON_NAME_MAPPING.get(couponType));
                        content = MessageFormat.format(messageModel.getTemplate(), COUPON_NAME_MAPPING.get(couponType), endTime);
                        break;
                }
                UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, content);
                userMessageMapper.create(userMessageModel);
                sendJPushByUserMessageModel(userMessageModel);
            }
        }
    }

    public void generateMembershipExpiredEvent(String loginName) {
        boolean isExisted = userMessageMetaMapper.isExpiredLevelFiveMembershipExisted(loginName);
        if (!isExisted) {
            return;
        }

        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.MEMBERSHIP_EXPIRED);
        //Title:您的V5会员已到期，请前去购买
        //AppTitle:您的V5会员已到期，请前去购买
        //Content:尊敬的用户，您的V5会员已到期，V5会员可享受服务费7折优惠，平台也将会在V5会员生日时送上神秘礼包哦。请及时续费以免耽误您获得投资奖励！
        String title = messageModel.getTitle();
        String appTitle = messageModel.getAppTitle();
        String content = messageModel.getTemplate();

        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, content);
        userMessageMapper.create(userMessageModel);
        sendJPushByUserMessageModel(userMessageModel);
    }

    public void generateMembershipPurchaseEvent(String loginName, int duration) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.MEMBERSHIP_BUY_SUCCESS);
        //Title:恭喜您已成功购买{0}个月V5会员！
        //AppTitle:恭喜您已成功购买{0}个月V5会员！
        //Content:尊敬的用户，恭喜您已成功购买V5会员，有效期至{0}，【马上投资】享受会员特权吧！
        String title = MessageFormat.format(messageModel.getTitle(), duration / 30);
        String appTitle = MessageFormat.format(messageModel.getAppTitle(), duration / 30);
        String content = MessageFormat.format(messageModel.getTemplate(), simpleDateFormat.format(DateTime.now().withTimeAtStartOfDay().plusDays(duration).toDate()));

        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, content);
        userMessageMapper.create(userMessageModel);
        sendJPushByUserMessageModel(userMessageModel);
    }

    public void generateBirthdayEvent() {
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.BIRTHDAY);
        //Title:拓天速贷为您送上生日祝福，请查收！
        //AppTitle:拓天速贷为您送上生日祝福，请查收！
        //Content:尊敬的{0}先生/女士，我猜今天是您的生日，拓天速贷在此送上真诚的祝福，生日当月投资即可享受收益翻倍哦！
        List<String> loginNames = userMessageMetaMapper.findBirthDayUsers();
        String title = messageModel.getTitle();
        String appTitle = messageModel.getAppTitle();
        loginNames.forEach(loginName -> {
            String userName = userMessageMetaMapper.findUserNameByLoginName(loginName);
            String content = MessageFormat.format(messageModel.getTemplate(), userName);

            UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, content);
            userMessageMapper.create(userMessageModel);
            sendJPushByUserMessageModel(userMessageModel);
        });
    }

    public void generateMembershipUpgradeEvent(String loginName, long membershipId) {
        Map<String, Object> membershipModel = userMessageMetaMapper.findMembershipById(membershipId);

        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.MEMBERSHIP_UPGRADE);
        //Title:恭喜您会员等级提升至V{0}
        //AppTitle:恭喜您会员等级提升至V{0}
        //Content:尊敬的用户，恭喜您会员等级提升至V{0}，拓天速贷为您准备了更多会员特权，快来查看吧。
        String title = MessageFormat.format(messageModel.getTitle(), membershipModel.get("level"));
        String appTitle = MessageFormat.format(messageModel.getAppTitle(), membershipModel.get("level"));
        String content = MessageFormat.format(messageModel.getTemplate(), membershipModel.get("level"));

        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, appTitle, content);
        userMessageMapper.create(userMessageModel);
        sendJPushByUserMessageModel(userMessageModel);
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
}
