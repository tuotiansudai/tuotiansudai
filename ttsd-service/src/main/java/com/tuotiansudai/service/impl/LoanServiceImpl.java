package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.service.SendCloudMailService;
import com.tuotiansudai.utils.*;

import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import java.util.*;

@Service
public class LoanServiceImpl implements LoanService {
    static Logger logger = Logger.getLogger(LoanServiceImpl.class);

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private PayWrapperClient payWrapperClient;
    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SendCloudMailService sendCloudMailService;
    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private InvestReferrerRewardMapper investReferrerRewardMapper;

    /**
     * @param loanTitleDto
     * @function 创建标题
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoanTitleModel createTitle(LoanTitleDto loanTitleDto) {
        LoanTitleModel loanTitleModel = new LoanTitleModel();
        long id = idGenerator.generate();
        loanTitleModel.setId(id);
        loanTitleModel.setTitle(loanTitleDto.getTitle());
        loanTitleModel.setType(LoanTitleType.NEW_TITLE_TYPE);
        loanTitleMapper.create(loanTitleModel);
        return loanTitleModel;
    }

    /**
     * @param loginName
     * @return
     * @function 获取成功注册过资金托管账户的用户登录名
     */
    @Override
    public List<String> getLoginNames(String loginName) {
        return accountMapper.findAllLoginNamesByLike(loginName);
    }

    /**
     * @return
     * @function 获取所有标题
     */
    public List<LoanTitleModel> findAllTitles() {
        return loanTitleMapper.findAll();
    }

    /**
     * @return List<LoanType>
     * @function 获取所有标的类型
     */
    @Override
    public List<LoanType> getLoanType() {
        List<LoanType> loanTypes = new ArrayList<LoanType>();
        for (LoanType loanType : LoanType.values()) {
            loanTypes.add(loanType);
        }
        return loanTypes;
    }

    /**
     * @return List<ActivityType>
     * @function 获取所有活动类型
     */
    @Override
    public List<ActivityType> getActivityType() {
        List<ActivityType> activityTypes = new ArrayList<ActivityType>();
        for (ActivityType activityType : ActivityType.values()) {
            activityTypes.add(activityType);
        }
        return activityTypes;
    }

