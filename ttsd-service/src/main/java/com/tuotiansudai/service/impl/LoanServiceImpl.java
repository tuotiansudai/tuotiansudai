package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanTitleMapper;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
import com.google.common.base.Function;
import com.tuotiansudai.exception.TTSDException;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
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
    public List<LoanListDto> findLoanList(LoanStatus status,long loanId,String loanName,Date startTime,Date endTime,int currentPageNo) {
        currentPageNo = (currentPageNo - 1) * 10;
        List<LoanModel> loanModels = loanMapper.findLoanList(status,loanId,loanName,startTime!=null?new DateTime(startTime).toString("yyyy-MM-dd HH:mm:ss"):null,endTime!=null?new DateTime(endTime).toString("yyyy-MM-dd HH:mm:ss"):null,currentPageNo);
        List<LoanListDto> loanListDtos = Lists.newArrayList();
        for (int i=0;i<loanModels.size();i++) {
            LoanListDto loanListDto = new LoanListDto();
            loanListDto.setId(loanModels.get(i).getId());
            loanListDto.setName(loanModels.get(i).getName());
            loanListDto.setType(loanModels.get(i).getType());
            loanListDto.setAgentLoginName(loanModels.get(i).getAgentLoginName());
            loanListDto.setLoanAmount(loanModels.get(i).getLoanAmount());
            loanListDto.setPeriods(loanModels.get(i).getPeriods());
            loanListDto.setBasicRate(String.valueOf(loanModels.get(i).getBaseRate()*100)+"%");
            loanListDto.setActivityRate(String.valueOf(loanModels.get(i).getActivityRate()*100)+"%");
            loanListDto.setStatus(loanModels.get(i).getStatus());
            loanListDto.setCreatedTime(loanModels.get(i).getCreatedTime());
            loanListDtos.add(loanListDto);
        }
        return loanListDtos;
    }

    @Override
    public int findLoanListCount(LoanStatus status,long loanId,String loanName,Date startTime,Date endTime) {
        return loanMapper.findLoanListCount(status, loanId, loanName, startTime!=null?new DateTime(startTime).toString("yyyy-MM-dd HH:mm:ss"):null, endTime!=null?new DateTime(endTime).toString("yyyy-MM-dd HH:mm:ss"):null);
    }

    public BaseDto<LoanDto> getLoanDetail(long loanId) {
        BaseDto<LoanDto> dto = new BaseDto<>();
        LoanDto loanDto = new LoanDto();
        dto.setData(loanDto);
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            loanDto.setStatus(false);
            return dto;
        }
        loanDto = convertModelToDto(loanModel);
        loanDto.setStatus(true);
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
        loanDto.setLoanAmount(String.valueOf(loanModel.getLoanAmount()));
        loanDto.setInvestIncreasingAmount(String.valueOf(loanModel.getInvestIncreasingAmount()));
        loanDto.setActivityType(loanModel.getActivityType());
        loanDto.setActivityRate(String.valueOf(loanModel.getActivityRate()));
        loanDto.setBasicRate(String.valueOf(loanModel.getBaseRate()));
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
    public BaseDto<BasePaginationDataDto> getInvests(long loanId, int index, int pageSize) {
        if (index <= 0) {
            index = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        long count = investMapper.findCountByStatus(loanId, InvestStatus.SUCCESS);
        List<InvestModel> investModels = investMapper.findByStatus(loanId, (index - 1) * pageSize, pageSize, InvestStatus.SUCCESS);
        List<InvestPaginationItemDto> investRecords = convertInvestModelToDto(investModels);
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

    private List<InvestPaginationItemDto> convertInvestModelToDto(List<InvestModel> investModels) {
        List<InvestPaginationItemDto> investRecordDtos = new ArrayList<>();
        InvestPaginationItemDto investRecordDto;
        for (InvestModel investModel : investModels) {
            investRecordDto = new InvestPaginationItemDto();
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

    private double calculateRaiseCompletedRate(long investedAmount, long loanAmount) {
        BigDecimal investedAmountBig = new BigDecimal(investedAmount);
        BigDecimal loanAmountBig = new BigDecimal(loanAmount);
        return investedAmountBig.divide(loanAmountBig).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private double calculateAmountNeedRaised(long amountNeedRaised, long loanAmount) {
        BigDecimal amountNeedRaisedBig = new BigDecimal(amountNeedRaised);
        BigDecimal loanAmountBig = new BigDecimal(loanAmount);
        return loanAmountBig.subtract(amountNeedRaisedBig)
                .divide(new BigDecimal(100d)).doubleValue();
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
