package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.PointTaskModel;
import com.tuotiansudai.point.repository.model.UserPointTaskModel;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
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
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserPointTaskMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PointTaskMapper pointTaskMapper;

    @Autowired
    private UserPointTaskMapper userPointTaskMapper;

    @Test
    public void shouldCreatePointBillModel() throws Exception {
        UserModel fakeUserModel = this.createFakeUserModel();

        List<PointTaskModel> pointTaskModels = pointTaskMapper.find(0,1);
        for (PointTaskModel pointTaskModel : pointTaskModels) {
            userPointTaskMapper.create(new UserPointTaskModel(fakeUserModel.getLoginName(), pointTaskModel.getId()));
        }

        assertThat(userPointTaskMapper.findByLoginName(fakeUserModel.getLoginName()).size(), is(pointTaskModels.size()));
    }

    private UserModel createFakeUserModel() {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName("fakeUser");
        fakeUserModel.setPassword("123abc");
        fakeUserModel.setEmail("12345@abc.com");
        fakeUserModel.setMobile("13900000000");
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        fakeUserModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUserModel);
        return fakeUserModel;
    }
}
