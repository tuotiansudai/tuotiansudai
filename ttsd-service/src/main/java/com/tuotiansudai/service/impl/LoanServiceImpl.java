package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LoanServiceImpl implements LoanService {

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
    IdGenerator idGenerator;

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

    public List<LoanTitleModel> findAllTitles() {
        return loanTitleMapper.findAll();
    }

    @Override
    public List<LoanType> getLoanType() {
        List<LoanType> loanTypes = new ArrayList<LoanType>();
        for (LoanType loanType : LoanType.values()) {
            loanTypes.add(loanType);
        }
        return loanTypes;
    }

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
     * @return
     * @function 创建标的
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> createLoan(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto();
        PayDataDto dataDto = new PayDataDto();
        if (loanDto.getFundraisingStartTime() == null || loanDto.getFundraisingEndTime() == null) {
            dataDto.setStatus(false);
            baseDto.setData(dataDto);
            return baseDto;
        }
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
    public BaseDto<LoanDto> getLoanDetail(String loanId) {
        BaseDto dto = new BaseDto();
        String loginName = LoginUserInfo.getLoginName();
        LoanDto loanDto = new LoanDto();
        LoanModel loanModel = loanMapper.findById(Long.parseLong(loanId));
        if (loanModel == null) {
            dto.setSuccess(true);
            loanDto.setStatus(false);
            return dto;
        }
        loanDto = convertModelToDto(loanModel, loginName);
        loanDto.setStatus(true);

        return dto;
    }

    private LoanDto convertModelToDto(LoanModel loanModel, String loginName) {

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
            loanDto.setBalance(accountModel.getBalance());
        }
        loanDto.setPreheatSeconds(Long.parseLong(this.calculatorPreheatSeconds(loanModel.getFundraisingStartTime())));
        long amountNeedRaised = investMapper.sumSuccessInvestAmount(loanModel.getId());
        loanDto.setAmountNeedRaised(amountNeedRaised);
        loanDto.setRaiseCompletedRate(calculateRaiseCompletedRate(amountNeedRaised, loanModel.getLoanAmount()));
        loanDto.setLoanTitles(loanTitleRelationMapper.findByLoanId(loanModel.getId()));
        return loanDto;
    }

    @Override
    public String getExpectedTotalIncome(long loanId, double investAmount) {

        return null;
    }

    @Override
    public BaseDto<InvestRecordDataDto> getInvests(long loanId, int index, int pageSize) {
        BaseDto baseDto = new BaseDto();
        List<InvestModel> investModels = investMapper.getInvests(loanId, index, pageSize, InvestStatus.SUCCESS);
        List<InvestRecordDto>  investRecordDtos = convertInvestModelToDto(investModels);
        InvestRecordDataDto investRecordDataDto = new InvestRecordDataDto(index,pageSize,investRecordDtos.size());

        baseDto.setSuccess(true);
        investRecordDataDto.setStatus(true);
        baseDto.setData(investRecordDataDto);
        return baseDto;
    }

    private List<InvestRecordDto> convertInvestModelToDto(List<InvestModel> investModels) {
        List<InvestRecordDto> investRecordDtos = new ArrayList<>();
        InvestRecordDto investRecordDto = null;
        for (InvestModel investModel : investModels) {
            investRecordDto = new InvestRecordDto();
            investRecordDto.setLoginName(investModel.getLoginName());
            investRecordDto.setAmount("" + investModel.getAmount());
            investRecordDto.setSource(investModel.getSource());
            //TODO:预期利息
            investRecordDto.setExpectedRate("1.0");
            investRecordDto.setSuccessTime(new SimpleDateFormat("yyyy-MM-dd").format(investModel.getSuccessTime()));
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

    private double calculateRaiseCompletedRate(long amountNeedRaised, long loanAmount) {
        BigDecimal amountNeedRaisedBig = new BigDecimal(amountNeedRaised);
        BigDecimal loanAmountBig = new BigDecimal(loanAmount);
        double raiseCompletedRate = loanAmountBig.subtract(amountNeedRaisedBig).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return raiseCompletedRate;
    }
}
