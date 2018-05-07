package com.tuotiansudai.fudian.mapper;

import com.tuotiansudai.fudian.dto.response.ResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UpdateMapper {

    @Update("UPDATE register SET `response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateRegister(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE card_bind SET `response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateCardBind(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE cancel_card_bind SET `response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateCancelCardBind(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE recharge SET `response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateRecharge(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE withdraw SET `response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateWithdraw(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE authorization SET `response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateAuthorization(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE password_reset SET `response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updatePasswordReset(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE phone_update SET `response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updatePhoneUpdate(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_create SET `response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateLoanCreate(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_invest SET `response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateLoanInvest(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_credit_invest SET `response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateLoanCreditInvest(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_full SET SET `response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateLoanFull(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_repay SET `response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateLoanRepay(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_callback SET `response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateLoanCallback(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE merchant_transfer SET `response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateMerchantTransfer(@Param(value = "dto") ResponseDto responseDto);
}
