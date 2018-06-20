package com.tuotiansudai.fudian.config;

import com.tuotiansudai.fudian.service.*;
import com.tuotiansudai.fudian.strategy.*;

public enum ApiType {

    REGISTER(new RegisterResponseParser(), "/user/register", RegisterService.class),
    CARD_BIND(new CardBindResponseParser(), "/user/card/bind", CardBindService.class),
    CANCEL_CARD_BIND(new CancelCardBindResponseParser(), "/user/card/cancelBind", CancelCardBindService.class),

    RECHARGE(new RechargeResponseParser(), "/account/recharge", RechargeService.class),
    RECHARGE_M(new RechargeResponseParser(), "/app/realPayRecharge", RechargeService.class),
    WITHDRAW(new WithdrawResponseParser(), "/account/withdraw", WithdrawService.class),

    AUTHORIZATION(new AuthorizationResponseParser(), "/business/authorization", AuthorizationService.class),

    PASSWORD_RESET(new PasswordResetResponseParser(), "/pwd/reset", PasswordResetService.class),

    PHONE_UPDATE(new PhoneUpdateResponseParser(), "/phone/update", PasswordResetService.class),

    LOAN_CREATE(new LoanCreateResponseParser(), "/loan/create", LoanCreateService.class),
    LOAN_INVEST(new LoanInvestResponseParser(), "/loan/invest", LoanInvestService.class),
    LOAN_FAST_INVEST(new LoanInvestResponseParser(), "/loan/fastInvest", LoanInvestService.class),
    LOAN_CREDIT_INVEST(new LoanCreditInvestResponseParser(), "/loanCredit/invest", LoanCreditInvestService.class),

    LOAN_FULL(new LoanFullResponseParser(), "/loan/full", LoanFullService.class),
    LOAN_REPAY(new LoanRepayResponseParser(), "/loan/repay", LoanRepayService.class),
    LOAN_FAST_REPAY(new LoanRepayResponseParser(), "/loan/fastRepay", LoanRepayService.class),
    LOAN_CALLBACK(new LoanCallbackResponseParser(), "/loan/callback", null),

    QUERY_USER(new QueryUserResponseParser(), "/query/user", null),
    QUERY_LOAN(new QueryLoanResponseParser(), "/query/loan", null),
    QUERY_TRADE(new QueryTradeResponseParser(), "/query/trade", null),
    QUERY_LOG_ACCOUNT(new QueryLogAccountResponseParser(), "/query/logAccount", null),
    QUERY_LOG_LOAN_ACCOUNT(new QueryLogLoanAccountResponseParser(), "/query/logLoanAccount", null),
    QUERY_DOWNLOAD_LOG_FILES(new QueryDownloadLogFilesResponseParser(), "/query/downloadLogFiles", null),

    MERCHANT_TRANSFER(new MerchantTransferResponseParser(), "/merchant/merchantTransfer", null),

    ;

    private ResponseParserInterface parser;

    private String path;

    private Class<?> callbackHandlerClass;


    ApiType(ResponseParserInterface parser, String path, Class<?> callbackHandlerClass) {
        this.parser = parser;
        this.path = path;
        this.callbackHandlerClass = callbackHandlerClass;
    }

    public ResponseParserInterface getParser() {
        return parser;
    }

    public String getPath() {
        return path;
    }

    public Class<?> getCallbackHandlerClass() {
        return callbackHandlerClass;
    }


}
