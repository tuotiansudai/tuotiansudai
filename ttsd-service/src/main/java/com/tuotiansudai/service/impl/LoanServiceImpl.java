package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.LoanTitleDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanTitleMapper;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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
}
