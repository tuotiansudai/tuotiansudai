package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNopwdMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.paywrapper.service.InvestService;
import com.tuotiansudai.paywrapper.service.UserBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.AutoInvestPlanMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.AutoInvestMonthPeriod;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
public class InvestServiceImpl implements InvestService {

    static Logger logger = Logger.getLogger(InvestServiceImpl.class);

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserBillService userBillService;

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> invest(InvestDto dto) {
        // TODO : 这个方法里的事务如何处理
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());

        InvestModel investModel = new InvestModel(dto);
        investModel.setId(idGenerator.generate());
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newInvestRequest(
                dto.getLoanId(),
                String.valueOf(investModel.getId()),
                accountModel.getPayUserId(),
                String.valueOf(investModel.getAmount()));
        try {
            checkLoanInvestAccountAmount(dto.getLoginName(), investModel.getLoanId(), investModel.getAmount());
            investMapper.create(investModel);
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
            return baseDto;
        } catch (PayException e) {
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setStatus(false);
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
    }

    @Transactional
    private BaseDto<PayDataDto> investNopwd(InvestDto dto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);

        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());

        InvestModel investModel = new InvestModel(dto);
        investModel.setIsAutoInvest(true);
        investModel.setId(idGenerator.generate());
        investMapper.create(investModel);
        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newInvestNopwdRequest(
                dto.getLoanId(),
                String.valueOf(investModel.getId()),
                accountModel.getPayUserId(),
                String.valueOf(investModel.getAmount())
        );
        try {
            checkLoanInvestAccountAmount(dto.getLoginName(), investModel.getLoanId(), investModel.getAmount());
            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(
                    ProjectTransferNopwdMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);
            if(responseModel.isSuccess()){
                onInvestSuccess(investModel);
            }
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            onInvestFail(investModel);
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getLocalizedMessage());
            logger.error(e.getLocalizedMessage(), e);
        }
        return baseDto;
    }

    private void checkLoanInvestAccountAmount(String loginName, long loanId, long investAmount) throws PayException {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel.getBalance() < investAmount) {
            logger.error("投资失败，投资金额[" + investAmount + "]超过用户[" + loginName + "]账户余额[" + accountModel.getBalance() + "]");
            throw new PayException("账户余额不足");
        }
        LoanModel loan = loanMapper.findById(loanId);
        if (loan == null) {
            logger.error("投资失败，查找不到指定的标的[" + loanId + "]");
            throw new PayException("标的不存在");
        }
        long successInvestAmount = investMapper.sumSuccessInvestAmount(loanId);
        long remainAmount = loan.getLoanAmount() - successInvestAmount;

        if (remainAmount < investAmount) {
            logger.error("投资失败，投资金额[" + investAmount + "]超过标的[" + loanId + "]可投金额[" + remainAmount + "]");
            throw new PayException("投资金额超过标的可投金额");
        }
    }

    @Override
    @Transactional
    public String investCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                ProjectTransferNotifyMapper.class,
                ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }
        postInvestCallback(callbackRequest);
        String respData = callbackRequest.getResponseData();
        return respData;
    }

    private void postInvestCallback(BaseCallbackRequestModel callbackRequestModel) {
        long orderId = Long.parseLong(callbackRequestModel.getOrderId());
        InvestModel investModel = investMapper.findById(orderId);
        if (investModel == null) {
            logger.error(MessageFormat.format("invest(project_transfer) callback order is not exist (orderId = {0})", callbackRequestModel.getOrderId()));
            return;
        }
        if (callbackRequestModel.isSuccess()) {
            onInvestSuccess(investModel);
        } else {
            onInvestFail(investModel);
        }
    }

    private void onInvestSuccess(InvestModel investModel) {
        long loanId = investModel.getLoanId();
        // freeze
        try {
            userBillService.freeze(investModel.getLoginName(), investModel.getId(), investModel.getAmount(), UserBillBusinessType.INVEST_SUCCESS);
        } catch (AmountTransferException e) {
            logger.error("投资成功，但资金冻结失败", e);
        }
        // 改invest 本身状态
        investMapper.updateStatus(investModel.getId(), InvestStatus.SUCCESS);
        LoanModel loanModel = loanMapper.findById(loanId);
        long successInvestAmountTotal = investMapper.sumSuccessInvestAmount(loanId);
        // 满标，改标的状态 RECHECK
        if (successInvestAmountTotal == loanModel.getLoanAmount()) {
            loanMapper.updateStatus(loanId, LoanStatus.RECHECK);
        } else if (successInvestAmountTotal > loanModel.getLoanAmount()) {
            // TODO:超投
        }
    }

    private void onInvestFail(InvestModel investModel){
        investMapper.updateStatus(investModel.getId(), InvestStatus.FAIL);
    }

    @Override
    public List<AutoInvestPlanModel> findValidPlanByPeriod(AutoInvestMonthPeriod period) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return autoInvestPlanMapper.findEnabledPlanByPeriod(period.getPeriodValue(), cal.getTime());
    }

    @Override
    public void autoInvest(long loanId) {
        LoanModel loanModel= loanMapper.findById(loanId);
        List<AutoInvestPlanModel> autoInvestPlanModels = this.findValidPlanByPeriod(AutoInvestMonthPeriod.generateFromLoanPeriod(loanModel.getPeriods()));
        for (AutoInvestPlanModel autoInvestPlanModel: autoInvestPlanModels) {
            try {
                long availableLoanAmount = loanModel.getLoanAmount() - investMapper.sumSuccessInvestAmount(loanId);
                if (availableLoanAmount <= 0) {
                    return;
                }
                InvestDto investDto = new InvestDto();
                investDto.setLoanId(String.valueOf(loanId));
                investDto.setLoginName(autoInvestPlanModel.getLoginName());
                long autoInvestAmount = this.calculateAutoInvestAmount(autoInvestPlanModel, availableLoanAmount);
                if (autoInvestAmount == 0) {
                    continue;
                }
                investDto.setAmount(String.valueOf(autoInvestAmount));
                investDto.setInvestSource(InvestSource.AUTO);
                BaseDto<PayDataDto> baseDto = this.investNopwd(investDto);
                if (!baseDto.isSuccess()) {
                    logger.debug(MessageFormat.format("auto invest failed auto invest plan id is {0} and invest amount is {1} and loanId id {2}",autoInvestPlanModel.getId(),autoInvestAmount,loanId));
                }
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
                continue;
            }
        }
    }

    private long calculateAutoInvestAmount(AutoInvestPlanModel autoInvestPlanModel, long availableLoanAmount) {
        long availableAmount = accountMapper.findByLoginName(autoInvestPlanModel.getLoginName()).getBalance() - autoInvestPlanModel.getRetentionAmount();
        long maxInvestAmount = autoInvestPlanModel.getMaxInvestAmount();
        long minInvestAmount = autoInvestPlanModel.getMinInvestAmount();
        long returnAmount = 0;
        if (availableLoanAmount < minInvestAmount) {
            return returnAmount;
        }
        if (availableAmount >= maxInvestAmount) {
            returnAmount = maxInvestAmount;
        } else if (availableAmount < maxInvestAmount && availableAmount >= minInvestAmount) {
            returnAmount = availableAmount;
        }
        if (returnAmount >= availableLoanAmount) {
            returnAmount = availableLoanAmount;
        }
        return returnAmount;
    }
}
