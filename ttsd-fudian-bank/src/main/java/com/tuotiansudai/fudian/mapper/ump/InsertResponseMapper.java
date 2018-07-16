package com.tuotiansudai.fudian.mapper.ump;

import com.tuotiansudai.fudian.ump.sync.request.BaseSyncRequestModel;
import com.tuotiansudai.fudian.ump.sync.response.MerSendSmsPwdResponseModel;
import com.tuotiansudai.fudian.ump.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.fudian.ump.sync.response.MerRegisterPersonResponseModel;
import com.tuotiansudai.fudian.ump.sync.response.TransferResponseModel;
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

    @Insert("insert into mer_send_sms_pwd_response (request_id, sign_type, sign, mer_Id, version, ret_code, ret_msg, response_data, response_time)" +
            "values (#{requestId}, #{signType}, #{sign}, #{merId}, #{version}, #{retCode}, #{retMsg}, #{responseData}, #{responseTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertResponseResetPwd(MerSendSmsPwdResponseModel model);

    @Insert("insert into mer_register_person_response (request_id, sign_type, sign, mer_Id, version, user_id, account_id, reg_date, ret_code, ret_msg, response_data, response_time)" +
            "values (#{requestId}, #{signType}, #{sign}, #{merId}, #{version}, #{userId}, #{accountId}, #{regDate}, #{retCode}, #{retMsg}, #{responseData}, #{responseTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertResponseRegister(MerRegisterPersonResponseModel model);
}
