package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.CertificationRequestDto;

import java.io.IOException;

public interface MobileAppCertificationService {
    /**
     * @function 平台登录用户实名认证接口
     * @param certificationRequestDto 移动端用户实名认证请求参数包装类
     * @return CertificationResponseDto
     */
    BaseResponseDto validateUserCertificationInfo(CertificationRequestDto certificationRequestDto);

}
