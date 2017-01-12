package com.tuotiansudai.membership.service;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.membership.dto.MembershipPrivilegePurchaseDto;
import com.tuotiansudai.membership.exception.MembershipPrivilegeIsPurchasedException;
import com.tuotiansudai.membership.exception.NotEnoughAmountException;
import com.tuotiansudai.membership.repository.mapper.MembershipPrivilegeMapper;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePriceType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MembershipPrivilegePurchaseService {

    private static Logger logger = Logger.getLogger(MembershipPrivilegePurchaseService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private MembershipPrivilegeMapper membershipPrivilegeMapper;

    public BaseDto<PayFormDataDto> purchase(String loginName,MembershipPrivilegePriceType membershipPrivilegePriceType, Source source) throws MembershipPrivilegeIsPurchasedException, NotEnoughAmountException {
        logger.info(String.format("[membership privilege purchase:] user(%s) purchase duration(%s)",loginName,String.valueOf(membershipPrivilegePriceType.getDuration())));
        if(membershipPrivilegeMapper.findValidPrivilegeModelByLoginName(loginName,new Date()) != null){
            throw new MembershipPrivilegeIsPurchasedException();
        }
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginName);
        AccountModel accountModel = accountMapper.findByLoginName(loginName);

        if (accountModel == null || accountModel.getBalance() < membershipPrivilegePriceType.getPrice()) {
            throw new NotEnoughAmountException();
        }

        MembershipPrivilegePurchaseDto purchaseDto = new MembershipPrivilegePurchaseDto(loginName,
                userModel.getMobile(),
                userModel.getUserName(),
                membershipPrivilegePriceType.getDuration(), membershipPrivilegePriceType.getPrice(), source);

        return payWrapperClient.membershipPrivilegePurchase(purchaseDto);
    }

}
