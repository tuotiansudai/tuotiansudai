package com.tuotiansudai.message.util;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.mapper.UserMessageMapper;
import com.tuotiansudai.message.repository.model.MessageEventType;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.UserMessageModel;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoginLogService;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserMessageEventGenerator {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private InvestReferrerRewardMapper investReferrerRewardMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private LoginLogService loginLogService;

    @Transactional
    public void generateRegisterUserSuccessEvent(String loginName) {
        MessageModel registerUserSuccessMessage = messageMapper.findActiveByEventType(MessageEventType.REGISTER_USER_SUCCESS);
        //终于等到你，欢迎来到拓天速贷平台。
        UserMessageModel registerUserMessageModel = new UserMessageModel(registerUserSuccessMessage.getId(), loginName, registerUserSuccessMessage.getTitle(), null);
        userMessageMapper.create(registerUserMessageModel);

        UserModel userModel = userMapper.findByLoginName(loginName);
        if (!Strings.isNullOrEmpty(userModel.getReferrer())) {
            MessageModel recommendSuccessMessage = messageMapper.findActiveByEventType(MessageEventType.RECOMMEND_SUCCESS);
            //您推荐的好友 {0} 成功注册，若该好友进行投资，您即可获取现金奖励哦
            String titleTemplate = recommendSuccessMessage.getTitle();
            String title = MessageFormat.format(titleTemplate, loginName);
            UserMessageModel userMessageModel = new UserMessageModel(recommendSuccessMessage.getId(), userModel.getReferrer(), title, null);
            userMessageMapper.create(userMessageModel);
        }
    }

    @Transactional
    public void generateRegisterAccountSuccessEvent(String loginName) {
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.REGISTER_ACCOUNT_SUCCESS);
        //实名认证成功，您的支付密码已经由联动优势发送至注册手机号码，请牢记。
        String title = messageModel.getTitle();
        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, null);
        userMessageMapper.create(userMessageModel);
    }

    @Transactional
    public void generateRechargeSuccessEvent(String loginName, long amount) {
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.RECHARGE_SUCCESS);
        //充值成功：您已成功充值 {0} 元，及时<a href="/loan-list">投资赚取更多</a>哦。
        String titleTemplate = messageModel.getTitle();
        String title = MessageFormat.format(titleTemplate, AmountConverter.convertCentToString(amount));
        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, null);
        userMessageMapper.create(userMessageModel);
    }

    @Transactional
    public void generateWithdrawSuccessEvent(String loginName, long amount) {
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.WITHDRAW_SUCCESS);
        //提现成功：您已成功提现 {0} 元，选择拓天，共赢财富。
        String titleTemplate = messageModel.getTitle();
        String title = MessageFormat.format(titleTemplate, AmountConverter.convertCentToString(amount));
        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, null);
        userMessageMapper.create(userMessageModel);
    }

    @Transactional
    public void generateInvestSuccessEvent(String loginName, long loanId, long amount) {
        LoanModel loanModel = loanMapper.findById(loanId);
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.INVEST_SUCCESS);
        //投标成功：您在<a href="/loan/{0}">{1}</a>项目成功投资 {2} 元，不日即将放款。
        String titleTemplate = messageModel.getTitle();
        String title = MessageFormat.format(titleTemplate, String.valueOf(loanId), loanModel.getName(), AmountConverter.convertCentToString(amount));
        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, null);
        userMessageMapper.create(userMessageModel);
    }

    @Transactional
    public void generateTransferSuccessEvent(String loginName, long transferApplicationId) {
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.TRANSFER_SUCCESS);
        //您发起的转让项目<a href="/transfer/{0}">{1}</a>已经转让成功，资金已经到达您的账户。
        String titleTemplate = messageModel.getTitle();
        String title = MessageFormat.format(titleTemplate, String.valueOf(transferApplicationId), transferApplicationModel.getName());
        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, null);
        userMessageMapper.create(userMessageModel);
    }

    public void generateTransferFailEvent(String loginName, long transferApplicationId) {
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.TRANSFER_FAIL);
        //很遗憾，您发起的转让项目<a href="/transfer/{0}">{1}</a>没有转让成功。
        String titleTemplate = messageModel.getTitle();
        String title = MessageFormat.format(titleTemplate, String.valueOf(transferApplicationId), transferApplicationModel.getName());
        UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, null);
        userMessageMapper.create(userMessageModel);
    }

    @Transactional
    public void generateLoanOutSuccessEvent(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.LOAN_OUT_SUCCESS);
        //您投资的<a href="/loan/{0}">{1}</a>项目已经满额放款，快来<a href="/user-bill">查看收益</a>吧。
        String titleTemplate = messageModel.getTitle();
        String title = MessageFormat.format(titleTemplate, String.valueOf(loanId), loanModel.getName());
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanId);
        Set<String> investorLoginNames = Sets.newHashSet();
        for (InvestModel investModel : investModels) {
            if (!investorLoginNames.contains(investModel.getLoginName())) {
                investorLoginNames.add(investModel.getLoginName());
                UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), investModel.getLoginName(), title, null);
                userMessageMapper.create(userMessageModel);
            }
        }
    }

    @Transactional
    public void generateRepaySuccessEvent(long loanRepayId) {
        LoanRepayModel loanRepayModel = loanRepayMapper.findById(loanRepayId);
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.REPAY_SUCCESS);
        //您投资的<a href="/loan/{0}">{1}</a>项目还款啦，赶快<a href="/user-bill">查看收益</a>吧。
        String titleTemplate = messageModel.getTitle();
        String title = MessageFormat.format(titleTemplate, String.valueOf(loanModel.getId()), loanModel.getName());
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanModel.getId());
        Set<String> investorLoginNames = Sets.newHashSet();
        for (InvestModel investModel : investModels) {
            if (!investorLoginNames.contains(investModel.getLoginName())) {
                investorLoginNames.add(investModel.getLoginName());
                UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), investModel.getLoginName(), title, null);
                userMessageMapper.create(userMessageModel);
            }
        }
    }

    @Transactional
    public void generateRecommendAwardSuccessEvent(long loanId) {
        MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.RECOMMEND_AWARD_SUCCESS);
        //您推荐的好友 {0} 成功进行了投资，您获得了 {1} 元现金奖励，<a href="/user-bill">立即查看</a>。
        String titleTemplate = messageModel.getTitle();
        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanId);
        for (InvestModel successInvest : successInvests) {
            List<InvestReferrerRewardModel> investReferrerRewardModels = investReferrerRewardMapper.findByInvestId(successInvest.getId());
            for (InvestReferrerRewardModel investReferrerRewardModel : investReferrerRewardModels) {
                if (investReferrerRewardModel.getStatus() == ReferrerRewardStatus.SUCCESS) {
                    String title = MessageFormat.format(titleTemplate, successInvest.getLoginName(), AmountConverter.convertCentToString(investReferrerRewardModel.getAmount()));
                    UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), investReferrerRewardModel.getReferrerLoginName(), title, null);
                    userMessageMapper.create(userMessageModel);
                }
            }
        }
    }

    @Transactional
    public void generateCouponExpiredAlertEvent(String loginName) {
        long times = loginLogService.countSuccessTimesOnDate(loginName, new Date());
        if (times == 1) {
            MessageModel messageModel = messageMapper.findActiveByEventType(MessageEventType.COUPON_5DAYS_EXPIRED_ALERT);
            //您的{0}即将到期，使用可以获取额外收益哦，<a href="/my-treasure">立即使用</a>。
            String titleTemplate = messageModel.getTitle();
            List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, null);
            for (UserCouponModel userCouponModel : userCouponModels) {
                if (InvestStatus.SUCCESS != userCouponModel.getStatus() && Days.daysBetween(new DateTime(userCouponModel.getEndTime()), new DateTime()).getDays() == 5) {
                    String title;
                    CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                    switch (couponModel.getCouponType()) {
                        case RED_ENVELOPE:
                        case NEWBIE_COUPON:
                        case INVEST_COUPON:
                            title = MessageFormat.format(titleTemplate, AmountConverter.convertCentToString(couponModel.getAmount()) + "元" + couponModel.getCouponType().getName());
                            break;
                        case INTEREST_COUPON:
                            title = MessageFormat.format(titleTemplate, new BigDecimal(couponModel.getRate()).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).toString()  + "%" +  couponModel.getCouponType().getName());
                            break;
                        default:
                            title = MessageFormat.format(titleTemplate, couponModel.getCouponType().getName());
                            break;
                    }
                    UserMessageModel userMessageModel = new UserMessageModel(messageModel.getId(), loginName, title, null);
                    userMessageMapper.create(userMessageModel);
                }
            }
        }

    }
}
