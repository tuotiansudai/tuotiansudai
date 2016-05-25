package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.client.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.UUID;

public class MobileAppTokenProvider {

    private int tokenExpiredSeconds = 3600;

    private ObjectMapper objectMapper = new ObjectMapper();

    static Logger log = Logger.getLogger(MobileAppTokenProvider.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    public String refreshToken(String loginName, String oldToken) {
        if (!Strings.isNullOrEmpty(oldToken)) {
            redisWrapperClient.del(oldToken);
        }
        String tokenTemplate = "app-token:{0}:{1}";
        String token = MessageFormat.format(tokenTemplate, loginName, UUID.randomUUID().toString());
        redisWrapperClient.setex(token, this.tokenExpiredSeconds, loginName);
        log.debug(MessageFormat.format("[MobileAppTokenProvider][refreshToken] loginName: {0} oldToken: {1} newToken: {2}",
                loginName, oldToken, token));
        return token;
    }

    public void deleteToken(String token) {
        redisWrapperClient.del(token);
    }

    public String getToken(InputStream inputStream) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            BaseParamDto dto = objectMapper.readValue(inputStream, BaseParamDto.class);
            BaseParam baseParam = dto.getBaseParam();
            log.debug(MessageFormat.format("[MobileAppTokenProvider][getToken] baseParam:{userId:{0} phoneNum:{1} token:{2} " +
                            "platform:{3} appVersion:{4} osVersion:{5} deviceId:{6} deviceModel:{7} screenW:{8} screenH:{9} channel: {10}",
                    baseParam.getUserId(), baseParam.getPhoneNum(), baseParam.getToken(), baseParam.getPlatform(),
                    baseParam.getAppVersion(), baseParam.getOsVersion(), baseParam.getDeviceId(), baseParam.getDeviceModel(),
                    baseParam.getScreenW(), baseParam.getScreenH(), baseParam.getChannel()));
            return baseParam.getToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUserNameByToken(String token) {
        return redisWrapperClient.get(token);
    }

    public BaseResponseDto generateResponseDto(String token) {
        LoginResponseDataDto loginResponseDataDto = new LoginResponseDataDto();
        loginResponseDataDto.setToken(token);

        BaseResponseDto<LoginResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(loginResponseDataDto);

        return dto;
    }

    public BaseResponseDto generateResponseDto(ReturnMessage returnMessage) {
        LoginResponseDataDto loginResponseDataDto = new LoginResponseDataDto();
        loginResponseDataDto.setToken("");

        BaseResponseDto<LoginResponseDataDto> dto = new BaseResponseDto<>();
        dto.setData(loginResponseDataDto);

        dto.setCode(returnMessage.getCode());
        dto.setMessage(returnMessage.getMsg());
        return dto;
    }

    public void setTokenExpiredSeconds(int tokenExpiredSeconds) {
        this.tokenExpiredSeconds = tokenExpiredSeconds;
    }
}
