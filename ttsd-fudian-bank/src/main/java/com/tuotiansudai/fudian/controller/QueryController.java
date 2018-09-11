package com.tuotiansudai.fudian.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.fudian.dto.QueryTradeType;
import com.tuotiansudai.fudian.dto.response.*;
import com.tuotiansudai.fudian.message.*;
import com.tuotiansudai.fudian.service.*;
import com.tuotiansudai.fudian.util.AmountUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/query")
public class QueryController {

    private final static Logger logger = LoggerFactory.getLogger(QueryController.class);

    private final QueryUserService queryUserService;

    private final QueryLoanService queryLoanService;

    private final QueryTradeService queryTradeService;

    private final QueryLogAccountService queryLogAccountService;

    private final QueryLogLoanAccountService queryLogLoanAccountService;

    @Autowired
    public QueryController(QueryUserService queryUserService, QueryLoanService queryLoanService, QueryTradeService queryTradeService, QueryLogAccountService queryLogAccountService, QueryLogLoanAccountService queryLogLoanAccountService) {
        this.queryUserService = queryUserService;
        this.queryLoanService = queryLoanService;
        this.queryTradeService = queryTradeService;
        this.queryLogAccountService = queryLogAccountService;
        this.queryLogLoanAccountService = queryLogLoanAccountService;
    }

    @RequestMapping(path = "/user/user-name/{userName}/account-no/{accountNo}", method = RequestMethod.GET)
    public ResponseEntity<BankQueryUserMessage> queryUser(@PathVariable(name = "userName") String userName,
                                                          @PathVariable(name = "accountNo") String accountNo) {
        ResponseDto<QueryUserContentDto> responseDto = queryUserService.query(userName, accountNo);

        if (responseDto == null || !responseDto.isSuccess()) {
            return ResponseEntity.ok(new BankQueryUserMessage(false,
                    responseDto != null ? responseDto.getRetMsg() : "查询失败"));
        }
        QueryUserContentDto content = responseDto.getContent();
        return ResponseEntity.ok(new BankQueryUserMessage(
                userName,
                accountNo,
                content.getAuthorization(),
                AmountUtils.toCent(content.getBalance()),
                AmountUtils.toCent(content.getWithdrawBalance()),
                AmountUtils.toCent(content.getFreezeBalance()),
                content.getIdentityCode(),
                content.getStatus()));
    }

    @RequestMapping(path = "/loan/loan-tx-no/{loanTxNo}/loan-acc-no/{loanAccNo}", method = RequestMethod.GET)
    public ResponseEntity<BankQueryLoanMessage> queryLoan(@PathVariable(name = "loanTxNo") String loanTxNo,
                                                          @PathVariable(name = "loanAccNo") String loanAccNo) {
        ResponseDto<QueryLoanContentDto> responseDto = queryLoanService.query(loanTxNo);

        if (responseDto == null || !responseDto.isSuccess()) {
            return ResponseEntity.ok(new BankQueryLoanMessage(false, responseDto != null ? responseDto.getRetMsg() : "查询失败"));
        }

        return ResponseEntity.ok(new BankQueryLoanMessage(loanTxNo,
                loanAccNo,
                AmountUtils.toCent(responseDto.getContent().getAmount()),
                AmountUtils.toCent(responseDto.getContent().getBalance()),
                responseDto.getContent().getStatus()));
    }

    @RequestMapping(path = "/trade/order-no/{orderNo}/order-date/{orderDate}/query-type/{queryType}", method = RequestMethod.GET)
    public ResponseEntity<BankQueryTradeMessage> queryTrade(@PathVariable(name = "orderNo") String orderNo,
                                                            @PathVariable(name = "orderDate") String orderDate,
                                                            @PathVariable(name = "queryType") QueryTradeType queryTradeType) {

        ResponseDto<QueryTradeContentDto> responseDto = queryTradeService.query(orderNo, orderDate, queryTradeType);

        if (responseDto == null || !responseDto.isSuccess()) {
            return ResponseEntity.ok(new BankQueryTradeMessage(false, responseDto != null ? responseDto.getRetMsg() : "查询失败"));
        }

        return ResponseEntity.ok(new BankQueryTradeMessage(orderNo, orderDate, queryTradeType, responseDto.getContent().getQueryState()));
    }

    @RequestMapping(path = "/log-account/user-name/{userName}/account-no/{accountNo}/query-order-date-start/{queryOrderDateStart}/query-order-date-end/{queryOrderDateEnd}", method = RequestMethod.GET)
    public ResponseEntity<BankQueryLogAccountMessage> queryLogAccount(@PathVariable(name = "userName") String userName,
                                                                      @PathVariable(name = "accountNo") String accountNo,
                                                                      @PathVariable(name = "queryOrderDateStart") @DateTimeFormat(pattern = "yyyyMMdd") Date queryOrderDateStart,
                                                                      @PathVariable(name = "queryOrderDateEnd") @DateTimeFormat(pattern = "yyyyMMdd") Date queryOrderDateEnd) {
        if (queryOrderDateStart.after(queryOrderDateEnd)) {
            return ResponseEntity.ok(new BankQueryLogAccountMessage(false, "查询时间不正确"));
        }

        List<ResponseDto<QueryLogAccountContentDto>> responseList = queryLogAccountService.query(userName, accountNo, queryOrderDateStart, queryOrderDateEnd);

        if (responseList == null) {
            return ResponseEntity.ok(new BankQueryLogAccountMessage(false, "查询失败"));
        }

        List<BankQueryLogAccountItemMessage> items = Lists.newArrayList();
        for (ResponseDto<QueryLogAccountContentDto> responseDto : responseList) {
            List<BankQueryLogAccountItemMessage> subItems = responseDto.getContent().getAccountLogList().stream().map(log -> new BankQueryLogAccountItemMessage(
                    log.getAmount(), log.getBalance(), log.getFreezeBalance(), log.getCreateTime(), log.getOrderNo(), log.getOrderDate(), log.getRemark(), log.getToUserName()
            )).collect(Collectors.toList());
            items.addAll(subItems);
        }
        return ResponseEntity.ok(new BankQueryLogAccountMessage(userName, accountNo, new DateTime(queryOrderDateStart).toString("yyyyMMdd"), new DateTime(queryOrderDateEnd).toString("yyyyMMdd"), items));
    }

    @RequestMapping(path = "/log-loan-account/loan-tx-no/{loanTxNo}/loan-acc-no/{loanAccNo}", method = RequestMethod.GET)
    public ResponseEntity<BankQueryLogLoanAccountMessage> queryLogLoanAccount(@PathVariable(name = "loanTxNo") String loanTxNo,
                                                                              @PathVariable(name = "loanAccNo") String loanAccNo) {
        ResponseDto<QueryLogLoanAccountContentDto> responseDto = queryLogLoanAccountService.query(loanTxNo, loanAccNo);

        if (responseDto == null) {
            return ResponseEntity.ok(new BankQueryLogLoanAccountMessage(false, "查询失败"));
        }

        return ResponseEntity.ok(new BankQueryLogLoanAccountMessage(loanTxNo,
                loanAccNo,
                responseDto.getContent().getLoanAccountLogList().stream().map(item -> new BankQueryLogLoanAccountItemMessage(item.getAmount(),
                        item.getBalance(),
                        item.getFreezeBalance(),
                        item.getCreateTime(),
                        item.getOrderNo(),
                        item.getOrderDate(),
                        item.getRemark(),
                        item.getToUserName())).collect(Collectors.toList())));
    }
}
