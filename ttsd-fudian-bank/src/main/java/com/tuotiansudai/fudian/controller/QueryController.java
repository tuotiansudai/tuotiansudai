package com.tuotiansudai.fudian.controller;

import com.tuotiansudai.fudian.dto.request.QueryTradeType;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto> queryUser(@RequestParam(name = "userName") String userName,
                                                 @RequestParam(name = "accountNo") String accountNo) {
        logger.info("[Fudian] query user userName: {}, accountNo: {}", userName, accountNo);

        ResponseDto responseDto = queryUserService.query(userName, accountNo);

        return ResponseEntity.ok(responseDto);
    }

    @RequestMapping(path = "/loan", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto> queryLoan(@RequestParam(name = "loanTxNo") String loanTxNo) {
        logger.info("[Fudian] query loan loanTxNo: {}", loanTxNo);

        ResponseDto responseDto = queryLoanService.query(loanTxNo);

        return ResponseEntity.ok(responseDto);
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

        ResponseDto responseDto = queryLogAccountService.query(userName, accountNo, queryOrderDate);

        return ResponseEntity.ok(responseDto);
    }

    @RequestMapping(path = "/log-loan-account", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto> queryLogLoanAccount(@RequestParam(name = "loanAccNo") String loanAccNo,
                                                           @RequestParam(name = "loanTxNo") String loanTxNo) {
        logger.info("[Fudian] query log loan account loanAccNo: {}, loanTxNo: {}", loanAccNo, loanTxNo);

        ResponseDto responseDto = queryLogLoanAccountService.query(loanAccNo, loanTxNo);

        return ResponseEntity.ok(responseDto);
    }
}
