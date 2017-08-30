package com.tuotiansudai.paywrapper.current;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.current.dto.LoanOutHistoryDto;
import com.tuotiansudai.current.dto.LoanOutHistoryStatus;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.CurrentLoanOutTransferAgentRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CurrentLoanOutTransferReserveRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CurrentLoanOutTransferReserveNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Service
public class CurrentLoanOutService {

    private final static Logger logger = Logger.getLogger(CurrentLoanOutService.class);

    private final static String ORDER_ID_SEPARATOR = "X";

    private final static String ORDER_ID_TEMPLATE = "{0}" + ORDER_ID_SEPARATOR + "{1}";

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    public LoanOutHistoryDto loanOut(LoanOutHistoryDto loanOutHistoryDto) throws Exception {
        if (loanOutHistoryDto.getStatus() == LoanOutHistoryStatus.RESERVE_TRANSFER_WAITING_PAY) {
            return this.reserveTransfer(loanOutHistoryDto);
        }

        if (loanOutHistoryDto.getStatus() == LoanOutHistoryStatus.WAITING_PAY) {
            return this.agentTransfer(loanOutHistoryDto);
        }

        return null;
    }

    private LoanOutHistoryDto reserveTransfer(LoanOutHistoryDto loanOutHistoryDto) throws Exception {
        logger.info(MessageFormat.format("[current loan out] reserve transfer is starting, request data: {0}", loanOutHistoryDto.toString()));
        //此账户需要提前准备好（特殊的个人账号）
        String reserveAccount = loanOutHistoryDto.getReserveAccount();
        AccountModel accountModel = accountMapper.findByLoginName(reserveAccount);
        if (accountModel == null || accountModel.getBalance() < loanOutHistoryDto.getInterestAmount()) {
            logger.error(MessageFormat.format("[Current Loan Out] reserve transfer account({0}) is not found or balance is not enough, request data: {1}",
                    loanOutHistoryDto.getReserveAccount(),
                    loanOutHistoryDto.toString()));
            return null;
        }

        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newCurrentLoanOutTransferReserveRequest(
                MessageFormat.format(ORDER_ID_TEMPLATE, String.valueOf(loanOutHistoryDto.getId()), String.valueOf(new Date().getTime())),
                accountModel.getPayUserId(),
                String.valueOf(loanOutHistoryDto.getInterestAmount()));

        ProjectTransferResponseModel responseModel = paySyncClient.send(CurrentLoanOutTransferReserveRequestMapper.class,
                requestModel,
                ProjectTransferResponseModel.class);

        String log_message = MessageFormat.format("[Current Loan Out] reserve transfer request finished, request data: {0}, response: {1}", loanOutHistoryDto.toString(), String.valueOf(responseModel.isSuccess()));
        if (responseModel.isSuccess()) {
            logger.info(log_message);
        } else {
            logger.error(log_message);
        }

        loanOutHistoryDto.setStatus(responseModel.isSuccess() ? LoanOutHistoryStatus.RESERVE_TRANSFER_SUCCESS : LoanOutHistoryStatus.RESERVE_TRANSFER_FAIL);

        return loanOutHistoryDto;
    }

    private LoanOutHistoryDto agentTransfer(LoanOutHistoryDto loanOutHistoryDto) throws Exception {
        logger.info(MessageFormat.format("[current loan out] agent transfer is starting, request data: {0}", loanOutHistoryDto.toString()));
        //此账户需要提前准备好（特殊的个人账号）
        String agentAccount = loanOutHistoryDto.getAgentAccount();
        AccountModel accountModel = accountMapper.findByLoginName(agentAccount);

        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newCurrentLoanOutTransferAgentRequest(
                MessageFormat.format(ORDER_ID_TEMPLATE, String.valueOf(loanOutHistoryDto.getId()), String.valueOf(new Date().getTime())),
                accountModel.getPayUserId(),
                String.valueOf(loanOutHistoryDto.getInterestAmount()));

        ProjectTransferResponseModel responseModel = paySyncClient.send(CurrentLoanOutTransferAgentRequestMapper.class,
                requestModel,
                ProjectTransferResponseModel.class);

        String message = MessageFormat.format("[Current Loan Out] agent transfer finished, request data: {0}, response {1}", loanOutHistoryDto.toString(), String.valueOf(responseModel.isSuccess()));
        if (responseModel.isSuccess()) {
            logger.info(message);
        } else {
            logger.error(message);
        }

        loanOutHistoryDto.setStatus(responseModel.isSuccess() ? LoanOutHistoryStatus.SUCCESS : LoanOutHistoryStatus.FAIL);

        return loanOutHistoryDto;
    }

