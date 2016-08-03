package com.tuotiansudai.aspect;

import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.repository.mapper.UserOpLogMapper;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserOpLogModel;
import com.tuotiansudai.repository.model.UserOpType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
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
    public void afterChangePassword(JoinPoint joinPoint, Object returnValue) {
        Object[] args = joinPoint.getArgs();
        String loginName = (String) args[0];
        String ip = (String) args[3];
        String platform = (String) args[4];
        String deviceId = (String) args[5];

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(loginName);
        logModel.setIp(ip);
        logModel.setDeviceId(deviceId);
        logModel.setSource(platform == null ? null : Source.valueOf(platform.toUpperCase(Locale.ENGLISH)));
        logModel.setOpType(UserOpType.CHANGE_PASSWORD);
        logModel.setCreatedTime(new Date());
        logModel.setDescription((Boolean) returnValue ? "Success" : "Fail");

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
    public void afterBindEmail(JoinPoint joinPoint, Object returnValue) {
        Object[] args = joinPoint.getArgs();
        String loginName = (String) args[0];
        String ip = (String) args[2];
        String platform = (String) args[3];
        String deviceId = (String) args[4];

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(loginName);
        logModel.setIp(ip);
        logModel.setDeviceId(deviceId);
        logModel.setSource(platform == null ? null : Source.valueOf(platform.toUpperCase(Locale.ENGLISH)));
        logModel.setOpType(UserOpType.BIND_CHANGE_EMAIL);
        logModel.setCreatedTime(new Date());
        logModel.setDescription(returnValue != null ? "Success, Email: " + returnValue : "Fail");

        userOpLogMapper.create(logModel);
    }

    /**
     * 绑卡
     *
     * @param joinPoint
     * @param returnValue
     */
    @AfterReturning(value = "execution(* com.tuotiansudai.service.BindBankCardService.bindBankCard(..))", returning = "returnValue")
    public void afterBindBankCard(JoinPoint joinPoint, Object returnValue) {
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
        logModel.setCreatedTime(new Date());

        userOpLogMapper.create(logModel);
    }

    /**
     * 换卡
     *
     * @param joinPoint
     * @param returnValue
     */
    @AfterReturning(value = "execution(* com.tuotiansudai.service.BindBankCardService.replaceBankCard(..))", returning = "returnValue")
    public void afterReplaceBankCard(JoinPoint joinPoint, Object returnValue) {
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
        logModel.setCreatedTime(new Date());

        userOpLogMapper.create(logModel);
    }

    /**
     * 快捷支付 免密支付 协议开通
     *
     * @param joinPoint
     * @param returnValue
     */
    @AfterReturning(value = "execution(* *..paywrapper.service.AgreementService.agreement(..))", returning = "returnValue")
    public void afterAgreement(JoinPoint joinPoint, Object returnValue) {
        Object[] args = joinPoint.getArgs();
        AgreementDto dto = (AgreementDto) args[0];

        UserOpType opType;

        if (dto.isAutoInvest() || dto.isNoPasswordInvest()) {
            opType = UserOpType.NO_PASSWORD_AGREEMENT; // 开通免密支付协议
        } else if (dto.isFastPay()) {
            opType = UserOpType.FAST_PAY_AGREEMENT; // 开通快捷支付协议
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
        logModel.setCreatedTime(new Date());
        userOpLogMapper.create(logModel);

    }

    /**
     * 打开自动投标开关
     *
     * @param joinPoint
     */
    @AfterReturning(value = "execution(* com.tuotiansudai.service.InvestService.turnOnAutoInvest(..))")
    public void afterTurnOnAutoInvest(JoinPoint joinPoint) {
        String loginName = (String) joinPoint.getArgs()[0];
        String ip = (String) joinPoint.getArgs()[2];

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(loginName);
        logModel.setIp(ip);
        logModel.setDeviceId("");
        logModel.setSource(Source.WEB);
        logModel.setOpType(UserOpType.AUTO_INVEST);
        logModel.setCreatedTime(new Date());
        logModel.setDescription("Turn On.");

        userOpLogMapper.create(logModel);
    }

    /**
     * 关闭自动投标开关
     *
     * @param joinPoint
     */
    @AfterReturning(value = "execution(* com.tuotiansudai.service.InvestService.turnOffAutoInvest(..))", returning = "returnValue")
    public void afterTurnOffAutoInvest(JoinPoint joinPoint, Object returnValue) {
        if (!(Boolean) returnValue) return;

        String loginName = (String) joinPoint.getArgs()[0];
        String ip = (String) joinPoint.getArgs()[1];

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(loginName);
        logModel.setIp(ip);
        logModel.setDeviceId("");
        logModel.setSource(Source.WEB);
        logModel.setOpType(UserOpType.AUTO_INVEST);
        logModel.setCreatedTime(new Date());
        logModel.setDescription("Turn Off.");

        userOpLogMapper.create(logModel);
    }

    /**
     * 无密投资开关
     *
     * @param joinPoint
     */
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
        logModel.setCreatedTime(new Date());
        logModel.setDescription(isTurnOn ? "Turn On" : "Turn Off");

        userOpLogMapper.create(logModel);

    }
}
