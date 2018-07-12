package com.tuotiansudai.fudian.mapper.ump;

import com.tuotiansudai.fudian.dto.request.*;
import com.tuotiansudai.fudian.dto.umpRequest.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface InsertRequestMapper {

    @Insert("insert into ptp_mer_bind_card_request (service, sign_type, sign, charset, mer_Id, version, ret_url,notify_url, order_id, mer_date,user_id, card_id, account_name, identity_type, identity_code,is_open_fastPayment,request_time,request_url, request_data, status)" +
            "values (#{service}, #{signType}, #{sign}, #{charset}, #{merId}, #{version},#{retUrl}, #{notifyUrl}, #{orderId}, #{merDate}, #{userId}, #{cardId}, #{accountName}, #{identityType},#{identityCode},#{isOpenFastPayment},#{requestTime}, #{requestUrl}, #{requestData}, #{status})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertCardBind(BindCardRequestModel model);

    @Insert("insert into ptp_mer_replace_card_request (service, sign_type, sign, charset, mer_Id, version, ret_url,notify_url, order_id, mer_date,user_id, card_id, account_name, identity_type, identity_code,request_time,request_url, request_data, status)" +
            "values (#{service}, #{signType}, #{sign}, #{charset}, #{merId}, #{version},#{retUrl}, #{notifyUrl}, #{orderId}, #{merDate}, #{userId}, #{cardId}, #{accountName}, #{identityType},#{identityCode},#{requestTime}, #{requestUrl}, #{requestData}, #{status})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertReplaceCardBind(ReplaceCardRequestModel model);

    @Insert("insert into mer_recharge_person_request (service, sign_type, sign, charset, mer_Id, version, ret_url, notify_url, order_id, mer_date, pay_type, user_id, amount, gate_id, com_amt_type, request_time, request_url, request_data) " +
            "values (#{service}, #{signType}, #{sign}, #{charset}, #{merId}, #{version}, #{retUrl}, #{notifyUrl}, #{orderId}, #{merDate}, #{payType}, #{userId}, #{amount}, #{gateId}, #{comAmtType}, #{requestTime}, #{requestUrl}, #{requestData})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertRecharge(RechargeRequestModel model);

    @Insert("insert into cust_withdrawals_request (service, sign_type, sign, charset, mer_Id, version, ret_url, notify_url, apply_notify_flag,  order_id, mer_date, user_id, amount, com_amt_type, request_time, request_url, request_data)" +
            "values (#{service}, #{signType}, #{sign}, #{charset}, #{merId}, #{version}, #{retUrl}, #{notifyUrl}, #{applyNotifyFlag}, #{orderId}, #{merDate}, #{userId}, #{amount}, #{comAmtType}, #{requestTime}, #{requestUrl}, #{requestData})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertWithdraw(WithdrawRequestModel model);

    @Insert("insert into project_transfer_request (service, sign_type, sign, charset, mer_Id, version, ret_url, notify_url, order_id, mer_date, project_id, serv_type, trans_action, partic_type, partic_acc_type, partic_user_id, amount, request_time, request_url, request_data, status) " +
            "values (#{service}, #{signType}, #{sign}, #{charset}, #{merId}, #{version}, #{retUrl}, #{notifyUrl}, #{orderId}, #{merDate}, #{projectId}, #{servType}, #{transAction}, #{particType}, #{particAccType}, #{userId}, #{amount}, #{requestTime}, #{requestUrl}, #{requestData}, #{status})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertLoanRepay(LoanRepayRequestModel model);

}
