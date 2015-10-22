package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.InvestService;
import com.tuotiansudai.paywrapper.service.UserBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
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
    ProjectTransferNotifyMapper projectTransferNotifyMapper;

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
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
            investMapper.create(investModel);
            return baseDto;
        } catch (PayException e) {
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setStatus(false);
            payFormDataDto.setMessage(e.getMessage());
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
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

    /**
     * 投资回调接口，记录请求入库
     *
     * @param paramsMap
     * @param originalQueryString
     * @return
     */
    @Override
    @Transactional
    public String investCallback(Map<String, String> paramsMap, String originalQueryString) {
        // status标记此条记录是否已经被处理，0:未处理；1:已处理。
        paramsMap.put("status","0");
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                ProjectTransferNotifyMapper.class,
                ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }
//        postInvestCallback(callbackRequest);
        String respData = callbackRequest.getResponseData();
        return respData;
    }


    public BaseDto<BaseDataDto> asyncInvestCallback(){
        List<ProjectTransferNotifyRequestModel> todoList = projectTransferNotifyMapper.getTodoList();

        for(ProjectTransferNotifyRequestModel model : todoList) {
            processOneCallback(model);
        }

        BaseDto<BaseDataDto> asyncInvestNotifyDto = new BaseDto<>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDataDto.setStatus(true);
        asyncInvestNotifyDto.setData(baseDataDto);

        return  asyncInvestNotifyDto;
    }


    @Transactional
    private synchronized void processOneCallback(BaseCallbackRequestModel callbackRequestModel) {
        long orderId = Long.parseLong(callbackRequestModel.getOrderId());
        InvestModel investModel = investMapper.findById(orderId);
        if (investModel == null) {
            logger.error(MessageFormat.format("invest(project_transfer) callback order is not exist (orderId = {0})", callbackRequestModel.getOrderId()));
            return;
        }

        boolean status = true;

        String loginName = investModel.getLoginName();
        if (callbackRequestModel.isSuccess()) {

            long loanId = investModel.getLoanId();
            long successInvestAmountTotal = investMapper.sumSuccessInvestAmount(loanId);

            LoanModel loanModel = loanMapper.findById(loanId);
            if (successInvestAmountTotal + investModel.getAmount() > loanModel.getLoanAmount()) {
                // 超投返款处理
                status = overInvestPaybackProcess(orderId, investModel, loginName, loanId);
            } else {
                // freeze  冻结用户账户内的相应金额，记录用户交易userBill
                try {
                    userBillService.freeze(loginName, orderId, investModel.getAmount(), UserBillBusinessType.INVEST_SUCCESS);
                } catch (AmountTransferException e) {
                    logger.error("投资成功，但资金冻结失败", e);
                }
                // 改invest 本身状态
                investMapper.updateStatus(investModel.getId(), InvestStatus.SUCCESS);
                if (successInvestAmountTotal + investModel.getAmount() == loanModel.getLoanAmount()) {
                    // 满标，改标的状态 RECHECK
                    loanMapper.updateStatus(loanId, LoanStatus.RECHECK);
                }
            }
        } else {
            // 失败的话：改invest本身状态
            investMapper.updateStatus(investModel.getId(), InvestStatus.FAIL);
        }
        if(status) {
            projectTransferNotifyMapper.updateStatus(callbackRequestModel.getId(), 1);
        }
    }

    /**
     * 超投处理：返款、解冻、记录userBill、更新投资状态为失败
     *
     * @param orderId
     * @param investModel
     * @param loginName
     * @param loanId
     */
    @Transactional
    private boolean overInvestPaybackProcess(long orderId, InvestModel investModel, String loginName, long loanId) {
        // 超投处理：
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.overInvestPaybackRequest(
                String.valueOf(loanId), String.valueOf(loanId), accountModel.getPayUserId(), String.valueOf(investModel.getAmount()));

        boolean paybackSuccess = false;
        int tryCount = 0;
        // 超投返款，如果返款失败，最多重试3次
        while(++tryCount<=3) {
            try {
                ProjectTransferResponseModel responseModel = paySyncClient.send(
                        ProjectTransferMapper.class,
                        requestModel,
                        ProjectTransferResponseModel.class);

                if (responseModel.isSuccess()) {
                    // 解冻用户资金，记录userBill
                    userBillService.unfreeze(loginName, orderId, investModel.getAmount(), UserBillBusinessType.OVER_INVEST_PAYBACK);
                    // 改invest 本身状态为超投返款
                    investMapper.updateStatus(investModel.getId(), InvestStatus.OVER_INVEST_PAYBACK);
                    paybackSuccess = true;
                    break;
                } else {
                    logger.error("超投返款失败 "+tryCount+" 次");
                }
            } catch (AmountTransferException e) {
                logger.error("超投返款成功，但资金解冻失败", e);
            } catch (PayException e) {
                logger.error("超投返款失败 "+tryCount+" 次 ", e);
            }
        }
        if(!paybackSuccess) {
            // 如果3次重试后，还是返款失败，则记录本次投资为 超投返款失败
            investMapper.updateStatus(investModel.getId(), InvestStatus.OVER_INVEST_PAYBACK_FAIL);
        }
        return paybackSuccess;
    }


}
