package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.enums.MembershipPrivilegePurchaseStatus;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.membership.repository.mapper.MembershipPrivilegeMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipPrivilegePurchaseMapper;
import com.tuotiansudai.membership.repository.model.MembershipPrivilege;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegeModel;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePriceType;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePurchaseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.SystemBillMapper;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.DateConvertUtil;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.ibatis.javassist.compiler.ast.Member;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MembershipPrivilegePurchaseCallbackTest extends RepayBaseTest {

    @Autowired
    private MembershipPrivilegePurchasePayService membershipPrivilegePurchasePayService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private MembershipPrivilegePurchaseMapper membershipPrivilegePurchaseMapper;
    @Autowired
    private UserBillMapper userBillMapper;
    @Autowired
    private SystemBillMapper systemBillMapper;
    @Autowired
    private MembershipPrivilegeMapper membershipPrivilegeMapper;

    @Test
    public void shouldPurchaseCallbackIsSuccess() {
        UserModel userModel = getFakeUser("loginNameTester");
        userModel.setUserName("UserName");
        userModel.setIdentityNumber("11XXX11XXX11XXX123");
        userModel.setMobile(RandomStringUtils.randomNumeric(11));
        userMapper.create(userModel);

        AccountModel accountModel = this.getFakeAccount(userModel);
        accountMapper.create(accountModel);
        MembershipPrivilegePurchaseModel membershipPrivilegePurchaseModel
                = new MembershipPrivilegePurchaseModel(IdGenerator.generate(), userModel.getLoginName(),
                userModel.getMobile(),
                userModel.getUserName(),
                MembershipPrivilegePriceType._30,
                MembershipPrivilege.SERVICE_FEE,
                Source.IOS);

        membershipPrivilegePurchaseMapper.create(membershipPrivilegePurchaseModel);


        membershipPrivilegePurchasePayService.purchaseCallback(this.getFakeCallbackParamsMap(membershipPrivilegePurchaseModel.getId(), "transfer_notify"), "");

        List<UserBillModel> userBillModels = userBillMapper.findByLoginName(userModel.getLoginName());
        SystemBillModel systemBillModels = systemBillMapper.findByOrderId(membershipPrivilegePurchaseModel.getId(), SystemBillBusinessType.MEMBERSHIP_PRIVILEGE_PURCHASE);

        assertThat(userBillModels.size(), is(1));
        assertThat(userBillModels.get(0).getLoginName(), is(userModel.getLoginName()));
        assertThat(userBillModels.get(0).getAmount(), is(membershipPrivilegePurchaseModel.getAmount()));
        assertThat(userBillModels.get(0).getBalance(), is(accountModel.getBalance() - membershipPrivilegePurchaseModel.getAmount()));
        assertThat(userBillModels.get(0).getBusinessType(), is(UserBillBusinessType.MEMBERSHIP_PRIVILEGE_PURCHASE));

        assertNotNull(systemBillModels);
        assertThat(systemBillModels.getAmount(), is(membershipPrivilegePurchaseModel.getAmount()));
        assertThat(systemBillModels.getBusinessType(), is(SystemBillBusinessType.MEMBERSHIP_PRIVILEGE_PURCHASE));

        MembershipPrivilegePurchaseModel membershipPrivilegePurchaseModelReturn = membershipPrivilegePurchaseMapper.findById(membershipPrivilegePurchaseModel.getId());
        assertNotNull(membershipPrivilegePurchaseModelReturn);
        assertThat(membershipPrivilegePurchaseModelReturn.getStatus(), is(MembershipPrivilegePurchaseStatus.SUCCESS));
        Date currentDate = new DateTime().plusMinutes(5).toDate();
        MembershipPrivilegeModel membershipPrivilegeModel = membershipPrivilegeMapper.findValidPrivilegeModelByLoginName(userModel.getLoginName(), currentDate);

        assertNotNull(membershipPrivilegeModel);
        assertThat(membershipPrivilegeModel.getLoginName(), is(userModel.getLoginName()));
        assertThat(membershipPrivilegeModel.getPrivilege(), is(membershipPrivilegePurchaseModel.getPrivilege()));

    }

}
