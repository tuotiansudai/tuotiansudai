package com.ttsd.api.service.impl;

import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.impl.UserBO;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipAccount;
import com.esoft.jdp2p.user.service.RechargeService;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppPersonalInfoService;
import com.ttsd.api.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

@Service
public class MobileAppPersonalInfoServiceImpl implements MobileAppPersonalInfoService {

    @Resource
    private HibernateTemplate ht;

    @Resource
    private UserBO userBO;

    @Autowired
    private RechargeService rechargeService;

    @Override
    public BaseResponseDto getPersonalInfoData(PersonalInfoRequestDto personalInfoRequestDto) {
        String returnCode = ReturnMessage.SUCCESS.getCode();
        BaseResponseDto<PersonalInfoResponseDataDto> dto = new BaseResponseDto();
        String userName = personalInfoRequestDto.getUserName();

        User user = userBO.getUserByUserNameOrMobileNumber(userName);
        if (user == null){
            returnCode = ReturnMessage.USER_ID_IS_NULL.getCode();
        }
        if(ReturnMessage.SUCCESS.getCode().equals(returnCode)){
            PersonalInfoResponseDataDto personalInfoDataDto = generatePersonalInfoData(user);
            dto.setData(personalInfoDataDto);
        }
        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        return dto;
    }

    @Override
    public PersonalInfoResponseDataDto generatePersonalInfoData(User user) {
        PersonalInfoResponseDataDto personalInfoDataDto = new PersonalInfoResponseDataDto();
        personalInfoDataDto.setUserId(user.getUsername());
        personalInfoDataDto.setUserName(user.getUsername());
        personalInfoDataDto.setPhoneNum(user.getMobileNumber());
        personalInfoDataDto.setPhoto(user.getPhoto());
        boolean certificationFlag = verifyCertification(user.getUsername());
        personalInfoDataDto.setCertificationFlag(certificationFlag);
        boolean isBindedBankCard = isBindedBankCard(user.getUsername());
        personalInfoDataDto.setIsBoundBankCard(isBindedBankCard);
        if(certificationFlag){
            personalInfoDataDto.setRealName(user.getRealname());
            personalInfoDataDto.setIdCard(user.getIdCard());
        }else {
            personalInfoDataDto.setRealName("");
            personalInfoDataDto.setIdCard("");
        }
        BankCard bankCard = queryBankCardByUserId(user.getUsername());
        if(bankCard != null){
            personalInfoDataDto.setBankCardNo(CommonUtils.encryptBankCardNo(bankCard.getCardNo()));
            personalInfoDataDto.setBankId(bankCard.getBankNo());
            personalInfoDataDto.setIsFastPayment(isOpenFastPayment(bankCard));
            personalInfoDataDto.setFastPaymentEnable(rechargeService.isFastPaymentBank(bankCard.getBankNo()));
        }else{
            personalInfoDataDto.setBankCardNo("");
            personalInfoDataDto.setBankId("");
            personalInfoDataDto.setIsFastPayment(false);
            personalInfoDataDto.setFastPaymentEnable(false);
        }

        return personalInfoDataDto;
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

    @Override
    public BankCard queryBankCardByUserId(String userId) {
        String sql = "select * from bank_card where user_id = ? and status='passed' ";
        SQLQuery sqlQuery= ht.getSessionFactory().getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, userId);
        sqlQuery.addEntity(BankCard.class);
        List<BankCard> bankCards = sqlQuery.list();
        if(CollectionUtils.isNotEmpty(bankCards)){
            return bankCards.get(0);
        }
        return null;
    }

    @Override
    public boolean isOpenFastPayment(BankCard bankCard) {
        
        return bankCard.getIsOpenFastPayment();
    }


}
