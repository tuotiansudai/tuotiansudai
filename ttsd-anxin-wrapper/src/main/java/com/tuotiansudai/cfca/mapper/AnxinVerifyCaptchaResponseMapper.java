package com.tuotiansudai.cfca.mapper;

import com.tuotiansudai.cfca.model.AnxinVerifyCaptchaResponseModel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinVerifyCaptchaResponseMapper {

    @Insert("insert into verify_captcha_response (tx_time, ret_code, ret_message, user_id, project_code, check_code, created_time) " +
            "values (#{txTime},#{retCode},#{retMessage},#{userId},#{projectCode},#{checkCode},#{createdTime})")
    void create(AnxinVerifyCaptchaResponseModel anxinVerifyCaptchaResponseModel);
}
