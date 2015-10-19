package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.TTSDException;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
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
    private RepayService repayService;

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
        }
        String loanUserId = getLoginName(loanDto.getLoanerLoginName());
        if (loanUserId == null) {
            dataDto.setStatus(false);
            dataDto.setMessage("借款用户不存在");
            baseDto.setData(dataDto);
            return baseDto;
        }
        if(loanDto.getPeriods()>12 || loanDto.getPeriods() <= 0){
            dataDto.setStatus(false);
            dataDto.setMessage("借款期限最小为1，最大为12");
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
        DecimalFormat decimalFormat = new DecimalFormat("######0.00");
        LoanDto loanDto = new LoanDto();
        loanDto.setId(loanModel.getId());
        loanDto.setProjectName(loanModel.getName());
        loanDto.setAgentLoginName(loanModel.getAgentLoginName());
        loanDto.setLoanerLoginName(loanModel.getLoanerLoginName());
        loanDto.setPeriods(loanModel.getPeriods());
        loanDto.setDescriptionHtml(loanModel.getDescriptionHtml());
        loanDto.setDescriptionText(loanModel.getDescriptionText());
        loanDto.setLoanAmount(AmountUtil.convertCentToString(loanModel.getLoanAmount()/10000));
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
        }else{
            loanDto.setMaxAvailableInvestAmount("0.00");
        }

        loanDto.setAmountNeedRaised(calculateAmountNeedRaised(investedAmount, loanModel.getLoanAmount()));
        loanDto.setRaiseCompletedRate(calculateRaiseCompletedRate(investedAmount, loanModel.getLoanAmount()));
        loanDto.setLoanTitles(loanTitleRelationMapper.findByLoanId(loanModel.getId()));
        loanDto.setLoanTitleDto(loanTitleMapper.findAll());
        loanDto.setPreheatSeconds(calculatorPreheatSeconds(loanModel.getFundraisingStartTime()));

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

    private List<InvestPaginationItemDto> convertInvestModelToDto(List<InvestModel> investModels,int serialNoBegin) {

        List<InvestPaginationItemDto> investRecordDtos = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("######0.00");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        InvestPaginationItemDto investRecordDto = null;
        for (int i = 0;investModels!=null&&i < investModels.size();i++){
            InvestModel investModel = investModels.get(i);
            investRecordDto = new InvestPaginationItemDto();
            investRecordDto.setLoginName(investModel.getLoginName());
            investRecordDto.setAmount(decimalFormat.format(investModel.getAmount() / 100d));
            investRecordDto.setSource(investModel.getSource());
            investRecordDto.setAutoInvest(investModel.isAutoInvest());
            investRecordDto.setSerialNo(serialNoBegin + i + 1);
            //TODO:预期利息
            investRecordDto.setExpectedRate(decimalFormat.format(1.0));
            investRecordDto.setCreatedTime(simpleDateFormat.format(investModel.getCreatedTime()));

            investRecordDtos.add(investRecordDto);
        }

        return investRecordDtos;
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

    private double calculateAmountNeedRaised(long amountNeedRaised, long loanAmount) {
        BigDecimal amountNeedRaisedBig = new BigDecimal(amountNeedRaised);
        BigDecimal loanAmountBig = new BigDecimal(loanAmount);
        double amountNeedRaisedDouble = loanAmountBig.subtract(amountNeedRaisedBig)
                .divide(new BigDecimal(100d))
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return amountNeedRaisedDouble;
    }

    private double calculateRaiseCompletedRate(long investedAmount, long loanAmount) {
        BigDecimal investedAmountBig = new BigDecimal(investedAmount);
        BigDecimal loanAmountBig = new BigDecimal(loanAmount);
        return investedAmountBig.divide(loanAmountBig)
                .setScale(2, BigDecimal.ROUND_DOWN)
                .multiply(new BigDecimal(100))
                .doubleValue();
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
            loanTitleRelationMapper.create(loanTitleRelationModelList);
        }
    }
    @Override
    public BaseDto<BasePaginationDataDto> getInvests(long loanId, int index, int pageSize) {
        if (index <= 0) {
            index = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        long count = investMapper.findCountByStatus(loanId, InvestStatus.SUCCESS);
        List<InvestModel> investModels = investMapper.findByStatus(loanId, (index - 1) * pageSize, pageSize, InvestStatus.SUCCESS);
        List<InvestPaginationItemDto> investRecords = convertInvestModelToDto(investModels, (index - 1) * pageSize);
        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        BasePaginationDataDto<InvestPaginationItemDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, investRecords);
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
                int totalPages = (int) (count % pageSize > 0 ? count / index + 1 : count / index);
                index = index > totalPages ? totalPages : index;
                loanModels = loanMapper.findRepayingPaginationByLoanerLoginName(loginName, (index - 1) * pageSize, pageSize, startTime, endTime);
            }
        }

        if (LoanStatus.COMPLETE == status) {
            count = loanMapper.findCountCompletedByLoanerLoginName(loginName, startTime, endTime);
            if (count > 0) {
                int totalPages = (int) (count % pageSize > 0 ? count / index + 1 : count / index);
                index = index > totalPages ? totalPages : index;
                loanModels = loanMapper.findCompletedPaginationByLoanerLoginName(loginName, (index - 1) * pageSize, pageSize, startTime, endTime);
            }
        }

        if (LoanStatus.CANCEL == status) {
            count = loanMapper.findCountCanceledByLoanerLoginName(loginName, startTime, endTime);
            if (count > 0) {
                int totalPages = (int) (count % pageSize > 0 ? count / index + 1 : count / index);
                index = index > totalPages ? totalPages : index;
                loanModels = loanMapper.findCanceledPaginationByLoanerLoginName(loginName, (index - 1) * pageSize, pageSize, startTime, endTime);
            }
        }

        List<LoanerLoanPaginationItemDataDto> records = Lists.transform(loanModels, new Function<LoanModel, LoanerLoanPaginationItemDataDto>() {
            @Override
            public LoanerLoanPaginationItemDataDto apply(LoanModel input) {
                return new LoanerLoanPaginationItemDataDto(input);
            }
        });

        BasePaginationDataDto<LoanerLoanPaginationItemDataDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, records);
        dataDto.setStatus(true);

        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);

        return dto;
    }

    public LoanRepayDataDto getLoanerLoanData(long loanId) {
        return null;
    }

    @Override
    public void loanOut(long loanId, long minInvestAmount, Date fundraisingEndTime) throws TTSDException {
        // 修改标的的最小投资金额和投资截止时间
        updateLoanInfo(loanId, minInvestAmount, fundraisingEndTime);

        // 如果存在未处理完成的记录，则不允许放款
        // 放款并记账，同时生成还款计划，处理推荐人奖励，处理短信和邮件通知
        processLoanOutPayRequest(loanId);
    }

    private void updateLoanInfo(long loanId, long minInvestAmount, Date fundraisingEndTime) {
        LoanModel loan4update = new LoanModel();
        loan4update.setId(loanId);
        if (fundraisingEndTime != null) {
            loan4update.setFundraisingEndTime(fundraisingEndTime);
        }
        loan4update.setMinInvestAmount(minInvestAmount);
        loanMapper.update(loan4update);
    }

    private void processLoanOutPayRequest(long loanId) throws TTSDException {
        LoanOutDto loanOutDto = new LoanOutDto();
        loanOutDto.setLoanId(String.valueOf(loanId));
        BaseDto<PayDataDto> resp = payWrapperClient.loanOut(loanOutDto);
        if (resp.isSuccess()) {
            PayDataDto data = resp.getData();
            if (!data.getStatus()) {
                logger.error("放款失败:" + resp.getData().getMessage());
                throw new TTSDException("放款失败");
            }
        }
    }
}
