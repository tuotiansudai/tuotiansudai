package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ReferrerRelationMapperTest {

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldCreateReferrerRelation() throws Exception {
        UserModel  user1 = new UserModel();
        user1.setId(idGenerator.generate());
        user1.setLoginName("test1");
        user1.setPassword("123");
        user1.setMobile("13900000000");
        user1.setRegisterTime(new Date());
        user1.setLastModifiedTime(new Date());
        user1.setStatus(UserStatus.ACTIVE);
        user1.setSalt("123");
        userMapper.create(user1);
        UserModel  user2 = new UserModel();
        user2.setId(idGenerator.generate());
        user2.setLoginName("test2");
        user2.setPassword("123");
        user2.setMobile("13900000001");
        user2.setRegisterTime(new Date());
        user2.setLastModifiedTime(new Date());
        user2.setStatus(UserStatus.ACTIVE);
        user2.setSalt("123");
        userMapper.create(user2);
        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();
        referrerRelationModel.setReferrerLoginName("test1");
        referrerRelationModel.setLoginName("test2");
        referrerRelationModel.setLevel(1);

        referrerRelationMapper.create(referrerRelationModel);

        List<ReferrerRelationModel> testModelList = referrerRelationMapper.findByLoginName("test2");

        assertNotNull(testModelList.get(0));
    }

    @Test
    public void findReferrerCountByReferrerLoginName() throws Exception {
        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();
        referrerRelationModel.setReferrerLoginName("test11");
        referrerRelationModel.setLoginName("test11");
        referrerRelationModel.setLevel(1);
        UserModel  user1 = new UserModel();
        user1.setId(idGenerator.generate());
        user1.setLoginName("test11");
        user1.setPassword("123");
        user1.setMobile("13900000000");
        user1.setRegisterTime(new Date());
        user1.setLastModifiedTime(new Date());
        user1.setStatus(UserStatus.ACTIVE);
        user1.setSalt("123");

        userMapper.create(user1);
        referrerRelationMapper.create(referrerRelationModel);
        int count = referrerRelationMapper.findReferrerCountByReferrerLoginName("test11");
        assertEquals(count,1);
    }
}
