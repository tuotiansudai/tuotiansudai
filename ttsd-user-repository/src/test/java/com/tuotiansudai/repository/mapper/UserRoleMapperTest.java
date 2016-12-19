package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserRoleMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Test
    public void shouldCreate() throws Exception {
        UserModel fakeUserModel = getFakeUserModel();
        userMapper.create(fakeUserModel);

        UserRoleModel userRoleModel1 = new UserRoleModel();
        userRoleModel1.setLoginName(fakeUserModel.getLoginName());
        userRoleModel1.setRole(Role.USER);
        List<UserRoleModel> userRoleModels1 = Lists.newArrayList();
        userRoleModels1.add(userRoleModel1);
        userRoleMapper.create(userRoleModels1);

        UserRoleModel userRoleModel2 = new UserRoleModel();
        userRoleModel2.setLoginName(fakeUserModel.getLoginName());
        userRoleModel2.setRole(Role.INVESTOR);
        List<UserRoleModel> userRoleModels2 = Lists.newArrayList();
        userRoleModels2.add(userRoleModel2);
        userRoleMapper.create(userRoleModels2);

        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(fakeUserModel.getLoginName());

        assertThat(userRoleModels.size(), is(2));
    }

    public UserModel getFakeUserModel() {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName("loginName");
        fakeUserModel.setPassword("123abc");
        fakeUserModel.setEmail("12345@abc.com");
        fakeUserModel.setMobile("13900000000");
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        fakeUserModel.setSalt("salt");
        return fakeUserModel;
    }
}
