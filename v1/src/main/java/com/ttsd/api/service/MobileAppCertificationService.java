package com.ttsd.api.service;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.ttsd.api.dto.BaseParamDto;
import com.ttsd.api.dto.CertificationRequestDto;
import com.ttsd.api.dto.CertificationResponseDto;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * Created by tuotian on 15/7/27.
 */
public interface MobileAppCertificationService {
    /**
     * @function 平台登录用户实名认证接口
     * @param certificationRequestDto 移动端用户实名认证请求参数包装类
     * @return CertificationResponseDto
     */
    CertificationResponseDto validateUserCertificationInfo(CertificationRequestDto certificationRequestDto) throws IOException, UserNotFoundException;

    /**
     * @function 封装结果集
     * @param code 消息代码
     * @param message 消息描述
     * @param userRealName 用户真实姓名
     * @param idCardNumber 用户身份证号码
     * @return CertificationResponseDto
     */
    CertificationResponseDto assembleResult(String code,String message,String userRealName,String idCardNumber);
}
