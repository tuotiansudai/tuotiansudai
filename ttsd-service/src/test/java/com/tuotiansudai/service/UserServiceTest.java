package com.tuotiansudai.service;

import com.tuotiansudai.dto.RegisterUserDto;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.impl.UserServiceImpl;
import com.tuotiansudai.utils.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Test
    public void shouldSaveReferrerRelations() {
        UserModel user1 = new UserModel();
        user1.setId(idGenerator.generate());
        user1.setLoginName("test1");
        user1.setPassword("123");
        user1.setMobile("13900000000");
        user1.setRegisterTime(new Date());
        user1.setLastLoginTime(new Date());
        user1.setLastModifiedTime(new Date());
        user1.setStatus(UserStatus.ACTIVE);
        user1.setSalt("123");
        userMapper.create(user1);
        UserModel user2 = new UserModel();
        user2.setId(idGenerator.generate());
        user2.setLoginName("test2");
        user2.setPassword("123");
        user2.setMobile("13900000001");
        user2.setRegisterTime(new Date());
        user2.setLastLoginTime(new Date());
        user2.setLastModifiedTime(new Date());
        user2.setStatus(UserStatus.ACTIVE);
        user2.setSalt("123");
        user2.setReferrer("test1");
        userMapper.create(user2);

        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();
        referrerRelationModel.setReferrerLoginName("test1");
        referrerRelationModel.setLoginName("test2");
        referrerRelationModel.setLevel(1);

        referrerRelationMapper.create(referrerRelationModel);


        UserModel user3 = new UserModel();
        user3.setId(idGenerator.generate());
        user3.setLoginName("test3");
        user3.setPassword("123");
        user3.setMobile("13900000002");
        user3.setRegisterTime(new Date());
        user3.setLastLoginTime(new Date());
        user3.setLastModifiedTime(new Date());
        user3.setStatus(UserStatus.ACTIVE);
        user3.setSalt("123");
        user3.setReferrer("test2");
        userMapper.create(user3);

//        userService.saveReferrerRelations("test2", "test3");

        List<ReferrerRelationModel> models = referrerRelationMapper.findByLoginName("test3");

        for (ReferrerRelationModel model : models) {
            if ("test1".equals(model.getReferrerLoginName())) {
                assertEquals(2, model.getLevel());
            }
        }

    }
}
