package com.tuotiansudai.fudian.mapper.ump;

import com.tuotiansudai.fudian.dto.request.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface InsertMapper {

    @Insert("INSERT INTO card_bind(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `user_name`, `account_no`, `request_time`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{userName}, #{accountNo}, now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertCardBind(CardBindRequestDto dto);

    @Insert("INSERT INTO cancel_card_bind(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `user_name`, `account_no`, `request_time`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{userName}, #{accountNo}, now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertCancelCardBind(CancelCardBindRequestDto dto);

    @Insert("INSERT INTO recharge(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `user_name`, `account_no`, `amount`, `fee`, `pay_type`, `request_time`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{userName}, #{accountNo}, #{amount}, #{fee}, #{payType}, now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertRecharge(RechargeRequestDto dto);

    @Insert("INSERT INTO withdraw(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `user_name`, `account_no`, `amount`, `fee`, `request_time`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{userName}, #{accountNo}, #{amount}, #{fee}, now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertWithdraw(WithdrawRequestDto dto);

    @Insert("INSERT INTO loan_repay(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `user_name`, `account_no`, `loan_tx_no`, `capital`, `interest`, `loan_fee`, `request_time`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{userName}, #{accountNo}, #{loanTxNo}, #{capital}, #{interest}, #{loanFee}, now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertLoanRepay(LoanRepayRequestDto dto);

}
