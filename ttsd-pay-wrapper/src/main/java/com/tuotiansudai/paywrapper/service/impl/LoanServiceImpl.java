package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestSmsNotifyDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.ReferrerRewardDto;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.MerBindProjectMapper;
import com.tuotiansudai.paywrapper.repository.mapper.MerUpdateProjectMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerBindProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerUpdateProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerBindProjectResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerUpdateProjectResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.*;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.DateUtil;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LoanServiceImpl implements LoanService {
    static Logger logger = Logger.getLogger(RegisterServiceImpl.class);
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private PaySyncClient paySyncClient;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserBillService userBillService;
    @Autowired
    private SmsWrapperClient smsWrapperClient;
    @Autowired
    private RepayService repayService;
    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private InvestReferrerRewardMapper investReferrerRewardMapper;
    @Autowired
    private ReferrerRewardService referrerRewardService;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private SendCloudMailService sendCloudMailService;


    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> createLoan(long loanId) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        LoanModel loanModel = loanMapper.findById(loanId);
        String loanerId = accountMapper.findByLoginName(loanModel.getLoanerLoginName()).getPayUserId();
        MerBindProjectRequestModel merBindProjectRequestModel = new MerBindProjectRequestModel(
                String.valueOf(loanModel.getId()),
                loanerId,
                String.valueOf(loanModel.getLoanAmount()),
                loanModel.getName()
        );
        try {
            MerBindProjectResponseModel responseModel = paySyncClient.send(MerBindProjectMapper.class,
                    merBindProjectRequestModel,
                    MerBindProjectResponseModel.class);
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            payDataDto.setStatus(false);
            logger.error(e.getLocalizedMessage(), e);
        }
        baseDto.setData(payDataDto);
        return baseDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> updateLoanStatus(long loanId, LoanStatus loanStatus) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        LoanModel loanModel = loanMapper.findById(loanId);
        MerUpdateProjectRequestModel merUpdateProjectRequestModel = new MerUpdateProjectRequestModel(
                loanModel.getLoanAmount(),
                loanModel.getId(),
                loanModel.getName(),
                loanStatus.getCode(),
                new SimpleDateFormat("yyyyMMdd").format(loanModel.getFundraisingEndTime())
        );
        try {
            MerUpdateProjectResponseModel responseModel = paySyncClient.send(MerUpdateProjectMapper.class,
                    merUpdateProjectRequestModel,
                    MerUpdateProjectResponseModel.class);
            if (responseModel.isSuccess()) {
                loanMapper.updateStatus(loanId, loanStatus);
            }
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            payDataDto.setStatus(false);
            logger.error(e.getLocalizedMessage(), e);
        }
        baseDto.setData(payDataDto);
        return baseDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> loanOut(long loanId) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);

        try {
            ProjectTransferResponseModel umPayReturn = doLoanOut(loanId);
            payDataDto.setStatus(umPayReturn.isSuccess());
            payDataDto.setCode(umPayReturn.getRetCode());
            payDataDto.setMessage(umPayReturn.getRetMsg());
        } catch (PayException e) {
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getLocalizedMessage());
            logger.error(e.getLocalizedMessage(), e);
        }
        return baseDto;
    }

    private ProjectTransferResponseModel doLoanOut(long loanId) throws PayException {
        // 预处理等待状态的投资记录，检查是否存在等待状态的投资记录
        preprocessWaitingInvest(loanId);

        // 查找借款人
        LoanModel loan = loanMapper.findById(loanId);
        if (loan == null) {
            throw new PayException("loan is not exists [" + loanId + "]");
        }
        String loanerId = accountMapper.findByLoginName(loan.getLoanerLoginName()).getPayUserId();


        // 查找所有投资成功的记录
        List<InvestModel> successInvestList = investMapper.findSuccessInvests(loanId);
        logger.debug("标的放款：查找到" + successInvestList.size() + "条成功的投资，标的ID:" + loanId);

        // 计算投资总金额
        long investAmountTotal = computeInvestAmountTotal(successInvestList);
        if (investAmountTotal <= 0) {
            throw new PayException("amount should great than 0");
        }

        logger.debug("标的放款：发起联动优势放款请求，标的ID:" + loanId + "，借款人:" + loanerId + "，放款金额:" + investAmountTotal);
        ProjectTransferResponseModel resp = doUmpayRequest(loanId, loanerId, investAmountTotal);

        if (resp.isSuccess()) {
            //TODO : 如果下面这些方法有任何一个出现异常，如何记录？
            logger.debug("标的放款：更新标的状态，标的ID:" + loanId);
            processLoanStatusForLoanOut(loan);

            logger.debug("标的放款：处理该标的的所有投资的账务信息，标的ID:" + loanId);
            processInvestForLoanOut(successInvestList);

            logger.debug("标的放款：把借款转给借款人账户，标的ID:" + loanId);
            processLoanAccountForLoanOut(loan, investAmountTotal);

            logger.debug("标的放款：生成还款计划，标的ID:" + loanId);
            repayService.generateInvestRepay(loan, successInvestList);

            logger.debug("标的放款：处理推荐人奖励，标的ID:" + loanId);
            recommendedIncome(loan, successInvestList);

            logger.debug("标的放款：处理短信和邮件通知，标的ID:" + loanId);
            processNotifyForLoanOut(loanId);
        }
        return resp;
    }

    private void preprocessWaitingInvest(long loanId) throws PayException {
        // 获取联动优势投资订单的有效时间点（在此时间之前的waiting记录将被清理，如存在在此时间之后的waiting记录，则暂时不允许放款）
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, -UmpayConstants.TIMEOUT_IN_SECOND_PROJECT_TRANSFER);
        Date validInvestTime = cal.getTime();

        // 检查是否存在未处理完成的投资记录
        int waitingInvestCount = investMapper.findWaitingInvestCountAfter(loanId, validInvestTime);
        if (waitingInvestCount > 0) {
            throw new PayException("exist waiting invest on loan[" + loanId + "]");
        }

        // 将已失效的投资记录状态置为失败
        investMapper.cleanWaitingInvestBefore(loanId, validInvestTime);
    }

    private ProjectTransferResponseModel doUmpayRequest(long loanId, String umpayUserId, long amount) throws PayException {
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newLoanOutRequest(
                String.valueOf(loanId), String.valueOf(loanId), umpayUserId, String.valueOf(amount));
        ProjectTransferResponseModel responseModel = paySyncClient.send(
                ProjectTransferMapper.class,
                requestModel,
                ProjectTransferResponseModel.class);
        return responseModel;
    }

    private long computeInvestAmountTotal(List<InvestModel> investList) {
        long amount = 0L;
        for (InvestModel investModel : investList) {
            amount += investModel.getAmount();
        }
        return amount;
    }

    private void processInvestForLoanOut(List<InvestModel> investList) {
        if (investList == null) {
            return;
        }
        for (InvestModel invest : investList) {
            try {
                userBillService.transferOutFreeze(invest.getLoginName(),
                        invest.getId(), invest.getAmount(), UserBillBusinessType.LOAN_SUCCESS);
            } catch (Exception e) {
                logger.error("transferOutFreeze Fail while loan out, invest [" + invest.getId() + "]", e);
                continue;
            }
        }
    }

    // 把借款转给借款人账户
    private void processLoanAccountForLoanOut(LoanModel loan, long amount) {
        try {
            long orderId = loan.getId();
            userBillService.transferInBalance(loan.getLoanerLoginName(),
                    orderId, amount, UserBillBusinessType.LOAN_SUCCESS);
        } catch (Exception e) {
            logger.error("transferInBalance Fail while loan out, loan[" + loan.getId() + "]", e);
        }
    }

    private void recommendedIncome(LoanModel loanModel, List<InvestModel> investModels) {
        long loanId = loanModel.getId();
        logger.debug("begin referrer reward after make loan " + loanId);

        for (InvestModel invest : investModels) {
            logger.debug("find invest " + invest.getId());
            List<ReferrerRelationModel> referrerRelationList = referrerRelationMapper.findByLoginName(invest.getLoginName());
            for (ReferrerRelationModel referrerRelationModel : referrerRelationList) {
                logger.debug("referrer is" + referrerRelationModel.getReferrerLoginName());
                List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(referrerRelationModel.getReferrerLoginName());
                Role role = JudgeRole(userRoleModels);
                AccountModel accountModel = accountMapper.findByLoginName(referrerRelationModel.getReferrerLoginName());
                String payUserId = accountModel == null ? "" : accountModel.getPayUserId();
                long orderId = idGenerator.generate();
                ReferrerRewardStatus status = ReferrerRewardStatus.FAIL;
                String bonus = calculateBonus(invest.getAmount(), referrerRelationModel, loanModel, payUserId, role);
                if (Double.valueOf(bonus) == -1) {
                    logger.debug("role is" + role + ": level is " + referrerRelationModel.getLevel() + ",该层级不存在对于奖励比例!");
                    continue;
                }
                logger.debug("payUserId is" + payUserId + ": bonus is " + bonus);
                if ((role.equals(Role.INVESTOR) || role.equals(Role.MERCHANDISER)) && !"".equals(payUserId) && Double.valueOf(bonus) > 0.00) {
                    ReferrerRewardDto referrerRewardDto = new ReferrerRewardDto(payUserId, bonus, referrerRelationModel.getReferrerLoginName(), orderId);
                    try {
                        BaseDto<PayDataDto> baseDto = referrerRewardService.getReferrerReward(referrerRewardDto);
                        if (baseDto.getData().getStatus()) {
                            if ("0000".equals(baseDto.getData().getCode())) {
                                status = ReferrerRewardStatus.SUCCESS;
                            }
                        } else {
                            logger.debug("投资" + invest.getId() + ",推荐人" + referrerRelationModel.getReferrerLoginName() + "奖励失败！原因:" + baseDto.getData().getMessage());
                        }

                    } catch (Exception e) {
                        logger.error(e.getLocalizedMessage(), e);
                    }

                }

                if (role.equals(Role.USER)) {
                    logger.debug("投资" + invest.getId() + ",推荐人" + referrerRelationModel.getReferrerLoginName() + ReferrerRewardMessageTemplate.NOT_BIND_CARD.getDescription());
                }
                createInvestReferrerReward(invest, bonus, referrerRelationModel, role, orderId, status);
            }
        }
    }

    private Role JudgeRole(List<UserRoleModel> userRoleModels) {
        List<String> userRoles = new ArrayList<>();
        for (UserRoleModel userRoleModel : userRoleModels) {
            userRoles.add(userRoleModel.getRole().name());
        }
        if (userRoles.contains(Role.MERCHANDISER.name())) {
            return Role.MERCHANDISER;
        } else if (userRoles.contains(Role.INVESTOR.name()) && !userRoles.contains(Role.MERCHANDISER.name())) {
            return Role.INVESTOR;
        } else {
            return Role.USER;
        }
    }

    private String calculateBonus(long amount, ReferrerRelationModel referrerRelationModel,
                                  LoanModel loanModel, String payUserId, Role role) {
        double bonus = 0.00;
        DecimalFormat df = new DecimalFormat("######0.00");
        BigDecimal big100 = new BigDecimal(100);
        String repayTimeUnit = loanModel.getType().getRepayTimeUnit();
        long periods = loanModel.getPeriods();
        //TODO:从数据库中获取奖励比例
        Map<Integer, Double> merchandiserMap = new HashMap<>();
        merchandiserMap.put(1, 0.4);
        merchandiserMap.put(2, 0.3);
        merchandiserMap.put(3, 0.2);
        merchandiserMap.put(4, 0.1);

        Map<Integer, Double> investorMap = new HashMap<>();
        investorMap.put(1, 0.4);
        investorMap.put(2, 0.3);
        int daysOrMonthByYear = 1;
        if ("month".equals(repayTimeUnit)) {
            daysOrMonthByYear = 12;
        } else if ("day".equals(repayTimeUnit)) {
            daysOrMonthByYear = DateUtil.judgeYear(new Date());
        }
        if (StringUtils.isEmpty(payUserId) || Role.INVESTOR.equals(role)) {
            if (investorMap.get(referrerRelationModel.getLevel()) != null) {
                BigDecimal rewardRateBig = new BigDecimal(investorMap.get(referrerRelationModel.getLevel())).divide(big100).setScale(6, BigDecimal.ROUND_HALF_UP);
                BigDecimal amountBig = new BigDecimal(amount / 100d);
                BigDecimal periodsBig = new BigDecimal(periods);
                BigDecimal daysOrMonthByYearBig = new BigDecimal(daysOrMonthByYear);

                bonus = amountBig.multiply(rewardRateBig).multiply(periodsBig)
                        .divide(daysOrMonthByYearBig, 2).doubleValue();
            } else {
                bonus = -1;
            }

        } else {
            if (merchandiserMap.get(referrerRelationModel.getLevel()) != null) {
                BigDecimal rewardRateBig = new BigDecimal(investorMap.get(referrerRelationModel.getLevel())).divide(big100);
                BigDecimal amountBig = new BigDecimal(amount / 100d);
                BigDecimal periodsBig = new BigDecimal(periods);
                BigDecimal daysOrMonthByYearBig = new BigDecimal(daysOrMonthByYear);

                bonus = amountBig.multiply(rewardRateBig).multiply(periodsBig)
                        .divide(daysOrMonthByYearBig).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            } else {
                bonus = -1;
            }

        }
        return df.format(bonus);
    }

    private void createInvestReferrerReward(InvestModel investModel, String bonus, ReferrerRelationModel referrerRelationModel, Role role, long id, ReferrerRewardStatus status) {
        InvestReferrerRewardModel investReferrerRewardModel = new InvestReferrerRewardModel();
        investReferrerRewardModel.setId(id);
        investReferrerRewardModel.setInvestId(investModel.getId());
        investReferrerRewardModel.setReferrerLoginName(referrerRelationModel.getReferrerLoginName());
        long bonusCent = AmountUtil.convertStringToCent(bonus);
        investReferrerRewardModel.setBonus(bonusCent);
        investReferrerRewardModel.setRoleName(role);
        investReferrerRewardModel.setStatus(status);
        investReferrerRewardModel.setTime(new Date());

        investReferrerRewardMapper.create(investReferrerRewardModel);
    }

    private void processNotifyForLoanOut(long loanId) {
        List<InvestNotifyInfo> notifyInfos = investMapper.findSuccessInvestMobileEmailAndAmount(loanId);
        logger.debug(MessageFormat.format("标的: {0} 放款短信通知", loanId));
        notifyInvestorsLoanOutSuccessfulBySMS(notifyInfos);
        logger.debug(MessageFormat.format("标的: {0} 放款邮件通知", loanId));
        notifyInvestorsLoanOutSuccessfulByEmail(notifyInfos);
    }

    private void notifyInvestorsLoanOutSuccessfulBySMS(List<InvestNotifyInfo> notifyInfos) {
        for (InvestNotifyInfo notifyInfo : notifyInfos) {
            InvestSmsNotifyDto dto = new InvestSmsNotifyDto(notifyInfo);
            smsWrapperClient.sendInvestNotify(dto);
        }
    }

    private void notifyInvestorsLoanOutSuccessfulByEmail(List<InvestNotifyInfo> notifyInfos) {
        for (InvestNotifyInfo notifyInfo : notifyInfos) {
            Map<String, String> emailParameters = Maps.newHashMap(new ImmutableMap.Builder<String, String>()
                    .put("loanName", notifyInfo.getLoanName())
                    .put("money", AmountUtil.convertCentToString(notifyInfo.getAmount()))
                    .build());
            String userEmail = notifyInfo.getEmail();
            System.out.println(userEmail);
            if (StringUtils.isNotEmpty(userEmail)) {
                sendCloudMailService.sendMailByLoanOut(userEmail, emailParameters);
            }
        }
    }

    private void processLoanStatusForLoanOut(LoanModel loan) {
        // 修改当前传入的 loan 对象，以便其它方法处理 loan 的数据
        loan.setRecheckTime(new Date());
        loan.setStatus(LoanStatus.REPAYING);

        // 构造新的 loan 对象，只应修改 recheckTime 和 status
        LoanModel loan4update = new LoanModel();
        loan4update.setId(loan.getId());
        loan4update.setRecheckTime(loan.getRecheckTime());
        loan4update.setStatus(loan.getStatus());
        loanMapper.update(loan4update);
    }
}
