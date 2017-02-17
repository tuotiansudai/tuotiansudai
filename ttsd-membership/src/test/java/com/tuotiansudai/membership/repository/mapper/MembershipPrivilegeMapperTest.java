package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipPrivilege;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegeModel;
import com.tuotiansudai.repository.model.UserModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MembershipPrivilegeMapperTest extends BaseMapperTest{
    @Autowired
    private MembershipPrivilegeMapper membershipPrivilegeMapper;
    @Test
    public void shouldCreateIsSuccess(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        UserModel userModel = createFakeUser("loginName","userName","11XXX11XXX11XXX123");
        MembershipPrivilegeModel membershipPrivilegeModel = new MembershipPrivilegeModel(userModel.getLoginName(),
                MembershipPrivilege.SERVICE_FEE,new Date(),new Date());

        membershipPrivilegeMapper.create(membershipPrivilegeModel);

        MembershipPrivilegeModel membershipPrivilegeModelReturn = membershipPrivilegeMapper.findById(membershipPrivilegeModel.getId());

        assertThat(membershipPrivilegeModel.getLoginName(),is(membershipPrivilegeModelReturn.getLoginName()));
        assertThat(membershipPrivilegeModel.getPrivilege(),is(membershipPrivilegeModelReturn.getPrivilege()));
        assertThat(dateToLocalDateTime(membershipPrivilegeModel.getStartTime()).format(formatter),
                is(dateToLocalDateTime(membershipPrivilegeModelReturn.getStartTime()).format(formatter)));
        assertThat(dateToLocalDateTime(membershipPrivilegeModel.getEndTime()).format(formatter),is(dateToLocalDateTime(membershipPrivilegeModelReturn.getEndTime()).format(formatter)));


    }


}
