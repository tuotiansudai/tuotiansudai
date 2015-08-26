package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanTitleMapper;
import com.tuotiansudai.repository.mapper.TitleMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.DateCompare;
import com.tuotiansudai.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private TitleMapper titleMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Autowired
    IdGenerator idGenerator;
    /**
     * @param loanTitleDto
     * @function 创建标题
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TitleModel createTitle(LoanTitleDto loanTitleDto) {
        TitleModel titleModel = new TitleModel();
        long id = idGenerator.generate();
        titleModel.setId(id);
        titleModel.setTitle(loanTitleDto.getTitle());
        titleModel.setType("new");
        titleMapper.create(titleModel);
        return titleModel;
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

    public List<TitleModel> findAllTitles(){
        return titleMapper.find();
    }

    @Override
    public List<LoanType> getLoanType() {
        List<LoanType> loanTypes = new ArrayList<LoanType>();
        for (LoanType loanType:LoanType.values()){
            loanTypes.add(loanType);
        }
        return loanTypes;
    }

    @Override
    public List<ActivityType> getActivityType() {
        List<ActivityType> activityTypes = new ArrayList<ActivityType>();
        for (ActivityType activityType:ActivityType.values()){
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
    public BaseDto<PayDataDto> createLoanBid(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto();
        PayDataDto dataDto = new PayDataDto();
        long minInvestAmount = AmountUtil.convertStringToCent(loanDto.getMinInvestAmount());
        long maxInvestAmount = AmountUtil.convertStringToCent(loanDto.getMaxInvestAmount());;
        if (maxInvestAmount < minInvestAmount){
            dataDto.setStatus(false);
            return baseDto;
        }
        Integer result = DateCompare.compareDate(loanDto.getFundraisingStartTime(), loanDto.getFundraisingEndTime());
        if (result == null || result == 1){
            dataDto.setStatus(false);
            return baseDto;
        }
        String loanUserId = getLoginName(loanDto.getLoanerLoginName());
        if (loanUserId == null) {
            dataDto.setStatus(false);
            return baseDto;
        }
        String loanAgentId = getLoginName(loanDto.getAgentLoginName());
        if (loanAgentId == null){
            dataDto.setStatus(false);
            return baseDto;
        }
        long projectId = idGenerator.generate();/****标的号****/
        loanDto.setId(projectId);

        loanDto.setLoanAmount(String.valueOf(AmountUtil.convertStringToCent(loanDto.getLoanAmount())));
        loanDto.setMaxInvestAmount(String.valueOf(maxInvestAmount));
        loanDto.setMinInvestAmount(String.valueOf(minInvestAmount));
        loanDto.setInvestIncreasingAmount(String.valueOf(AmountUtil.convertStringToCent(loanDto.getInvestIncreasingAmount())));

        loanDto.setActivityRate(rateStrDivideOneHundred(loanDto.getActivityRate()));
        loanDto.setInvestFeeRate(rateStrDivideOneHundred(loanDto.getInvestFeeRate()));
        loanDto.setBasicRate(rateStrDivideOneHundred(loanDto.getBasicRate()));

        loanDto.setCreatedTime(new Date());
        loanDto.setStatus(LoanStatus.WAITING_VERIFY);
        loanMapper.create(new LoanModel(loanDto));
        List<LoanTitleRelation> loanTitleRelationList = loanDto.getLoanTitles();
        for (LoanTitleRelation loanTitleRelation : loanDto.getLoanTitles()){
            loanTitleRelation.setId(idGenerator.generate());
            loanTitleRelation.setLoanId(projectId);
        }
        loanTitleMapper.create(loanTitleRelationList);
        dataDto.setStatus(true);
        baseDto.setData(dataDto);
        return baseDto;
    }

    public String getLoginName(String loginName){
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        String loanUserId = null;
        if (accountModel != null) {
            loanUserId = accountModel.getPayUserId();
        }
        return loanUserId;
    }

    public String rateStrDivideOneHundred(String rate){
        BigDecimal rateBigDecimal = new BigDecimal(rate);
        return String.valueOf(rateBigDecimal.divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }
}
