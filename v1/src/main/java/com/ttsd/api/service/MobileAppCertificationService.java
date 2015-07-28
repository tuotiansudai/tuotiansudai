package com.ttsd.api.service;

import com.ttsd.api.dto.BaseParamDto;
import com.ttsd.api.dto.CertificationRequestDto;
import com.ttsd.api.dto.CertificationResponseDto;

import javax.annotation.Resource;
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
    CertificationResponseDto validateUserCertificationInfo(CertificationRequestDto certificationRequestDto);

}
