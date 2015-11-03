package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.BaseException;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    static Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private InvestService investService;

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
    @Override
    public List<LoanTitleModel> findAllTitles() {
        return loanTitleMapper.findAll();
    }

    /**
     * @param loanDto
     * @return BaseDto<PayDataDto>
     * @function 创建标的
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> createLoan(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        long minInvestAmount = AmountUtil.convertStringToCent(loanDto.getMinInvestAmount());
        long maxInvestAmount = AmountUtil.convertStringToCent(loanDto.getMaxInvestAmount());
        long loanAmount = AmountUtil.convertStringToCent(loanDto.getLoanAmount());
        String loanAgentId = getLoginName(loanDto.getAgentLoginName());
        if (loanAgentId == null) {
            dataDto.setStatus(false);
            dataDto.setMessage("代理用户不存在");
            baseDto.setData(dataDto);
            return baseDto;
        } else {
            List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginNameAndRole(loanDto.getAgentLoginName(), Role.LOANER.name());
            if (CollectionUtils.isEmpty(userRoleModels)) {
                dataDto.setStatus(false);
                dataDto.setMessage("代理用户不具有借款人角色");
                baseDto.setData(dataDto);
                return baseDto;
            }
        }
        String loanUserId = getLoginName(loanDto.getLoanerLoginName());
        if (loanUserId == null) {
            dataDto.setStatus(false);
            dataDto.setMessage("借款用户不存在");
            baseDto.setData(dataDto);
            return baseDto;
        } else {
            List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginNameAndRole(loanDto.getLoanerLoginName(), Role.LOANER.name());
            if (CollectionUtils.isEmpty(userRoleModels)) {
                dataDto.setStatus(false);
                dataDto.setMessage("借款用户不具有借款人角色");
                baseDto.setData(dataDto);
                return baseDto;
            }
        }
        if(loanDto.getPeriods() <= 0){
            dataDto.setStatus(false);
            dataDto.setMessage("借款期限最小为1");
            baseDto.setData(dataDto);
            return baseDto;
        }
        if (loanAmount <= 0) {
            dataDto.setStatus(false);
            dataDto.setMessage("预计出借金额应大于0");
            baseDto.setData(dataDto);
            return baseDto;
        }
        if (minInvestAmount <= 0) {
            dataDto.setStatus(false);
            dataDto.setMessage("最小投资金额应大于0");
            baseDto.setData(dataDto);
            return baseDto;
        }
        if (maxInvestAmount < minInvestAmount) {
            dataDto.setStatus(false);
            dataDto.setMessage("最小投资金额不得大于最大投资金额");
            baseDto.setData(dataDto);
            return baseDto;
        }
        if (maxInvestAmount > loanAmount) {
            dataDto.setStatus(false);
            dataDto.setMessage("最大投资金额不得大于预计出借金额");
            baseDto.setData(dataDto);
            return baseDto;
        }
        if (loanDto.getFundraisingEndTime().before(loanDto.getFundraisingStartTime())) {
            dataDto.setStatus(false);
            dataDto.setMessage("筹款启动时间不得晚于筹款截止时间");
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
    public BaseDto<LoanDto> getLoanDetail(long loanId) {
        BaseDto<LoanDto> dto = new BaseDto<>();
        LoanDto loanDto = new LoanDto();
        dto.setData(loanDto);

        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            return dto;
        }

        loanDto = convertModelToDto(loanModel);
        loanDto.setStatus(true);
        dto.setData(loanDto);
        return dto;
    }

    private LoanDto convertModelToDto(LoanModel loanModel) {
        String loginName = LoginUserInfo.getLoginName();
        DecimalFormat decimalFormat = new DecimalFormat("######0.00");
        LoanDto loanDto = new LoanDto();
        loanDto.setId(loanModel.getId());
        loanDto.setProjectName(loanModel.getName());
        loanDto.setAgentLoginName(loanModel.getAgentLoginName());
        loanDto.setLoanerLoginName(loanModel.getLoanerLoginName());
        loanDto.setPeriods(loanModel.getPeriods());
        loanDto.setDescriptionHtml(loanModel.getDescriptionHtml());
        loanDto.setDescriptionText(loanModel.getDescriptionText());
        loanDto.setLoanAmount(AmountUtil.convertCentToString(loanModel.getLoanAmount() / 10000));
        loanDto.setInvestIncreasingAmount(AmountUtil.convertCentToString(loanModel.getInvestIncreasingAmount()));
        loanDto.setMinInvestAmount(AmountUtil.convertCentToString(loanModel.getMinInvestAmount()));
        loanDto.setActivityType(loanModel.getActivityType());
        loanDto.setActivityRate(decimalFormat.format(loanModel.getActivityRate()));
        loanDto.setBasicRate(decimalFormat.format(loanModel.getBaseRate() * 100));
        loanDto.setLoanStatus(loanModel.getStatus());
        loanDto.setType(loanModel.getType());
        loanDto.setMaxInvestAmount(AmountUtil.convertCentToString(loanModel.getMaxInvestAmount()));
        long investedAmount = investMapper.sumSuccessInvestAmount(loanModel.getId());
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel != null) {
            long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmountByLoginName(loanModel.getId(),loginName);
            loanDto.setBalance(accountModel.getBalance()/100d);
            loanDto.setMaxAvailableInvestAmount(AmountUtil.convertCentToString(calculateMaxAvailableInvestAmount(
                    accountModel.getBalance(), loanModel.getLoanAmount() - investedAmount,
                    loanModel.getMinInvestAmount(), loanModel.getInvestIncreasingAmount(),
                    loanModel.getMaxInvestAmount(), sumSuccessInvestAmount)));
        }

        loanDto.setAmountNeedRaised(calculateAmountNeedRaised(investedAmount, loanModel.getLoanAmount()));
        loanDto.setRaiseCompletedRate(calculateRaiseCompletedRate(investedAmount, loanModel.getLoanAmount()));
        loanDto.setLoanTitles(loanTitleRelationMapper.findByLoanId(loanModel.getId()));
        loanDto.setLoanTitleDto(loanTitleMapper.findAll());
        loanDto.setPreheatSeconds(calculatorPreheatSeconds(loanModel.getFundraisingStartTime()));
        loanDto.setFundraisingStartTime(loanModel.getFundraisingStartTime());
        loanDto.setBaseDto(getInvests(loanModel.getId(), 1, 10));

        return loanDto;
    }

    private long calculateMaxAvailableInvestAmount(long balance,long amountNeedRaised,long minInvestAmount,
                                                   long investIncreasingAmount,long maxInvestAmount,long userInvestedAmount){
        long maxAvailableInvestAmount = NumberUtils.min(balance,amountNeedRaised,maxInvestAmount - userInvestedAmount);

        if(maxAvailableInvestAmount >= minInvestAmount){
            maxAvailableInvestAmount = maxAvailableInvestAmount - (maxAvailableInvestAmount - minInvestAmount)%investIncreasingAmount;
        }else{
            maxAvailableInvestAmount = 0L;
        }
        return maxAvailableInvestAmount;

    }

    private long calculatorPreheatSeconds(Date fundraisingStartTime) {
        if (fundraisingStartTime == null) {
            return 0l;
        }
        long time = (fundraisingStartTime.getTime() - System
                .currentTimeMillis()) / 1000;
        if (time < 0) {
            return 0l;
        }
        return time;

    }

    private double calculateAmountNeedRaised(long investedAmount, long loanAmount) {
        BigDecimal investedAmountBig = new BigDecimal(investedAmount);
        BigDecimal loanAmountBig = new BigDecimal(loanAmount);
        return loanAmountBig.subtract(investedAmountBig)
                .divide(new BigDecimal(100D), 2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    private double calculateRaiseCompletedRate(long investedAmount, long loanAmount) {
        BigDecimal investedAmountBig = new BigDecimal(investedAmount);
        BigDecimal loanAmountBig = new BigDecimal(loanAmount);
        return investedAmountBig.divide(loanAmountBig, 4, BigDecimal.ROUND_DOWN).doubleValue();
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> openLoan(LoanDto loanDto) {
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
        if (LoanStatus.WAITING_VERIFY == loanDto.getLoanStatus()) {
            loanDto.setLoanStatus(LoanStatus.PREHEAT);
            baseDto = payWrapperClient.createLoan(loanDto);
            BaseDto<PayDataDto> openLoanDto = payWrapperClient.updateLoan(loanDto);
            if (baseDto.getData().getStatus() && openLoanDto.getData().getStatus()) {
                loanDto.setLoanStatus(LoanStatus.RAISING);
                BaseDto<PayDataDto> investLoanDto = payWrapperClient.updateLoan(loanDto);
                if (investLoanDto.getData().getStatus()) {
                    loanDto.setLoanStatus(LoanStatus.PREHEAT);
                    updateLoanAndLoanTitleRelation(loanDto);
                    return investLoanDto;
                }
            }

            return baseDto;
        }
        payDataDto.setStatus(false);
        baseDto.setData(payDataDto);
        return baseDto;
    }

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
        updateLoanAndLoanTitleRelation(loanDto);
        payDataDto.setStatus(true);
        baseDto.setData(payDataDto);
        return baseDto;
    }

    @Override
    public void startFundraising(long loanId) {
        loanMapper.updateStatus(loanId, LoanStatus.RAISING);
        createAutoInvestJob(loanId);
    }

    private void createAutoInvestJob(long loanId) {

    }

    @Override
    public LoanModel findLoanById(long loanId) {
        // TODO: 重构 Mybatis 直接查询关联关系
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
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
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
            for (LoanTitleRelationModel loanTitleRelationModel : loanTitleRelationModelList) {
                loanTitleRelationModel.setId(idGenerator.generate());
                loanTitleRelationModel.setLoanId(loanModel.getId());
            }
            loanTitleRelationMapper.create(loanTitleRelationModelList);
        }
    }

    @Override
    public BaseDto<BasePaginationDataDto> getInvests(long loanId, int index, int pageSize) {
        long count = investMapper.findCountByStatus(loanId, InvestStatus.SUCCESS);
        List<InvestModel> investModels = investMapper.findByStatus(loanId, (index - 1) * pageSize, pageSize, InvestStatus.SUCCESS);
        List<InvestPaginationItemDto> records = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(investModels)) {
            records = Lists.transform(investModels, new Function<InvestModel, InvestPaginationItemDto>() {
                @Override
                public InvestPaginationItemDto apply(InvestModel input) {
                    InvestPaginationItemDto item = new InvestPaginationItemDto();
                    item.setLoginName(input.getLoginName());
                    item.setAmount(AmountUtil.convertCentToString(input.getAmount()));
                    item.setSource(input.getSource());
                    item.setAutoInvest(input.isAutoInvest());
                    item.setExpectedInterest(AmountUtil.convertCentToString(investService.calculateExpectedInterest(input.getLoanId(), input.getAmount())));
                    item.setCreatedTime(input.getCreatedTime());
                    return item;
                }
            });
        }
        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        BasePaginationDataDto<InvestPaginationItemDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, records);
        baseDto.setData(dataDto);
        dataDto.setStatus(true);
        return baseDto;
    }

    @Override
    public BaseDto<BasePaginationDataDto> getLoanerLoanData(int index, int pageSize, LoanStatus status, Date startTime, Date endTime) {
        String loginName = LoginUserInfo.getLoginName();
        if (startTime == null) {
            startTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            startTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        }

        if (endTime == null) {
            endTime = new DateTime().withDate(9999, 12, 31).withTimeAtStartOfDay().toDate();
        } else {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        List<LoanModel> loanModels = Lists.newArrayList();
        long count = 0;
        if (LoanStatus.REPAYING == status) {
            count = loanMapper.findCountRepayingByLoanerLoginName(loginName, startTime, endTime);
            if (count > 0) {
                int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
                index = index > totalPages ? totalPages : index;
                loanModels = loanMapper.findRepayingPaginationByLoanerLoginName(loginName, (index - 1) * pageSize, pageSize, startTime, endTime);
            }
        }

        if (LoanStatus.COMPLETE == status) {
            count = loanMapper.findCountCompletedByLoanerLoginName(loginName, startTime, endTime);
            if (count > 0) {
                int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
                index = index > totalPages ? totalPages : index;
                loanModels = loanMapper.findCompletedPaginationByLoanerLoginName(loginName, (index - 1) * pageSize, pageSize, startTime, endTime);
            }
        }

        if (LoanStatus.CANCEL == status) {
            count = loanMapper.findCountCanceledByLoanerLoginName(loginName, startTime, endTime);
            if (count > 0) {
                int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
                index = index > totalPages ? totalPages : index;
                loanModels = loanMapper.findCanceledPaginationByLoanerLoginName(loginName, (index - 1) * pageSize, pageSize, startTime, endTime);
            }
        }

        List<LoanPaginationItemDataDto> records = Lists.transform(loanModels, new Function<LoanModel, LoanPaginationItemDataDto>() {
            @Override
            public LoanPaginationItemDataDto apply(LoanModel input) {
                return new LoanPaginationItemDataDto(input);
            }
        });

        BasePaginationDataDto<LoanPaginationItemDataDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, records);
        dataDto.setStatus(true);

        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);

        return dto;
    }

    @Override
    public BaseDto<PayDataDto> loanOut(LoanDto loanDto) throws BaseException {
        updateLoanAndLoanTitleRelation(loanDto);

        // 如果存在未处理完成的记录，则不允许放款
        // 放款并记账，同时生成还款计划，处理推荐人奖励，处理短信和邮件通知
        return processLoanOutPayRequest(loanDto.getId());
    }

    private BaseDto<PayDataDto> processLoanOutPayRequest(long loanId) throws BaseException {
        LoanOutDto loanOutDto = new LoanOutDto();
        loanOutDto.setLoanId(String.valueOf(loanId));
        BaseDto<PayDataDto> dto = payWrapperClient.loanOut(loanOutDto);
        if (dto.isSuccess()) {
            PayDataDto data = dto.getData();
            if (!data.getStatus()) {
                logger.error(MessageFormat.format("放款失败: {0}", dto.getData().getMessage()));
                throw new BaseException("放款失败");
            }
        }
        return dto;
    }

    @Override
    public int findLoanListCount(LoanStatus status, long loanId, String loanName, Date startTime, Date endTime) {
        return loanMapper.findLoanListCount(status, loanId, loanName, startTime, endTime);
    }


    @Override
    public List<LoanListDto> findLoanList(LoanStatus status, long loanId, String loanName, Date startTime, Date endTime, int currentPageNo, int pageSize) {
        currentPageNo = (currentPageNo - 1) * 10;
        List<LoanModel> loanModels = loanMapper.findLoanList(status, loanId, loanName, startTime, endTime, currentPageNo, pageSize);
        List<LoanListDto> loanListDtos = Lists.newArrayList();
        for (int i = 0; i < loanModels.size(); i++) {
            LoanListDto loanListDto = new LoanListDto();
            loanListDto.setId(loanModels.get(i).getId());
            loanListDto.setName(loanModels.get(i).getName());
            loanListDto.setType(loanModels.get(i).getType());
            loanListDto.setAgentLoginName(loanModels.get(i).getAgentLoginName());
            loanListDto.setLoanAmount(loanModels.get(i).getLoanAmount());
            loanListDto.setPeriods(loanModels.get(i).getPeriods());
            loanListDto.setBasicRate(String.valueOf(new BigDecimal(loanModels.get(i).getBaseRate()*100).setScale(2,BigDecimal.ROUND_HALF_UP))+"%");
            loanListDto.setActivityRate(String.valueOf(new BigDecimal(loanModels.get(i).getActivityRate()*100).setScale(2,BigDecimal.ROUND_HALF_UP))+"%");
            loanListDto.setStatus(loanModels.get(i).getStatus());
            loanListDto.setCreatedTime(loanModels.get(i).getCreatedTime());
            loanListDtos.add(loanListDto);
        }
        return loanListDtos;
    }
}
