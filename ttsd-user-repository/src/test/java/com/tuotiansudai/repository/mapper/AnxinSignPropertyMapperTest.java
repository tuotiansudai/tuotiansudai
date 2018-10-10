package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AnxinSignPropertyModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.util.UUIDGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class AnxinSignPropertyMapperTest {

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Autowired
    private FakeUserHelper fakeUserHelper;

    @Test
    public void shouldCreateUpdateFind() throws Exception {
        String loginName = "anxinLoginName";
        createFakeUser(loginName);
        AnxinSignPropertyModel model = new AnxinSignPropertyModel();
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

    private UserModel createFakeUser(String loginName) {
        UserModel model = new UserModel();
        model.setLoginName(loginName);
        model.setPassword("password");
        model.setEmail("loginName@abc.com");
        model.setMobile(String.valueOf(new Random().nextInt(10000)));
        model.setUserName("userName");
        model.setIdentityNumber("identityNumber");
        model.setRegisterTime(new Date());
        model.setStatus(UserStatus.ACTIVE);
        model.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        fakeUserHelper.create(model);
        return model;
    }

}
