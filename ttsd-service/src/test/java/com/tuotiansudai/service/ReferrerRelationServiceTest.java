package com.tuotiansudai.service;

import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ReferrerRelationServiceTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private ReferrerRelationService referrerRelationService;

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
        user2.setLastModifiedTime(new Date());
        user2.setStatus(UserStatus.ACTIVE);
        user2.setSalt("123");
        user2.setReferrer("test1");
        userMapper.create(user2);

        referrerRelationService.generateRelation(user1.getLoginName(), user2.getLoginName());

        List<ReferrerRelationModel> models = referrerRelationMapper.findByLoginName(user2.getLoginName());

        assertThat(models.size(), is(1));
    }
}
