package com.tuotiansudai.coupon.repository.mapper;

import com.tuotiansudai.repository.mapper.AnxinSignPropertyMapper;
import com.tuotiansudai.repository.mapper.BaseMapperTest;
import com.tuotiansudai.repository.model.AnxinSignPropertyModel;
import com.tuotiansudai.util.UUIDGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class AnxinSignPropertyMapperTest extends BaseMapperTest {

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Test
    public void shouldCreateUpdateFind() throws Exception {
        AnxinSignPropertyModel model = new AnxinSignPropertyModel();

        String loginName = "sidneygao";

        model.setLoginName(loginName);
        model.setProjectCode(UUIDGenerator.generate());
        model.setAuthIp("192.168.111.222");
        model.setAnxinUserId("asdfghjklpoiuytrewwqzcxb89274940");
        model.setAuthTime(new Date());
        model.setCreatedTime(new Date());
        model.setSkipAuth(true);
        anxinSignPropertyMapper.create(model);

        AnxinSignPropertyModel getModel = anxinSignPropertyMapper.findByLoginName(loginName);

        assertNotNull(getModel);

        getModel.setProjectCode("abcd");
        getModel.setAuthIp("123");
        getModel.setAnxinUserId("uuuu");
        Date authTime = DateTime.now().withTime(12, 0, 0, 0).toDate();
        getModel.setAuthTime(authTime);
        getModel.setSkipAuth(false);
        anxinSignPropertyMapper.update(getModel);

        AnxinSignPropertyModel getModel2 = anxinSignPropertyMapper.findByLoginName(loginName);

        assert (getModel2.getProjectCode().equals("abcd"));
        assert (getModel2.getAuthIp().equals("123"));
        assert (getModel2.getAnxinUserId().equals("uuuu"));
        assert (getModel2.isSkipAuth() == false);
        assert (getModel2.getAuthTime().getTime() == authTime.getTime());

        AnxinSignPropertyModel getModel3 = anxinSignPropertyMapper.findById(getModel2.getId());

        assertNotNull(getModel3);
    }

}
