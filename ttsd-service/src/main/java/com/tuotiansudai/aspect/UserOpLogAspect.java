package com.tuotiansudai.aspect;

import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.UserOpLogMapper;
import com.tuotiansudai.repository.model.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Aspect
@Component
public class UserOpLogAspect {

    @Autowired
    private UserOpLogMapper userOpLogMapper;

    /**
     * 修改密码
     *
     * @param joinPoint
     * @param returnValue
     */
    //String loginName, String originalPassword, String newPassword, String ip, String platform, String deviceId
    @AfterReturning(value = "execution(* com.tuotiansudai.service.UserService.changePassword(..))", returning = "returnValue")
    public void afterChangePassword(JoinPoint joinPoint, Object returnValue){
        Object[] args = joinPoint.getArgs();
        String loginName = (String) args[0];
        String ip = (String) args[3];
        String platform = (String) args[4];
        String deviceId = (String) args[5];

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(loginName);
        logModel.setIp(ip);
        logModel.setDeviceId(deviceId);
        logModel.setSource(Source.valueOf(platform.toUpperCase(Locale.ENGLISH)));
        logModel.setOpType(UserOpType.CHANGE_PASSWORD);
        logModel.setDescription((Boolean)returnValue ? "成功" : "失败");

        userOpLogMapper.create(logModel);
    }

    /**
     * 绑定邮箱，修改邮箱
     *
     * @param joinPoint
     * @param returnValue
     */
    // String loginName, String uuid, String ip, String platform, String deviceId
    @AfterReturning(value = "execution(* com.tuotiansudai.service.BindEmailService.verifyEmail(..))", returning = "returnValue")
    public void afterBindEmail(JoinPoint joinPoint, Object returnValue){
        Object[] args = joinPoint.getArgs();
        String loginName = (String) args[0];
        String ip = (String) args[2];
        String platform = (String) args[3];
        String deviceId = (String) args[4];

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(loginName);
        logModel.setIp(ip);
        logModel.setDeviceId(deviceId);
        logModel.setSource(Source.valueOf(platform.toUpperCase(Locale.ENGLISH)));
        logModel.setOpType(UserOpType.BIND_EMAIL);
        logModel.setDescription(returnValue!=null ? "成功" : "失败");

        userOpLogMapper.create(logModel);
    }

    /**
     * 绑卡
     *
     * @param joinPoint
     * @param returnValue
     */
    @AfterReturning(value = "execution(* com.tuotiansudai.service.BindBankCardService.bindBankCard(..))", returning = "returnValue")
    public void afterBindBankCard(JoinPoint joinPoint, Object returnValue){
        Object[] args = joinPoint.getArgs();
        BindBankCardDto dto = (BindBankCardDto) args[0];

        String loginName = dto.getLoginName();
        String deviceId = dto.getDeviceId();
        Source source = dto.getSource();
        String ip = dto.getIp();

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(loginName);
        logModel.setIp(ip);
        logModel.setDeviceId(deviceId);
        logModel.setSource(source);
        logModel.setOpType(UserOpType.BIND_CARD);

        BaseDto<PayFormDataDto> ret = (BaseDto<PayFormDataDto>)returnValue;
        logModel.setDescription(ret.getData().getMessage());

        userOpLogMapper.create(logModel);
    }

    /**
     * 换卡
     *
     * @param joinPoint
     * @param returnValue
     */
    @AfterReturning(value = "execution(* com.tuotiansudai.service.BindBankCardService.replaceBankCard(..))", returning = "returnValue")
    public void afterReplaceBankCard(JoinPoint joinPoint, Object returnValue){
        Object[] args = joinPoint.getArgs();
        BindBankCardDto dto = (BindBankCardDto) args[0];

        String loginName = dto.getLoginName();
        String deviceId = dto.getDeviceId();
        Source source = dto.getSource();
        String ip = dto.getIp();

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(loginName);
        logModel.setIp(ip);
        logModel.setDeviceId(deviceId);
        logModel.setSource(source);
        logModel.setOpType(UserOpType.REPLACE_CARD);

        BaseDto<PayFormDataDto> ret = (BaseDto<PayFormDataDto>)returnValue;
        logModel.setDescription(ret.getData().getMessage());

        userOpLogMapper.create(logModel);
    }

