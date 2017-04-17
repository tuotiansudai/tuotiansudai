package com.tuotiansudai.cfca.mapper;

import com.tuotiansudai.cfca.model.AnxinSendCaptchaResponseModel;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxinSendCaptchaResponseMapper {

    @Insert("insert into send_captcha_response (tx_time, ret_code, ret_message, user_id, project_code, is_send_voice, created_time) " +
            "values (#{txTime},#{retCode},#{retMessage},#{userId},#{projectCode},#{isSendVoice},#{createdTime})")
    void create(AnxinSendCaptchaResponseModel anxinSendCaptchaResponseModel);
}
