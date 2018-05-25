package com.tuotiansudai.fudian.mapper;

import com.tuotiansudai.fudian.dto.request.LoanInvestStatus;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.service.LoanInvestService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UpdateMapper {

    @Update("UPDATE register SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateRegister(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE card_bind SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateCardBind(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE cancel_card_bind SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateCancelCardBind(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE recharge SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateRecharge(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE withdraw SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateWithdraw(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE authorization SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateAuthorization(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE password_reset SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updatePasswordReset(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE phone_update SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updatePhoneUpdate(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_create SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateLoanCreate(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_invest SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now(), `status` = #{status} WHERE `order_no` = #{dto.content.orderNo} AND `status` = 'SENT'")
    int updateLoanInvest(@Param(value = "dto") ResponseDto responseDto,
                         @Param(value = "status") LoanInvestStatus status);

    @Update("UPDATE loan_credit_invest SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateLoanCreditInvest(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_full SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateLoanFull(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_repay SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateLoanRepay(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_callback SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateLoanCallback(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE merchant_transfer SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateMerchantTransfer(@Param(value = "dto") ResponseDto responseDto);
}