    /**
     * 快捷支付 免密支付 协议开通
     *
     * @param joinPoint
     * @param returnValue
     */
    @AfterReturning(value = "execution(* *.paywrapper.service.AgreementService.agreement(..))", returning = "returnValue")
    public void afterAgreement(JoinPoint joinPoint, Object returnValue){
        Object[] args = joinPoint.getArgs();
        AgreementDto dto = (AgreementDto) args[0];

        UserOpType opType;

        if (dto.isAutoInvest() || dto.isNoPasswordInvest()) {
            opType =  UserOpType.NO_PASSWORD_AGREEMENT; // 开通免密支付协议
        } else if (dto.isFastPay()) {
            opType =  UserOpType.FAST_PAY_AGREEMENT; // 开通快捷支付协议
        } else {
            return;
        }

        String loginName = dto.getLoginName();
        String deviceId = dto.getDeviceId();
        Source source = dto.getSource();
        String ip = dto.getIp();

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(loginName);
        logModel.setIp(ip);
        logModel.setDeviceId(deviceId);
        logModel.setSource(source);
        logModel.setOpType(opType);

        BaseDto<PayFormDataDto> ret = (BaseDto<PayFormDataDto>)returnValue;
        logModel.setDescription(ret.getData().getMessage());

        userOpLogMapper.create(logModel);

        // 如果开通的是“免密支付”协议，那么默认会自动开通“免密投资”功能。
        if (opType ==  UserOpType.NO_PASSWORD_AGREEMENT) {
            UserOpLogModel noPasswordLogModel = new UserOpLogModel();
            noPasswordLogModel.setLoginName(loginName);
            noPasswordLogModel.setIp(ip);
            noPasswordLogModel.setDeviceId(deviceId);
            noPasswordLogModel.setSource(source);
            noPasswordLogModel.setOpType(UserOpType.INVEST_NO_PASSWORD);
            noPasswordLogModel.setDescription(ret.getData().getMessage());
            userOpLogMapper.create(noPasswordLogModel);
        }
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.service.InvestService.turnOnAutoInvest(..))")
    public void afterTurnOnAutoInvest(JoinPoint joinPoint) {
        AutoInvestPlanModel model = (AutoInvestPlanModel)joinPoint.getArgs()[0];

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(model.getLoginName());
        logModel.setIp(model.getIp());
        logModel.setDeviceId("");
        logModel.setSource(Source.WEB);
        logModel.setOpType(UserOpType.AUTO_INVEST);
        logModel.setDescription("Turn On.");

        userOpLogMapper.create(logModel);
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.service.InvestService.turnOffAutoInvest(..))")
    public void afterTurnOffAutoInvest(JoinPoint joinPoint) {
        String loginName = (String)joinPoint.getArgs()[0];
        String ip = (String)joinPoint.getArgs()[1];

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(loginName);
        logModel.setIp(ip);
        logModel.setDeviceId("");
        logModel.setSource(Source.WEB);
        logModel.setOpType(UserOpType.AUTO_INVEST);
        logModel.setDescription("Turn Off.");

        userOpLogMapper.create(logModel);
    }

    @AfterReturning(value = "execution(* com.tuotiansudai.service.InvestService.switchNoPasswordInvest(..))")
    public void afterSwitchNoPasswordInvest(JoinPoint joinPoint) {
        String loginName = (String) joinPoint.getArgs()[0];
        boolean isTurnOn = (boolean) joinPoint.getArgs()[1];
        String ip = (String) joinPoint.getArgs()[2];

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(loginName);
        logModel.setIp(ip);
        logModel.setDeviceId("");
        logModel.setSource(Source.WEB);
        logModel.setOpType(UserOpType.INVEST_NO_PASSWORD);
        logModel.setDescription(isTurnOn ? "Turn On" : "Turn Off");

        userOpLogMapper.create(logModel);

    }
}
