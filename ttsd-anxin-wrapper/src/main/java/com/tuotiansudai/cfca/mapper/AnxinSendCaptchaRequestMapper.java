package com.tuotiansudai.cfca.mapper;

import com.tuotiansudai.cfca.model.AnxinSendCaptchaRequestModel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinSendCaptchaRequestMapper {

    @Insert("insert into send_captcha_request (tx_time, user_id, project_code, is_send_voice, created_time) " +
            "values (#{txTime},#{userId},#{projectCode},#{isSendVoice},#{createdTime})")
    void create(AnxinSendCaptchaRequestModel anxinSendCaptchaRequestModel);
}
