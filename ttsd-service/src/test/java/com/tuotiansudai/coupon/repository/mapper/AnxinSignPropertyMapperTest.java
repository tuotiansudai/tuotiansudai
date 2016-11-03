package com.tuotiansudai.coupon.repository.mapper;

import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.repository.mapper.AnxinSignPropertyMapper;
import com.tuotiansudai.repository.mapper.BaseMapperTest;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.UUIDGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class AnxinSignPropertyMapperTest extends BaseMapperTest {

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Test
    public void shouldCreateUpdateFind() throws Exception {
        AnxinSignPropertyModel model = new AnxinSignPropertyModel();

        String loginName = "sidneygao";

        model.setLoginName(loginName);
        model.setProjectCode(UUIDGenerator.generate());
        model.setIp("192.168.111.222");
        model.setAnxinUserId("asdfghjklpoiuytrewwqzcxb89274940");
        model.setAuthTime(new Date());
        model.setCreatedTime(new Date());
        model.setSkipAuth(true);
        anxinSignPropertyMapper.create(model);

        AnxinSignPropertyModel getModel = anxinSignPropertyMapper.findByLoginName(loginName);

        assertNotNull(getModel);

        getModel.setProjectCode("abcd");
        getModel.setIp("123");
        getModel.setAnxinUserId("uuuu");
        Date authTime = new Date();
        getModel.setAuthTime(authTime);
        getModel.setSkipAuth(false);
        anxinSignPropertyMapper.update(getModel);

        AnxinSignPropertyModel getModel2 = anxinSignPropertyMapper.findByLoginName(loginName);

        assert (getModel2.getProjectCode().equals("abcd"));
        assert (getModel2.getIp().equals("123"));
        assert (getModel2.getAnxinUserId().equals("uuuu"));
        assert (getModel2.isSkipAuth() == false);
        assert (getModel2.getAuthTime().getTime() / 1000 == authTime.getTime() / 1000);

        AnxinSignPropertyModel getModel3 = anxinSignPropertyMapper.findById(getModel2.getId());

        assertNotNull(getModel3);
    }

}
