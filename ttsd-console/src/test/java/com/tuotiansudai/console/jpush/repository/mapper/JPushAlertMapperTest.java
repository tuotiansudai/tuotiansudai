package com.tuotiansudai.console.jpush.repository.mapper;

import com.tuotiansudai.console.jpush.repository.model.JPushAlertModel;
import com.tuotiansudai.console.jpush.repository.model.JumpTo;
import com.tuotiansudai.console.jpush.repository.model.PushSource;
import com.tuotiansudai.console.jpush.repository.model.PushType;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:spring-security.xml", "classpath:applicationContext.xml"})
@Transactional
public class JPushAlertMapperTest {
    @Autowired
    private JPushAlertMapper jPushAlertMapper;
    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldCreateJPushAlertIsSuccess(){
        UserModel userModel1 = fakeUserModel("tuotian1", "12900000000");
        userMapper.create(userModel1);
        JPushAlertModel jPushAlertModel = getFakeJPushAlertModel("tuotian1");
        jPushAlertMapper.create(jPushAlertModel);

        assertNotNull(jPushAlertModel.getId());

    }

    private JPushAlertModel getFakeJPushAlertModel(String loginName){
        JPushAlertModel jPushAlertModel = new JPushAlertModel();
        jPushAlertModel.setName("name");
        jPushAlertModel.setPushTime(new Date());
        jPushAlertModel.setPushType(PushType.HUMANISTIC_CARE);
        jPushAlertModel.setPushSource(PushSource.All);
        jPushAlertModel.setContent("content");
        jPushAlertModel.setJumpTo(JumpTo.INVEST);
        jPushAlertModel.setLinkAddress("lindAddress");
        jPushAlertModel.setCreatedTime(new Date());
        String[] pushObjects = {"11","12"};
        jPushAlertModel.setPushObjects(Arrays.asList(new String[]{"12","13"}));
        jPushAlertModel.setCreatedBy(loginName);
        return jPushAlertModel;
    }

    private UserModel fakeUserModel(String loginName,String mobile) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile(mobile);
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }




}
