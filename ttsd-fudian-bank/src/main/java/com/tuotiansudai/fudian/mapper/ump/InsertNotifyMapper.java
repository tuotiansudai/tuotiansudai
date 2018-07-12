package com.tuotiansudai.fudian.mapper.ump;

import com.tuotiansudai.fudian.dto.request.*;
import com.tuotiansudai.fudian.dto.umpResponse.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface InsertNotifyMapper {

    @Insert("insert into mer_bind_card_notify_request (service, sign_type, sign, mer_id, version, user_id, order_id, gate_id, last_four_cardid, user_bind_agreement_list, mer_date, ret_code, request_time, response_time, request_data, response_data)" +
            "values (#{service}, #{signType}, #{sign}, #{merId}, #{version}, #{userId}, #{orderId}, #{gateId}, #{lastFourCardid}, #{userBindAgreementList}, #{merDate}, #{retCode}, #{requestTime}, #{responseTime}, #{requestData}, #{responseData})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertNotifyCardBind(BindCardNotifyModel model);

    @Insert("insert into mer_bind_apply_card_notify_request (service, sign_type, sign, mer_id, version, user_id, order_id, mer_date,ret_code,ret_msg, request_time, response_time, request_data, response_data)" +
            "values (#{service}, #{signType}, #{sign}, #{merId}, #{version},#{userId}, #{orderId}, #{merDate}, #{retCode},#{retMsg}, #{requestTime}, #{responseTime}, #{requestData}, #{responseData})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertApplyNotifyCardBind(BindCardApplyNotifyModel model);

    @Insert("insert into recharge_notify_request (service, sign_type, sign, mer_id, version, order_id, mer_date, trade_no, mer_check_date, balance, com_amt, com_amt_type, ret_code, ret_msg, request_time, response_time, request_data, response_data)" +
            "values (#{service}, #{signType}, #{sign}, #{merId}, #{version}, #{orderId}, #{merDate}, #{tradeNo}, #{merCheckDate}, #{balance}, #{comAmt}, #{comAmtType}, #{retCode}, #{retMsg}, #{requestTime}, #{responseTime}, #{requestData}, #{responseData})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertNotifyRecharge(RechargeNotifyModel model);

    @Insert("insert into cust_withdrawals_request (service, sign_type, sign, charset, mer_Id, version, ret_url, notify_url, apply_notify_flag,  order_id, mer_date, user_id, amount, com_amt_type, request_time, request_url, request_data)" +
            "values (#{service}, #{signType}, #{sign}, #{charset}, #{merId}, #{version}, #{retUrl}, #{notifyUrl}, #{applyNotifyFlag}, #{orderId}, #{merDate}, #{userId}, #{amount}, #{comAmtType}, #{requestTime}, #{requestUrl}, #{requestData})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertNotifyWithdraw(WithdrawNotifyModel model);

    @Insert("insert into withdraw_apply_notify_request (service, sign_type, sign, mer_id, version, order_id, mer_date, trade_no, amount, com_amt, com_amt_type, ret_code, ret_msg, request_time, response_time, request_data, response_data)" +
            "values (#{service}, #{signType}, #{sign}, #{merId}, #{version}, #{orderId}, #{merDate}, #{tradeNo}, #{amount}, #{comAmt}, #{comAmtType}, #{retCode}, #{retMsg}, #{requestTime}, #{responseTime}, #{requestData}, #{responseData})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertApplyNotifyWithdraw(WithdrawApplyNotifyModel model);

    @Insert("insert into project_transfer_notify_request (service, sign_type, sign, mer_id, version, order_id, mer_date, trade_no, mer_check_date, ret_code, ret_msg, request_time, response_time, request_data, response_data)" +
            "values (#{service}, #{signType}, #{sign}, #{merId}, #{version}, #{orderId}, #{merDate}, #{tradeNo}, #{merCheckDate}, #{retCode}, #{retMsg}, #{requestTime}, #{responseTime}, #{requestData}, #{responseData})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertNotifyLoanRepay(LoanRepayNotifyModel model);

}
