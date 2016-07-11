package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.UUID;

@Service
public class MobileAppTokenProvider {

    private int tokenExpiredSeconds = 3600;

    private ObjectMapper objectMapper = new ObjectMapper();

    static Logger log = Logger.getLogger(MobileAppTokenProvider.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;
    @Autowired
    private UserMapper userMapper;

    @Value("${web.login.max.failed.times}")
    private int times;

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

    public BaseResponseDto generateResponseDto(ReturnMessage returnMessage,String loginName) {
        String message = returnMessage.getMsg();
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginName);
        if(userModel != null){
            String redisKey = MessageFormat.format("web:{0}:loginfailedtimes", userModel.getLoginName());

            if(ReturnMessage.LOGIN_FAILED.getCode().equals(returnMessage.getCode())){
                if(redisWrapperClient.exists(redisKey)){
                    int failTimes = Integer.parseInt(redisWrapperClient.get(redisKey));
                    message = MessageFormat.format(returnMessage.getMsg(),times - failTimes);
                }
            }else if(ReturnMessage.USER_IS_DISABLED.getCode().equals(returnMessage.getCode())){
                Long leftSeconds = redisWrapperClient.ttl(redisKey);
                message = MessageFormat.format(returnMessage.getMsg(),leftSeconds % 60 == 0? leftSeconds/60 : leftSeconds/60 + 1);
            }
        }

        LoginResponseDataDto loginResponseDataDto = new LoginResponseDataDto();
        loginResponseDataDto.setToken("");

        BaseResponseDto<LoginResponseDataDto> dto = new BaseResponseDto<>();
        dto.setData(loginResponseDataDto);

        dto.setCode(returnMessage.getCode());
        dto.setMessage(message);
        return dto;
    }


    public void setTokenExpiredSeconds(int tokenExpiredSeconds) {
        this.tokenExpiredSeconds = tokenExpiredSeconds;
    }
}
