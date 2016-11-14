package com.tuotiansudai.cfca.mapper;

import com.tuotiansudai.cfca.model.AnxinCreateAccountResponseModel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinCreateAccountResponseMapper {

    @Insert("insert into create_account_response (tx_time, ret_code, ret_message, user_id, person_name, ident_type_code, ident_no, email, mobile_phone, address, authentication_mode, not_send_pwd, anxin_sign_email, anxin_sign_mobile_phone, created_time) " +
            "values (#{txTime},#{retCode},#{retMessage},#{userId},#{personName},#{identTypeCode},#{identNo},#{email},#{mobilePhone},#{address},#{authenticationMode},#{notSendPwd},#{anxinSignEmail},#{anxinSignMobilePhone},#{createdTime})")
    void create(AnxinCreateAccountResponseModel anxinCreateAccountResponseModel);

}