    public String reserveTransferCallback(Map<String, String> paramsMap, String originalQueryString) {
        logger.info(MessageFormat.format("[current loan out] reserve transfer notify is coming, request data: {0}", originalQueryString));

        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                CurrentLoanOutTransferReserveNotifyRequestMapper.class,
                ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        try {
            long orderId = Long.parseLong(callbackRequest.getOrderId().split(ORDER_ID_SEPARATOR)[0]);

            String message = MessageFormat.format("[Current Loan Out] reserve transfer notify, order id: {0}, callback status: {1}", String.valueOf(orderId), String.valueOf(callbackRequest.isSuccess()));
            if (callbackRequest.isSuccess()) {
                logger.info(message);
            } else {
                logger.error(message);
            }

            LoanOutHistoryDto loanOutHistoryDto = new LoanOutHistoryDto();
            loanOutHistoryDto.setId(orderId);
            loanOutHistoryDto.setStatus(callbackRequest.isSuccess() ? LoanOutHistoryStatus.RESERVE_TRANSFER_SUCCESS : LoanOutHistoryStatus.RESERVE_TRANSFER_FAIL);

            mqWrapperClient.sendMessage(MessageQueue.CurrentLoanOutCallback, loanOutHistoryDto.toString());

            logger.info(MessageFormat.format("[Current Loan Out] reserve transfer notify, send queue message: {0}", loanOutHistoryDto.toString()));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Current Loan Out] reserve transfer notify, order id: {0}, callback status: {1}", String.valueOf(callbackRequest.getOrderId()), String.valueOf(callbackRequest.isSuccess())), e);
        }

        return callbackRequest.getResponseData();
    }

    public String agentTransferCallback(Map<String, String> paramsMap, String originalQueryString) {
        logger.info(MessageFormat.format("[current loan out] agent transfer notify is coming, request data: {0}", originalQueryString));

        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                CurrentLoanOutTransferReserveNotifyRequestMapper.class,
                ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        try {
            long orderId = Long.parseLong(callbackRequest.getOrderId().split(ORDER_ID_SEPARATOR)[0]);

            String message = MessageFormat.format("[Current Loan Out] agent transfer notify, order id: {0}, callback status: {1}", String.valueOf(orderId), String.valueOf(callbackRequest.isSuccess()));
            if (callbackRequest.isSuccess()) {
                logger.info(message);
            } else {
                logger.error(message);
            }

            LoanOutHistoryDto loanOutHistoryDto = new LoanOutHistoryDto();
            loanOutHistoryDto.setId(orderId);
            loanOutHistoryDto.setStatus(callbackRequest.isSuccess() ? LoanOutHistoryStatus.SUCCESS : LoanOutHistoryStatus.FAIL);

            mqWrapperClient.sendMessage(MessageQueue.CurrentLoanOutCallback, loanOutHistoryDto.toString());

            logger.info(MessageFormat.format("[Current Loan Out] agent transfer notify, send queue message: {0}", loanOutHistoryDto.toString()));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Current Loan Out] agent transfer notify, order id: {0}, callback status: {1}", String.valueOf(callbackRequest.getOrderId()), String.valueOf(callbackRequest.isSuccess())), e);
        }

        return callbackRequest.getResponseData();
    }
}
