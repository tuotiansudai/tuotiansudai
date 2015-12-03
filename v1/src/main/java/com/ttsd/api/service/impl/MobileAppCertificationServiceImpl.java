package com.ttsd.api.service.impl;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.esoft.umpay.user.service.impl.UmPayUserOperation;
import com.google.common.base.Strings;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppCertificationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Created by tuotian on 15/7/27.
 */
@Service(value = "MobileAppCertificationServiceImpl")
public class MobileAppCertificationServiceImpl implements MobileAppCertificationService{

    @Logger
    private Log log;

    @Autowired
    private UmPayUserOperation umPayUserOperation;

    @Autowired
    private UserService userService;

    /**
     * @function 移动端实名认证接口
     * @param certificationRequestDto 移动端用户实名认证请求参数包装类
     * @return CertificationResponseDto
     */
    @Override
    @Transactional
    public BaseResponseDto validateUserCertificationInfo(CertificationRequestDto certificationRequestDto) throws IOException, UserNotFoundException {
        BaseParam baseParam = certificationRequestDto.getBaseParam();//TODO 将此参数信息持久化到数据库中
        String userId = baseParam.getUserId();
        String userRealName = certificationRequestDto.getUserRealName();
        String idCardNumber = certificationRequestDto.getUserIdCardNumber();

        if (Strings.isNullOrEmpty(userId)){
            return assembleResult(ReturnMessage.USER_ID_IS_NULL.getCode(), ReturnMessage.USER_ID_IS_NULL.getMsg(), userRealName, idCardNumber);
        }
        if (Strings.isNullOrEmpty(userRealName)){
            return assembleResult(ReturnMessage.REAL_NAME_IS_NULL.getCode(), ReturnMessage.REAL_NAME_IS_NULL.getMsg(), userRealName, idCardNumber);
        }
        if (Strings.isNullOrEmpty(idCardNumber)){
            return assembleResult(ReturnMessage.ID_CARD_IS_NULL.getCode(), ReturnMessage.ID_CARD_IS_NULL.getMsg(), userRealName, idCardNumber);
        }
        User user = userService.getUserById(userId);
        if(StringUtils.isNotEmpty(user.getRealname()) && StringUtils.isNotEmpty(user.getIdCard())){
            return assembleResult(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg(), user.getRealname(), user.getIdCard());
        }
        if (userService.idCardIsExists(idCardNumber)){
            return assembleResult(ReturnMessage.ID_CARD_IS_EXIST.getCode(), ReturnMessage.ID_CARD_IS_EXIST.getMsg(), userRealName, idCardNumber);
        }
        if (user != null){
            user.setRealname(userRealName);
            user.setIdCard(idCardNumber);
            umPayUserOperation.createOperation(user, null);
            return assembleResult(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg(), userRealName, idCardNumber);
        }
        return null;
    }


    /**
     * @function 封装结果集
     * @param code 消息代码
     * @param message 消息描述
     * @param userRealName 用户真实姓名
     * @param idCardNumber 用户身份证号码
     * @return CertificationResponseDto
     */
    public BaseResponseDto assembleResult(String code, String message, String userRealName, String idCardNumber){
        CertificationResponseDataDto dataDto = new CertificationResponseDataDto();
        dataDto.setUserRealName(userRealName);
        dataDto.setUserIdCardNumber(idCardNumber);
        BaseResponseDto<CertificationResponseDataDto> dto = new BaseResponseDto();
        dto.setCode(code);
        dto.setMessage(message);
        dto.setData(dataDto);
        return dto;
    }

    /*************************************set方法**************************************/
    public void setLog(Log log) {
        this.log = log;
    }

    public void setUmPayUserOperation(UmPayUserOperation umPayUserOperation) {
        this.umPayUserOperation = umPayUserOperation;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
