package com.tuotiansudai.fudian.mapper;

import com.tuotiansudai.fudian.dto.response.ResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ReturnUpdateMapper {

    @Update("UPDATE register SET `return_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo} and return_response_data is null")
    void updateRegister(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE card_bind SET `return_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo} and return_response_data is null")
    void updateCardBind(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE cancel_card_bind SET `return_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo} and return_response_data is null")
    void updateCancelCardBind(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE recharge SET `return_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo} and return_response_data is null")
    void updateRecharge(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE withdraw SET `return_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo} and return_response_data is null")
    void updateWithdraw(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE authorization SET `return_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo} and return_response_data is null")
    void updateAuthorization(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE password_reset SET `return_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo} and return_response_data is null")
    void updatePasswordReset(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE phone_update SET `return_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo} and return_response_data is null")
    void updatePhoneUpdate(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_invest SET `return_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo} and return_response_data is null")
    void updateLoanInvest(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_credit_invest SET `return_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo} and return_response_data is null")
    void updateLoanCreditInvest(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_repay SET `return_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo} and return_response_data is null")
    void updateLoanRepay(@Param(value = "dto") ResponseDto responseDto);

}
