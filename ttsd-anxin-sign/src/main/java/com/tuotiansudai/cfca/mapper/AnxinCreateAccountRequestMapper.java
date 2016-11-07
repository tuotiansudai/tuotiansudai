package com.tuotiansudai.cfca.mapper;

import com.tuotiansudai.cfca.model.AnxinCreateAccountRequestModel;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Repository
@CacheNamespace(implementation = com.tuotiansudai.cache.MybatisRedisCache.class)
public interface AnxinCreateAccountRequestMapper {

    @Insert("insert into anxin_create_account_request (tx_time, retCode, retMessage, userId, person_name, ident_type_code, ident_no, email, mobile_phone, address, authentication_mode, not_send_pwd, anxinSignEmail, anxinSignMobilePhone, created_time) " +
            "values (#{txTime},#{retCode},#{retMessage},#{userId},#{personName},#{identTypeCode},#{identNo},#{email},#{mobilePhone},#{address},#{authenticationMode},#{notSendPwd},#{anxinSignEmail},#{anxinSignMobilePhone},#{createdTime})")
    @Options(useGeneratedKeys = true)
    void create(AnxinCreateAccountRequestModel anxinCreateAccountRequestModel);
}