    /**
     * @param loanDto
     * @return BaseDto<PayDataDto>
     * @function 创建标的
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> createLoan(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto();
        PayDataDto dataDto = new PayDataDto();
        long minInvestAmount = AmountUtil.convertStringToCent(loanDto.getMinInvestAmount());
        long maxInvestAmount = AmountUtil.convertStringToCent(loanDto.getMaxInvestAmount());
        long loanAmount = AmountUtil.convertStringToCent(loanDto.getLoanAmount());
        if (maxInvestAmount < minInvestAmount) {
            dataDto.setStatus(false);
            baseDto.setData(dataDto);
            return baseDto;
        }
        if (maxInvestAmount > loanAmount) {
            dataDto.setStatus(false);
            baseDto.setData(dataDto);
            return baseDto;
        }
        if (loanDto.getFundraisingEndTime().before(loanDto.getFundraisingStartTime())) {
            dataDto.setStatus(false);
            baseDto.setData(dataDto);
            return baseDto;
        }
        String loanUserId = getLoginName(loanDto.getLoanerLoginName());
        if (loanUserId == null) {
            dataDto.setStatus(false);
            baseDto.setData(dataDto);
            return baseDto;
        }
        String loanAgentId = getLoginName(loanDto.getAgentLoginName());
        if (loanAgentId == null) {
            dataDto.setStatus(false);
            baseDto.setData(dataDto);
            return baseDto;
        }
        long projectId = idGenerator.generate();/****标的号****/
        loanDto.setId(projectId);
        loanMapper.create(new LoanModel(loanDto));
        List<LoanTitleRelationModel> loanTitleRelationModelList = loanDto.getLoanTitles();
        if (loanTitleRelationModelList.size() > 0) {
            for (LoanTitleRelationModel loanTitleRelationModel : loanDto.getLoanTitles()) {
                loanTitleRelationModel.setId(idGenerator.generate());
                loanTitleRelationModel.setLoanId(projectId);
            }
            loanTitleRelationMapper.create(loanTitleRelationModelList);
        }
        dataDto.setStatus(true);
        baseDto.setData(dataDto);
        return baseDto;
    }

    private String getLoginName(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel != null) {
            return accountModel.getPayUserId();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> updateLoan(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = loanParamValidate(loanDto);
        PayDataDto payDataDto = new PayDataDto();
        if (!baseDto.getData().getStatus()) {
            return baseDto;
        }
        if (loanMapper.findById(loanDto.getId()) == null) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        if (LoanStatus.WAITING_VERIFY == loanDto.getLoanStatus() || LoanStatus.VERIFY_FAIL == loanDto.getLoanStatus()) {
            updateLoanAndLoanTitleRelation(loanDto);
            payDataDto.setStatus(true);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        if (LoanStatus.PREHEAT == loanDto.getLoanStatus()) {
            updateLoanAndLoanTitleRelation(loanDto);
            baseDto = payWrapperClient.createLoan(loanDto);
            if (baseDto.getData().getStatus()) {
                return payWrapperClient.updateLoan(loanDto);
            }
            return baseDto;
        }
        payDataDto.setStatus(false);
        baseDto.setData(payDataDto);
        return baseDto;
    }

    @Override
    public LoanModel findLoanById(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        List<LoanTitleRelationModel> loanTitleRelationModelList = loanTitleRelationMapper.findByLoanId(loanId);
        loanModel.setLoanTitles(loanTitleRelationModelList);
        return loanModel;
    }

    @Override
    public boolean loanIsExist(long loanId) {
        return findLoanById(loanId) != null;
    }

    private BaseDto<PayDataDto> loanParamValidate(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto();
        PayDataDto payDataDto = new PayDataDto();
        long minInvestAmount = AmountUtil.convertStringToCent(loanDto.getMinInvestAmount());
        long maxInvestAmount = AmountUtil.convertStringToCent(loanDto.getMaxInvestAmount());
        long loanAmount = AmountUtil.convertStringToCent(loanDto.getLoanAmount());
        if (maxInvestAmount < minInvestAmount) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        if (maxInvestAmount > loanAmount) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        if (loanDto.getFundraisingEndTime().before(loanDto.getFundraisingStartTime())) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        String loanUserId = getLoginName(loanDto.getLoanerLoginName());
        if (loanUserId == null) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        String loanAgentId = getLoginName(loanDto.getAgentLoginName());
        if (loanAgentId == null) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        payDataDto.setStatus(true);
        baseDto.setData(payDataDto);
        return baseDto;
    }

    private void updateLoanAndLoanTitleRelation(LoanDto loanDto) {
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(loanDto.getLoanStatus());
        loanMapper.update(loanModel);
        List<LoanTitleRelationModel> loanTitleRelationModelList = loanTitleRelationMapper.findByLoanId(loanDto.getId());
        if (!CollectionUtils.isEmpty(loanTitleRelationModelList)) {
            loanTitleRelationMapper.delete(loanDto.getId());
        }
        loanTitleRelationModelList = loanDto.getLoanTitles();
        if (!CollectionUtils.isEmpty(loanTitleRelationModelList)) {
            loanTitleRelationMapper.create(loanTitleRelationModelList);
        }
    }

    public BaseDto<LoanDto> getLoanDetail(long loanId) {
        BaseDto dto = new BaseDto();
        LoanDto loanDto = new LoanDto();
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            dto.setSuccess(true);
            loanDto.setStatus(false);
            return dto;
        }
        loanDto = convertModelToDto(loanModel);
        loanDto.setStatus(true);
        dto.setData(loanDto);
        return dto;
    }

    private LoanDto convertModelToDto(LoanModel loanModel) {
        String loginName = LoginUserInfo.getLoginName();

        LoanDto loanDto = new LoanDto();
        loanDto.setId(loanModel.getId());
        loanDto.setProjectName(loanModel.getName());
        loanDto.setAgentLoginName(loanModel.getAgentLoginName());
        loanDto.setLoanerLoginName(loanModel.getLoanerLoginName());
        loanDto.setPeriods(loanModel.getPeriods());
        loanDto.setDescriptionHtml(loanModel.getDescriptionHtml());
        loanDto.setDescriptionText(loanModel.getDescriptionText());
        loanDto.setLoanAmount("" + loanModel.getLoanAmount());
        loanDto.setInvestIncreasingAmount("" + loanModel.getInvestIncreasingAmount());
        loanDto.setActivityType(loanModel.getActivityType());
        loanDto.setActivityRate("" + loanModel.getActivityRate());
        loanDto.setBasicRate("" + loanModel.getBaseRate());
        loanDto.setLoanStatus(loanModel.getStatus());
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel != null) {
            loanDto.setBalance(accountModel.getBalance() / 100d);
        }
        long investedAmount = investMapper.sumSuccessInvestAmount(loanModel.getId());
        loanDto.setAmountNeedRaised(calculateAmountNeedRaised(investedAmount, loanModel.getLoanAmount()));
        loanDto.setRaiseCompletedRate(calculateRaiseCompletedRate(investedAmount, loanModel.getLoanAmount()));
        loanDto.setLoanTitles(loanTitleRelationMapper.findByLoanId(loanModel.getId()));
        return loanDto;
    }

    @Override
    public String getExpectedTotalIncome(long loanId, double investAmount) {

        return null;
    }

    @Override
    public BasePaginationDto<InvestPaginationDataDto> getInvests(long loanId, int index, int pageSize) {
        if (index <= 0) {
            index = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        int totalCount = investMapper.getTotalCount(loanId, InvestStatus.SUCCESS);
        List<InvestModel> investModels = investMapper.getInvests(loanId, (index - 1) * pageSize, pageSize, InvestStatus.SUCCESS);
        List<InvestPaginationDataDto> investRecordDtos = convertInvestModelToDto(investModels);
        BasePaginationDto dto = new BasePaginationDto(index, pageSize, totalCount);
        dto.setRecordDtoList(investRecordDtos);
        dto.setStatus(true);
        return dto;
    }

    private List<InvestPaginationDataDto> convertInvestModelToDto(List<InvestModel> investModels) {
        List<InvestPaginationDataDto> investRecordDtos = new ArrayList<>();
        InvestPaginationDataDto investRecordDto = null;
        for (InvestModel investModel : investModels) {
            investRecordDto = new InvestPaginationDataDto();
            investRecordDto.setLoginName(investModel.getLoginName());
            investRecordDto.setAmount(investModel.getAmount() / 100d);
            investRecordDto.setSource(investModel.getSource());
            //TODO:预期利息
            investRecordDto.setExpectedRate(1.0);
            investRecordDto.setCreatedTime(investModel.getCreatedTime());

            investRecordDtos.add(investRecordDto);
        }
        return investRecordDtos;
    }


    private String calculatorPreheatSeconds(Date fundraisingStartTime) {
        if (fundraisingStartTime == null) {
            return "0";
        }
        Long time = (fundraisingStartTime.getTime() - System
                .currentTimeMillis()) / 1000;
        if (time < 0) {
            return "0";
        }
        return time.toString();

    }

    private double calculateAmountNeedRaised(long amountNeedRaised, long loanAmount) {
        BigDecimal amountNeedRaisedBig = new BigDecimal(amountNeedRaised);
        BigDecimal loanAmountBig = new BigDecimal(loanAmount);
        double amountNeedRaisedDouble = loanAmountBig.subtract(amountNeedRaisedBig)
                .divide(new BigDecimal(100d)).doubleValue();
        return amountNeedRaisedDouble;
    }

    private double calculateRaiseCompletedRate(long investedAmount, long loanAmount) {
        BigDecimal investedAmountBig = new BigDecimal(investedAmount);
        BigDecimal loanAmountBig = new BigDecimal(loanAmount);
        return investedAmountBig.divide(loanAmountBig).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recommendedIncome(LoanModel loanModel) {
        long loanId = loanModel.getId();
        logger.debug("begin referrer reward after make loan " + loanId);

        List<InvestModel> investModels = investMapper.getSuccessInvests(loanId);

        for (InvestModel invest : investModels) {
            logger.debug("find invest " + invest.getId());
            List<ReferrerRelationModel> referrerRelationList = referrerRelationMapper.findByLoginName(invest.getLoginName());
            for (ReferrerRelationModel referrerRelationModel : referrerRelationList) {
                logger.debug("referrer is" + referrerRelationModel.getReferrerLoginName());
                List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(referrerRelationModel.getReferrerLoginName());
                Role role = JudgeRole(userRoleModels);
                AccountModel accountModel = accountMapper.findByLoginName(referrerRelationModel.getReferrerLoginName());
                String payUserId = accountModel == null ? "" : accountModel.getPayUserId();
                long id = idGenerator.generate();
                ReferrerRewardStatus status = ReferrerRewardStatus.FAIL;
                String bonus = calculateBonus(invest.getAmount(), referrerRelationModel, loanModel, payUserId, role);
                if (Double.valueOf(bonus) == -1) {
                    logger.debug("role is" + role + ": level is " + referrerRelationModel.getLevel() + ",该层级不存在对于奖励比例!");
                    continue;
                }
                logger.debug("payUserId is" + payUserId + ": bonus is " + bonus);
                if ((role.equals(Role.INVESTOR) || role.equals(Role.MERCHANDISER)) && !"".equals(payUserId) && Double.valueOf(bonus) > 0.00) {
                    ReferrerRewardDto referrerRewardDto = new ReferrerRewardDto(payUserId, bonus, referrerRelationModel.getReferrerLoginName(),id);
                    try {
                        BaseDto<PayDataDto> baseDto = payWrapperClient.referrerReward(referrerRewardDto);
                        if (baseDto.getData().getStatus()) {
                            if ("0000".equals(baseDto.getData().getCode())) {
                                status = ReferrerRewardStatus.SUCCESS;
                            }
                        } else {
                            logger.debug("投资" + invest.getId() + ",推荐人" + referrerRelationModel.getReferrerLoginName() + "奖励失败！原因:" + baseDto.getData().getMessage());
                        }

                    }catch (Exception e){
                        logger.debug(e.getLocalizedMessage(),e);
                    }

                }

                if (role.equals(Role.USER)) {
                    logger.debug("投资" + invest.getId() + ",推荐人" + referrerRelationModel.getReferrerLoginName() + ReferrerRewardMessageTemplate.NOT_BIND_CARD.getDescription());
                }
                createInvestReferrerReward(invest, bonus, referrerRelationModel, role, id, status);

            }

        }
    }

    @Transactional(rollbackFor = Exception.class)
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
                BigDecimal rewardRateBig = new BigDecimal(investorMap.get(referrerRelationModel.getLevel())).divide(big100).setScale(6,BigDecimal.ROUND_HALF_UP);
                BigDecimal amountBig = new BigDecimal(amount / 100d);
                BigDecimal periodsBig = new BigDecimal(periods);
                BigDecimal daysOrMonthByYearBig = new BigDecimal(daysOrMonthByYear);

                bonus = amountBig.multiply(rewardRateBig).multiply(periodsBig)
                        .divide(daysOrMonthByYearBig,2).doubleValue();
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


    public void notifyInvestorsLoanOutSuccessfulByEmail(LoanModel loan) {

        List<InvestModel> investModels = investMapper.getSuccessInvests(loan.getId());
        logger.debug(MessageFormat.format("标的: {0} 放款邮件通知", loan.getId()));
        for (InvestModel investModel : investModels) {
            Map<String, String> emailParameters = Maps.newHashMap(new ImmutableMap.Builder<String, String>()
                    .put("loanName", loan.getName())
                    .put("money", String.valueOf(investModel.getAmount() / 100d))
                    .build());
            UserModel userModel = userMapper.findByLoginName(investModel.getLoginName());
            if (userModel != null && StringUtils.isNotEmpty(userModel.getEmail())) {
                sendCloudMailService.sendMailByLoanOut(userModel.getEmail(), emailParameters);
            }


        }
    }
}
