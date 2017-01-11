package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.membership.repository.mapper.MembershipPrivilegePurchaseMapper;
import com.tuotiansudai.membership.repository.model.MembershipPrivilege;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePriceType;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePurchaseModel;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.ibatis.javassist.compiler.ast.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MembershipPrivilegePurchaseCallbackTest extends RepayBaseTest {

    @Autowired
    private MembershipPrivilegePurchasePayService membershipPrivilegePurchasePayService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private MembershipPrivilegePurchaseMapper membershipPrivilegePurchaseMapper;
    @Test
    public void shouldPurchaseCallbackIsSuccess(){
        UserModel userModel = getFakeUser("loginNameTester");
        userModel.setUserName("UserName");
        userModel.setIdentityNumber("11XXX11XXX11XXX123");
        userModel.setMobile(RandomStringUtils.randomNumeric(11));
        userMapper.create(userModel);
        MembershipPrivilegePurchaseModel membershipPrivilegePurchaseModel
                = new MembershipPrivilegePurchaseModel(idGenerator.generate(),userModel.getLoginName(),
                                                       userModel.getMobile(),
                                                       userModel.getUserName(),
                                                       MembershipPrivilegePriceType._30,
                                                       MembershipPrivilege.SERVICE_FEE,
                                                       Source.IOS);

        membershipPrivilegePurchaseMapper.create(membershipPrivilegePurchaseModel);
        membershipPrivilegePurchasePayService.purchaseCallback(this.getFakeCallbackParamsMap(membershipPrivilegePurchaseModel.getId(),"transfer_notify"),"");
    }

}
