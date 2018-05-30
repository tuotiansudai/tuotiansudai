package com.tuotiansudai.fudian.mapper;

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

    @Insert("INSERT INTO register(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `real_name`, `identity_type`, `identity_code`, `mobile_phone`, `request_time`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{realName}, #{identityType}, #{identityCode}, #{mobilePhone}, now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertRegister(RegisterRequestDto dto);

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

    @Insert("INSERT INTO authorization(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `user_name`, `account_no`, `business_type`, `request_time`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{userName}, #{accountNo}, #{businessType}, now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertAuthorization(AuthorizationRequestDto dto);

    @Insert("INSERT INTO password_reset(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `user_name`, `account_no`, `request_time`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{userName}, #{accountNo}, now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertPasswordReset(PasswordResetRequestDto dto);

    @Insert("INSERT INTO phone_update(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `user_name`, `account_no`, `new_phone`, `type`, `request_time`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{userName}, #{accountNo}, #{newPhone}, #{type}, now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertPhoneUpdate(PhoneUpdateRequestDto dto);

    @Insert("INSERT INTO loan_create(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `user_name`, `account_no`, `amount`, `loan_name`, `loan_type`, `request_time`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{userName}, #{accountNo}, #{amount}, #{loanName}, #{loanType}, now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertLoanCreate(LoanCreateRequestDto dto);

    @Insert("INSERT INTO loan_invest(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `user_name`, `account_no`, `amount`, `award`, `loan_tx_no`, `request_time`, `status`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{userName}, #{accountNo}, #{amount}, #{award}, #{loanTxNo}, now(), 'SENT')")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertLoanInvest(LoanInvestRequestDto dto);

    @Insert("INSERT INTO loan_credit_invest(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `user_name`, `account_no`, `credit_no`, `credit_no_amount`, `loan_tx_no`, `credit_amount`, `amount`, `credit_fee`, `credit_fee_type`, `invest_order_no`, `invest_order_date`, `ori_order_no`, `ori_order_date`, `repayed_amount`, `request_time`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{userName}, #{accountNo}, #{creditNo}, #{creditNoAmount}, #{loanTxNo}, #{creditAmount}, #{amount}, #{creditFee}, #{creditFeeType}, #{investOrderNo}, #{investOrderDate}, #{oriOrderNo}, #{oriOrderDate}, #{repayedAmount}, now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertLoanCreditInvest(LoanCreditInvestRequestDto dto);

    @Insert("INSERT INTO loan_full(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `user_name`, `account_no`, `loan_fee`, `loan_order_date`, `loan_order_no`, `loan_tx_no`, `expect_repay_time`, `request_time`, `status`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{userName}, #{accountNo}, #{loanFee}, #{loanOrderDate}, #{loanOrderNo}, #{loanTxNo}, #{expectRepayTime}, now(), 'SENT')")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertLoanFull(LoanFullRequestDto dto);

    @Insert("INSERT INTO loan_repay(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `user_name`, `account_no`, `loan_tx_no`, `capital`, `interest`, `loan_fee`, `request_time`, `status`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{userName}, #{accountNo}, #{loanTxNo}, #{capital}, #{interest}, #{loanFee}, now()), 'SENT'")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertLoanRepay(LoanRepayRequestDto dto);

    @Insert("INSERT INTO loan_callback(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `loan_tx_no`, `request_time`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{loanTxNo}, now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertLoanCallback(LoanCallbackRequestDto dto);

    @Insert({"<script>",
            "insert into loan_callback_invest(`loan_callback_id`, `capital`, `interest`, `interest_fee`, `rate_interest`, `invest_user_name`, `invest_account_no`, `invest_order_no`, `invest_order_date`, `order_no`, `order_date`) ",
            "values ",
            "<foreach collection='investItems' item='investItem' separator=','>",
            "(#{investItem.loanCallbackId}, #{investItem.capital}, #{investItem.interest}, #{investItem.interestFee}, #{investItem.rateInterest}, #{investItem.investUserName}, #{investItem.investAccountNo}, #{investItem.investOrderNo}, #{investItem.investOrderDate}, #{investItem.orderNo}, #{investItem.orderDate})",
            "</foreach>",
            "</script>"})
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertLoanCallbackInvestItems(@Param("investItems") List<LoanCallbackInvestItemRequestDto> investItems);

    @Insert("INSERT INTO merchant_transfer(`request_data`, `merchant_no`, `order_no`, `order_date`, `ext_mark`, `return_url`, `notify_url`, `user_name`, `account_no`, `amount`, `request_time`) " +
            "VALUES(#{requestData}, #{merchantNo}, #{orderNo}, #{orderDate}, #{extMark}, #{returnUrl}, #{notifyUrl}, #{userName}, #{accountNo}, #{amount}, now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertMerchantTransfer(MerchantTransferRequestDto dto);


}
