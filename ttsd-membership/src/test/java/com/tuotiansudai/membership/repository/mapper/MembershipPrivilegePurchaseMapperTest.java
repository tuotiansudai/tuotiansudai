package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipPrivilege;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePriceType;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePurchaseModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MembershipPrivilegePurchaseMapperTest extends BaseMapperTest {

    @Autowired
    private MembershipPrivilegePurchaseMapper membershipPrivilegePurchaseMapper;

    @Test
    public void shouldCreateIsSuccess() {
        UserModel userModel = createFakeUser("loginName", "userName", "11XXX11XXX11XXX123");
        MembershipPrivilegePurchaseModel membershipPrivilegePurchaseModel
                = new MembershipPrivilegePurchaseModel(Long.parseLong(RandomStringUtils.randomNumeric(10)), userModel.getLoginName(), userModel.getMobile(), userModel.getUserName(),
                MembershipPrivilegePriceType._30, MembershipPrivilege.SERVICE_FEE, Source.IOS);
        membershipPrivilegePurchaseMapper.create(membershipPrivilegePurchaseModel);

        MembershipPrivilegePurchaseModel membershipPrivilegePurchaseModelReturn
                = membershipPrivilegePurchaseMapper.findById(membershipPrivilegePurchaseModel.getId());

        assertThat(membershipPrivilegePurchaseModel.getStatus(), is(membershipPrivilegePurchaseModelReturn.getStatus()));
        assertThat(membershipPrivilegePurchaseModel.getAmount(), is(membershipPrivilegePurchaseModelReturn.getAmount()));
        assertThat(membershipPrivilegePurchaseModel.getLoginName(), is(membershipPrivilegePurchaseModelReturn.getLoginName()));
        assertThat(membershipPrivilegePurchaseModel.getMobile(), is(membershipPrivilegePurchaseModelReturn.getMobile()));
        assertThat(membershipPrivilegePurchaseModel.getPrivilege(), is(membershipPrivilegePurchaseModelReturn.getPrivilege()));
        assertThat(membershipPrivilegePurchaseModel.getSource(), is(membershipPrivilegePurchaseModelReturn.getSource()));

    }

}
