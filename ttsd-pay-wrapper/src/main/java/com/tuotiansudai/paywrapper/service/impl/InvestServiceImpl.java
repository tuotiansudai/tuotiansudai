package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferModel;
import com.tuotiansudai.paywrapper.service.InvestService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
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
    private LoanMapper loanMapper;

    @Override
    public BaseDto<PayFormDataDto> invest(InvestDto dto) {
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());

        InvestModel investModel = new InvestModel(dto);
        investModel.setId(idGenerator.generate());
        ProjectTransferModel requestModel = new ProjectTransferModel(
                dto.getLoanId(),
                String.valueOf(investModel.getId()),
                accountModel.getPayUserId(),
                String.valueOf(investModel.getAmount()));
        try {
            checkLoanRemainAmount(dto);
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
            investMapper.create(investModel);
            return baseDto;
        } catch (PayException e) {
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setStatus(false);
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
    }

    private void checkLoanRemainAmount(InvestDto dto) throws PayException {
        long loanId = Long.parseLong(dto.getLoanId());
        LoanModel loan = loanMapper.findById(loanId);
        if (loan == null) {
            logger.error("投资失败，查找不到指定的标的[" + dto.getLoanId() + "]");
            throw new PayException("标的不存在");
        }
        long successInvestAmount = investMapper.sumSuccessInvestAmount(loanId);
        long remainAmount = loan.getLoanAmount() - successInvestAmount;
        long investRequestAmount = AmountUtil.convertStringToCent(dto.getAmount());

        if(remainAmount<investRequestAmount){
            logger.error("投资失败，投资金额["+investRequestAmount+"]超过标的[" + dto.getLoanId() + "]可投金额["+remainAmount+"]");
            throw new PayException("投资金额超过标的可投金额");
        }
    }

    @Override
    public String investCallback(Map<String, String> paramsMap, String originalQueryString){
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

    @Transactional
    private void postInvestCallback(BaseCallbackRequestModel callbackRequestModel) {
        try {
            // freeze
            // 改invest 本身状态

            // 满标，改标的状态 RECHECK
            // TODO:超投

            // 失败的话：改invest本身状态
            long orderId = Long.parseLong(callbackRequestModel.getOrderId());
            InvestModel investMode = investMapper.findById(orderId);
            if (investMode == null) {
                logger.error(MessageFormat.format("invest(project_transfer) callback order is not exist (orderId = {0})", callbackRequestModel.getOrderId()));
                return;
            }

            String loginName = rechargeModel.getLoginName();
            long amount = rechargeModel.getAmount();
            if (callbackRequestModel.isSuccess()) {
                rechargeMapper.update(orderId, RechargeStatus1.SUCCESS);
                userBillService.transferInBalance(loginName, orderId, amount, UserBillBusinessType.RECHARGE_SUCCESS);
                //TODO update system bill
            } else {
                rechargeMapper.update(orderId, RechargeStatus1.FAIL);
            }
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("Recharge callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage());
        }

    }

}
