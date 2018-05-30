package com.tuotiansudai.fudian.mapper;

import com.tuotiansudai.fudian.dto.request.BankResponseStatus;
import com.tuotiansudai.fudian.dto.response.LoanCallbackInvestItemContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                         @Param(value = "status") BankResponseStatus status);

    @Update("UPDATE loan_credit_invest SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateLoanCreditInvest(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_full SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now(), `status` = 'BANK_RESPONSE'  WHERE `order_no` = #{dto.content.orderNo} AND `status` = 'SENT'")
    int updateLoanFull(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_repay SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now(), `status` = 'BANK_RESPONSE' WHERE `order_no` = #{dto.content.orderNo} AND `status`= 'SENT'")
    int updateLoanRepay(@Param(value = "dto") ResponseDto responseDto);

    @Update({"UPDATE loan_repay SET `query_response_data` = #{dto.reqData}, `query_response_time` = now(), `status` = 'MANUAL_QUERIED' WHERE `order_no` = #{dto.content.orderNo} AND `status` = 'SENT'"})
    void updateLoanRepayQuery(@Param(value = "dto") ResponseDto responseDto);

    @Update("UPDATE loan_callback SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateLoanCallback(@Param(value = "dto") ResponseDto responseDto);

    @Update({"<script>",
            "<foreach collection='investItems' item='investItem' separator=';'>",
            "UPDATE loan_callback_invest ",
            "<set> ",
            "`ret_code` = #{investItem.retCode}, `ret_msg` = #{investItem.retMsg} ",
            "</set>",
            "<where>",
            "`order_no` = #{investItem.orderNo} ",
            "</where>",
            "</foreach>",
            "</script>"})
    void updateLoanCallbackInvestItems(@Param("investItems") List<LoanCallbackInvestItemContentDto> investItems);

    @Update("UPDATE merchant_transfer SET `notify_response_data` = #{dto.reqData}, `ret_code` = #{dto.retCode}, `ret_msg` = #{dto.retMsg}, `response_time` = now() WHERE `order_no` = #{dto.content.orderNo}")
    void updateMerchantTransfer(@Param(value = "dto") ResponseDto responseDto);
}
