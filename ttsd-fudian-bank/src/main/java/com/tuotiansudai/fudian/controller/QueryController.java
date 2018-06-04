package com.tuotiansudai.fudian.controller;

import com.tuotiansudai.fudian.dto.request.QueryTradeType;
import com.tuotiansudai.fudian.dto.response.QueryLoanContentDto;
import com.tuotiansudai.fudian.dto.response.QueryUserContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.message.BankQueryLoanMessage;
import com.tuotiansudai.fudian.message.BankQueryUserMessage;
import com.tuotiansudai.fudian.service.*;
import com.tuotiansudai.fudian.util.AmountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/query")
public class QueryController {

    private static Logger logger = LoggerFactory.getLogger(QueryController.class);

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

    @RequestMapping(path = "/trade", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto> queryTrade(@RequestParam(name = "orderNo") String orderNo,
                                                  @RequestParam(name = "orderDate") String orderDate,
                                                  @RequestParam(name = "queryType") QueryTradeType queryType) {
        logger.info("[Fudian] query trade orderNo: {}, orderDate: {}, queryType: {}", orderNo, orderDate, queryType);

        ResponseDto responseDto = queryTradeService.query(orderNo, orderDate, queryType);

        return ResponseEntity.ok(responseDto);
    }

    @RequestMapping(path = "/log-account", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto> queryLogAccount(@RequestParam(name = "userName") String userName,
                                                       @RequestParam(name = "accountNo") String accountNo,
                                                       @RequestParam(name = "queryOrderDate") String queryOrderDate) {
        logger.info("[Fudian] query log account userName: {}, accountNo: {}, queryOrderDate: {}", userName, accountNo, queryOrderDate);

        ResponseDto responseDto = queryLogAccountService.query(userName, accountNo, queryOrderDate, null, null);

        return ResponseEntity.ok(responseDto);
    }

    @RequestMapping(path = "/log-loan-account", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto> queryLogLoanAccount(@RequestParam(name = "loanAccNo") String loanAccNo,
                                                           @RequestParam(name = "loanTxNo") String loanTxNo) {
        logger.info("[Fudian] query log loan account loanAccNo: {}, loanTxNo: {}", loanAccNo, loanTxNo);

        ResponseDto responseDto = queryLogLoanAccountService.query(null, null);

        return ResponseEntity.ok(responseDto);
    }
}
