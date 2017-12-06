package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.enums.MembershipPrivilegePurchaseStatus;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.membership.repository.mapper.MembershipPrivilegeMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipPrivilegePurchaseMapper;
import com.tuotiansudai.membership.repository.model.MembershipPrivilege;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegeModel;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePriceType;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePurchaseModel;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.SystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class MembershipPrivilegePurchaseCallbackTest extends RepayBaseTest {

    @Autowired
    private MembershipPrivilegePurchasePayService membershipPrivilegePurchasePayService;
    @Autowired
    private FakeUserHelper userMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private MembershipPrivilegePurchaseMapper membershipPrivilegePurchaseMapper;
    @Autowired
    private MembershipPrivilegeMapper membershipPrivilegeMapper;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Test
    public void shouldPurchaseCallbackIsSuccess() throws Exception {
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

        verifyAmountTransferMessage(userModel, membershipPrivilegePurchaseModel);

        verifySystemBillMessage(membershipPrivilegePurchaseModel);

        MembershipPrivilegePurchaseModel membershipPrivilegePurchaseModelReturn = membershipPrivilegePurchaseMapper.findById(membershipPrivilegePurchaseModel.getId());
        assertNotNull(membershipPrivilegePurchaseModelReturn);
        assertThat(membershipPrivilegePurchaseModelReturn.getStatus(), is(MembershipPrivilegePurchaseStatus.SUCCESS));
        Date currentDate = new DateTime().plusMinutes(5).toDate();
        MembershipPrivilegeModel membershipPrivilegeModel = membershipPrivilegeMapper.findValidPrivilegeModelByLoginName(userModel.getLoginName(), currentDate);

        assertNotNull(membershipPrivilegeModel);
        assertThat(membershipPrivilegeModel.getLoginName(), is(userModel.getLoginName()));
        assertThat(membershipPrivilegeModel.getPrivilege(), is(membershipPrivilegePurchaseModel.getPrivilege()));

    }

    private void verifySystemBillMessage(MembershipPrivilegePurchaseModel membershipPrivilegePurchaseModel) throws IOException {
        String messageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.SystemBill.getQueueName()));
        SystemBillMessage message = JsonConverter.readValue(messageBody, SystemBillMessage.class);
        assertThat(message.getAmount(), is(membershipPrivilegePurchaseModel.getAmount()));
        assertThat(message.getBusinessType(), is(SystemBillBusinessType.MEMBERSHIP_PRIVILEGE_PURCHASE));
    }

    private void verifyAmountTransferMessage(UserModel userModel, MembershipPrivilegePurchaseModel membershipPrivilegePurchaseModel) throws IOException {
        String messageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
        AmountTransferMessage message = JsonConverter.readValue(messageBody, AmountTransferMessage.class);
        assertThat(message.getLoginName(), is(userModel.getLoginName()));
        assertThat(message.getAmount(), is(membershipPrivilegePurchaseModel.getAmount()));
        assertThat(message.getBusinessType(), is(UserBillBusinessType.MEMBERSHIP_PRIVILEGE_PURCHASE));
        assertThat(message.getTransferType(), is(TransferType.TRANSFER_OUT_BALANCE));
    }

}
