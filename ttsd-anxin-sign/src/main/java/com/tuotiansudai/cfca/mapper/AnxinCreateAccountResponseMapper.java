package com.tuotiansudai.cfca.mapper;

import com.tuotiansudai.cfca.model.AnxinCreateAccountResponseModel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinCreateAccountResponseMapper {

    @Insert("insert into anxin_create_account_request (tx_time, person_name, ident_type_code, ident_no, email, mobile_phone, address, authentication_mode, not_send_pwd, created_time) " +
            "values (#{txTime},#{personName},#{identTypeCode},#{identNo},#{email},#{mobilePhone},#{address},#{authenticationMode},#{notSendPwd},#{createdTime})")
    void create(AnxinCreateAccountResponseModel anxinCreateAccountResponseModel);

}
