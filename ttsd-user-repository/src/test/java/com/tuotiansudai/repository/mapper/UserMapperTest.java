package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserExperienceMapper userExperienceMapper;

    @Autowired
    private FakeUserHelper fakeUserHelper;

    @Autowired
    private UserRoleMapper userRoleMapper;


    public UserModel getUserModelTest() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("helloworld");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }


    @Test
    public void shouldFindExperienceIsOk() {
        UserModel userModel = this.getUserModelTest();
        userModel.setLoginName("testExperience");
        userModel.setExperienceBalance(100l);
        userModel.setMobile("10000000000");
        fakeUserHelper.create(userModel);

        Long experience = userExperienceMapper.findExperienceByLoginName(userModel.getLoginName());
        assertTrue(experience == userModel.getExperienceBalance());
    }

    @Test
    public void shouldUpdateExperienceBalance() throws Exception {
        UserModel userModelTest = this.getUserModelTest();
        fakeUserHelper.create(userModelTest);

        userExperienceMapper.updateExperienceBalance(userModelTest.getLoginName(), ExperienceBillOperationType.IN, 2);

        assertThat(userExperienceMapper.findExperienceByLoginName(userModelTest.getLoginName()), is(2L));

        userExperienceMapper.updateExperienceBalance(userModelTest.getLoginName(), ExperienceBillOperationType.OUT, 1L);

        assertThat(userExperienceMapper.findExperienceByLoginName(userModelTest.getLoginName()), is(1L));
    }
}
