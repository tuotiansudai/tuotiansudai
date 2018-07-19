package com.tuotiansudai.fudian.mapper.ump;

import com.tuotiansudai.fudian.ump.sync.response.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface InsertResponseMapper {

    @Insert("insert into project_transfer_response (request_id, sign_type, sign, mer_id, version, mer_date, mer_check_date,trade_no,ret_code,ret_msg, response_data,response_time) " +
            "values (#{requestId}, #{signType}, #{sign}, #{merId}, #{version}, #{merDate}, #{merCheckDate},#{tradeNo}, #{retCode}, #{retMsg}, #{responseData}, #{responseTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void invertProjectTransfer(ProjectTransferResponseModel model);

    @Insert("insert into coupon_repay_transfer_response (request_id, sign_type, sign, mer_Id, version, order_id, mer_date, trade_no, mer_check_date,ret_code, ret_msg, response_data, response_time)" +
            "values (#{requestId}, #{signType}, #{sign}, #{merId}, #{version}, #{orderId}, #{merDate}, #{tradeNo},#{merCheckDate}, #{retCode}, #{retMsg}, #{responseData}, #{responseTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertResponseCouponRepay(TransferResponseModel model);

    @Insert("insert into extra_rate_transfer_response (request_id, sign_type, sign, mer_Id, version, order_id, mer_date, trade_no, mer_check_date,ret_code, ret_msg, response_data, response_time)" +
            "values (#{requestId}, #{signType}, #{sign}, #{merId}, #{version}, #{orderId}, #{merDate}, #{tradeNo},#{merCheckDate}, #{retCode}, #{retMsg}, #{responseData}, #{responseTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertResponseExtraRate(TransferResponseModel model);

    @Insert("insert into transfer_response (request_id, sign_type, sign, mer_Id, version, order_id, mer_date, trade_no, mer_check_date,ret_code, ret_msg, response_data, response_time)" +
            "values (#{requestId}, #{signType}, #{sign}, #{merId}, #{version}, #{orderId}, #{merDate}, #{tradeNo},#{merCheckDate}, #{retCode}, #{retMsg}, #{responseData}, #{responseTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertResponseTransfer(TransferResponseModel model);

    @Insert("insert into user_search_response (request_id, sign_type, sign, mer_Id, version,plat_user_id, account_id, cust_name, identity_type, identity_code, contact_mobile, mail_addr, account_state, balance, card_id, gate_id, user_bind_agreement_list,ret_code, ret_msg, response_data, response_time)" +
            "values (#{requestId}, #{signType}, #{sign}, #{merId}, #{version},#{platUserId}, #{accountId}, #{custName}, #{identityType}, #{identityCode}, #{contactMobile}, #{mailAddr}, #{accountState}, #{balance}, #{cardId}, #{gateId}, #{userBindAgreementList},#{retCode}, #{retMsg}, #{responseData}, #{responseTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertResponseUmpUserSearch(UserSearchResponseModel model);

    @Insert("insert into project_account_search_response (request_id, sign_type, sign, mer_Id, version, project_id, project_account_id, project_account_state, project_state, balance, ret_code, ret_msg, response_data, response_time)" +
            "values (#{requestId}, #{signType}, #{sign}, #{merId}, #{version}, #{projectId}, #{projectAccountId}, #{projectAccountState}, #{projectState}, #{balance}, #{retCode}, #{retMsg}, #{responseData}, #{responseTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertResponseUmpProjectAccount(ProjectAccountSearchResponseModel responseModel);

    @Insert("insert into transfer_search_response (request_id, sign_type, sign, mer_Id, version,mer_check_date, mer_date, trade_no, busi_type, amount, orgi_amt, com_amt, com_amt_type, tran_state, sms_num,ret_code, ret_msg, response_data, response_time)" +
            "values (#{requestId}, #{signType}, #{sign}, #{merId}, #{version},#{merCheckDate}, #{merDate}, #{tradeNo}, #{busiType}, #{amount}, #{orgiAmt}, #{comAmt}, #{comAmtType}, #{tranState}, #{smsNum},#{retCode}, #{retMsg}, #{responseData}, #{responseTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertResponseUmpTransfer(TransferSearchResponseModel responseModel);

}
