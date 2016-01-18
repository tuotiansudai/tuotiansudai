package com.tuotiansudai.console.jpush.repository.mapper;

import com.tuotiansudai.console.jpush.repository.model.JPushAlertModel;
import com.tuotiansudai.console.jpush.repository.model.JumpTo;
import com.tuotiansudai.console.jpush.repository.model.PushSource;
import com.tuotiansudai.console.jpush.repository.model.PushType;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

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
    @Test
    public void shouldFindJPushAlertModelByIdIsSuccess(){
        UserModel userModel1 = fakeUserModel("tuotian1", "12900000000");
        userMapper.create(userModel1);
        JPushAlertModel jPushAlertModel = getFakeJPushAlertModel("tuotian1");
        jPushAlertMapper.create(jPushAlertModel);

        JPushAlertModel jPushAlertModel1 = jPushAlertMapper.findJPushAlertModelById(jPushAlertModel.getId());
        assertEquals(jPushAlertModel.getName(), jPushAlertModel1.getName());
        assertEquals(jPushAlertModel.getJumpTo(), jPushAlertModel1.getJumpTo());
    }
    @Test
    public void shouldUpdateJPushAlertModelIsSuccess(){
        UserModel userModel1 = fakeUserModel("tuotian1", "12900000000");
        userMapper.create(userModel1);
        JPushAlertModel jPushAlertModel = getFakeJPushAlertModel("tuotian1");
        jPushAlertMapper.create(jPushAlertModel);

        jPushAlertModel.setPushObjects(Arrays.asList(new String[]{"44", "55"}));
        jPushAlertModel.setUpdatedTime(new Date());
        jPushAlertModel.setUpdatedBy(userModel1.getLoginName());

        jPushAlertMapper.update(jPushAlertModel);

        JPushAlertModel jPushAlertModel1 = jPushAlertMapper.findJPushAlertModelById(jPushAlertModel.getId());

        assertNotNull(jPushAlertModel1.getUpdatedBy());
        assertNotNull(jPushAlertModel1.getUpdatedTime());

        List<String> pushObjects = jPushAlertModel1.getPushObjects();

        String[] pushObjectArr = (String[])pushObjects.toArray();
        assertEquals("44",pushObjectArr[0]);
        assertEquals("55",pushObjectArr[1]);


    }

    private JPushAlertModel getFakeJPushAlertModel(String loginName){
        JPushAlertModel jPushAlertModel = new JPushAlertModel();
        jPushAlertModel.setName("name");
        jPushAlertModel.setPushType(PushType.HUMANISTIC_CARE);
        jPushAlertModel.setPushSource(PushSource.ALL);
        jPushAlertModel.setContent("content");
        jPushAlertModel.setJumpTo(JumpTo.INVEST);
        jPushAlertModel.setJumpToLink("lindAddress");
        jPushAlertModel.setCreatedTime(new Date());
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
