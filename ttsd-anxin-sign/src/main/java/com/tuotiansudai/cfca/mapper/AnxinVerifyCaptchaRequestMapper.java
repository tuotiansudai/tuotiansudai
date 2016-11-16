package com.tuotiansudai.cfca.mapper;

import com.tuotiansudai.cfca.model.AnxinVerifyCaptchaRequestModel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinVerifyCaptchaRequestMapper {

    @Insert("insert into verify_captcha_request (tx_time, user_id, project_code, check_code, created_time) " +
            "values (#{txTime},#{userId},#{projectCode},#{checkCode},#{createdTime})")
    void create(AnxinVerifyCaptchaRequestModel anxinVerifyCaptchaRequestModel);
}
