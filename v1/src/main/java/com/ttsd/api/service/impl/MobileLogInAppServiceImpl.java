package com.ttsd.api.service.impl;

import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.impl.UserBO;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.ttsd.api.dto.LogInDataDto;
import com.ttsd.api.dto.LogInRequestDto;
import com.ttsd.api.dto.LogInResponseDto;
import com.ttsd.api.dto.ReturnMessage;
import com.ttsd.api.service.MobileLogInAppService;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

@Service
public class MobileLogInAppServiceImpl implements MobileLogInAppService{

    @Resource
    private HibernateTemplate ht;

    @Resource
    private UserBO userBO;

    @Override
    public LogInResponseDto logIn(LogInRequestDto logInRequestDto) {
        String returnCode = ReturnMessage.SUCCESS.getCode();
        LogInResponseDto logInResponseDto = new LogInResponseDto();
        String userName = logInRequestDto.getUserName();

        User user = userBO.getUserByUserNameOrMobileNumber(userName);
        if (user == null){
            returnCode = ReturnMessage.LOGIN_FAILED.getCode();
        }
        if(ReturnMessage.SUCCESS.getCode().equals(returnCode)){
            LogInDataDto logInDataDto = generateLogInData(user);
            logInResponseDto.setData(logInDataDto);
        }
        logInResponseDto.setCode(returnCode);
        logInResponseDto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        return logInResponseDto;
    }

    @Override
    public LogInDataDto generateLogInData(User user) {
        LogInDataDto logInDataDto = new LogInDataDto();
        logInDataDto.setUserId(user.getUsername());
        logInDataDto.setUserName(user.getUsername());
        //logInDataDto.setToken();
        logInDataDto.setPhoneNum(user.getMobileNumber());
        logInDataDto.setPhoto(user.getPhoto());
        boolean certificationFlag = verifyCertification(user.getUsername());
        logInDataDto.setCertificationFlag(certificationFlag);
        boolean isBindedBankCard = isBindedBankCard(user.getUsername());
        logInDataDto.setIsBindedBankCard(isBindedBankCard);
        if(certificationFlag){
            logInDataDto.setRealName(user.getRealname());
            logInDataDto.setIdCard(user.getIdCard());
        }else{
            logInDataDto.setRealName("");
            logInDataDto.setIdCard("");
        }

        return logInDataDto;
    }

    @Override
    public boolean verifyCertification(String userName) {
        TrusteeshipAccount ta = null;
        List<TrusteeshipAccount> taList = ht.find(
                "from TrusteeshipAccount t where t.user.id=? and t.status='passed' ",
                new String[]{userName});
        if (null != taList && taList.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isBindedBankCard(String userName) {
        String hqlTemplate = "select count(bankCard) from BankCard bankCard where bankCard.user=''{0}'' and bankCard.status=''passed''";
        int count = DataAccessUtils.intResult(ht.find(MessageFormat.format(hqlTemplate, userName)));
        return count > 0;
    }


}
