package com.tuotiansudai.repository.mapper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Test
    public void shouldFindAllUsers() throws Exception {
        UserModel userModel = this.getUserModelTest();
        userMapper.create(userModel);
        userModel.setProvince("北京");
        userMapper.updateUser(userModel);

        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("hellokitty");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000001");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        userModelTest.setProvince("天津");
        userMapper.updateUser(userModelTest);

        List<String> list = userMapper.findAllUsersByProvinces(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", Lists.newArrayList("天津")).build()));
        assertThat(list.size(), is(1));

        assertThat(list.get(0), is("hellokitty"));
    }

    @Test
    public void shouldFindAllByRole() throws Exception {
        UserModel userModel = this.getUserModelTest();
        userMapper.create(userModel);
        userModel.setProvince("北京");
        userMapper.updateUser(userModel);
        UserRoleModel userRoleModel = new UserRoleModel("helloworld", Role.SD_STAFF);
        userRoleMapper.create(Lists.newArrayList(userRoleModel));

        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("hellokitty");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000001");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        userModelTest.setProvince("天津");
        userMapper.updateUser(userModelTest);
        UserRoleModel userRoleModelTest = new UserRoleModel("hellokitty", Role.AGENT);
        userRoleMapper.create(Lists.newArrayList(userRoleModelTest));

        List<String> staffList = userMapper.findAllByRole(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("role", Role.SD_STAFF).put("districtName", Lists.newArrayList("北京")).build()));

        assertThat(staffList.get(0), is("helloworld"));

        List<String> agentList = userMapper.findAllByRole(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("role", Role.AGENT).put("districtName", Lists.newArrayList("天津")).build()));

        assertThat(agentList.get(0), is("hellokitty"));
    }

    @Test
    public void shouldFindUserByEmailIsNotNull() throws Exception {
        userMapper.create(this.getUserModelTest());

        UserModel userModel = userMapper.findByEmail("12345@abc.com");
        assertNotNull(userModel);
    }

    @Test
    public void shouldFindUserByEmailIsNull() throws Exception {
        UserModel userModel = userMapper.findByEmail("22345@abc.com");

        assertNull(userModel);
    }

    @Test
    public void shouldFindUserByMobileIsNotNull() throws Exception {
        userMapper.create(this.getUserModelTest());

        UserModel userModel = userMapper.findByMobile("13900000000");
        assertNotNull(userModel);
    }

    @Test
    public void shouldFindUserByMobileIsNull() throws Exception {
        UserModel userModel = userMapper.findByMobile("13800000000");
        assertNull(userModel);
    }

    @Test
    public void shouldFindUserByLoginNameIsNotNull() throws Exception {
        userMapper.create(this.getUserModelTest());

        UserModel userModel = userMapper.findByLoginName("helloworld");
        assertNotNull(userModel);
    }

    @Test
    public void shouldFindUserByLoginNameIsNull() throws Exception {
        UserModel userModel = userMapper.findByLoginName("helloworld1");
        assertNull(userModel);
    }

    @Test
    public void shouldFindUserByLoginNameOrMobileIsNotNull() throws Exception {
        userMapper.create(this.getUserModelTest());
        UserModel userModel = userMapper.findByLoginNameOrMobile("helloworld");
        assertNotNull(userModel);
    }

    @Test
    public void shouldInsertUser() throws Exception {
        userMapper.create(this.getUserModelTest());

        UserModel userModel = userMapper.findByLoginName("helloworld");
        assertNotNull(userModel);

    }

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
    public void testGetUserCount() {
        long userCount = userMapper.findUsersCount();
        assertTrue(userCount > 0);
    }

    @Test
    public void shouldFindExperienceIsOk(){
        UserModel userModel = this.getUserModelTest();
        userModel.setLoginName("testExperience");
        userModel.setExperienceBalance(100l);
        userModel.setMobile("10000000000");
        userMapper.create(userModel);

        Long experience = userMapper.findExperienceByLoginName(userModel.getLoginName());
        assertTrue(experience == userModel.getExperienceBalance());
    }

    @Test
    public void shouldUpdateExperienceBalance() throws Exception {
        UserModel userModelTest = this.getUserModelTest();
        userMapper.create(userModelTest);

        userMapper.updateExperienceBalance(userModelTest.getLoginName(), ExperienceBillOperationType.IN, 2);

        assertThat(userMapper.findByLoginName(userModelTest.getLoginName()).getExperienceBalance(), is(2L));

        userMapper.updateExperienceBalance(userModelTest.getLoginName(), ExperienceBillOperationType.OUT, 1L);

        assertThat(userMapper.findByLoginName(userModelTest.getLoginName()).getExperienceBalance(), is(1L));
    }
}
