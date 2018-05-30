package com.tuotiansudai.fudian.mapper;

import com.tuotiansudai.fudian.dto.request.LoanInvestRequestDto;
import com.tuotiansudai.fudian.dto.request.LoanRepayRequestDto;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SelectRequestMapper {

    @Results(id = "loanInvestModelMap", value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "request_data", property = "requestData"),
            @Result(column = "merchant_no", property = "merchantNo"),
            @Result(column = "user_name", property = "userName"),
            @Result(column = "account_no", property = "accountNo"),
            @Result(column = "amount", property = "amount"),
            @Result(column = "award", property = "award"),
            @Result(column = "loan_tx_no", property = "loanTxNo"),
            @Result(column = "order_no", property = "orderNo"),
            @Result(column = "order_date", property = "orderDate"),
            @Result(column = "ext_mark", property = "extMark")
    })
    @Select("select id, request_data, merchant_no, user_name, account_no, amount, award, loan_tx_no, order_no, order_date, ext_mark " +
            "from loan_invest " +
            "where status = 'SENT' and DATE_ADD(request_time, INTERVAL 1 HOUR) > now() and DATE_ADD(request_time, INTERVAL 5 MINUTE) < now()")
    List<LoanInvestRequestDto> selectNoLoanInvestResponseInOneHour();

    @Select("SELECT id, request_data, merchant_no, user_name, account_no, loan_tx_no, capital, interest, loan_fee, order_no, order_date, ext_mark " +
            "FROM `loan_repay` " +
            "WHERE `status` = 'SENT' AND DATE_ADD(request_time, INTERVAL 1 HOUR) > now()")
    List<LoanRepayRequestDto> selectNoLoanRepayResponseInOneHour();
}
