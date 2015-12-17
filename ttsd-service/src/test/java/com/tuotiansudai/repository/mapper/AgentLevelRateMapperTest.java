package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AgentLevelRateModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class AgentLevelRateMapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AgentLevelRateMapper agentLevelRateMapper;

    @Test
    public void shouldCreateAgentLevelRateIsSuccess(){
        UserModel userModel = createFakeUser();
        AgentLevelRateModel agentLevelRateModel = new AgentLevelRateModel();
        agentLevelRateModel.setLoginName(userModel.getLoginName());
        agentLevelRateModel.setInputTime(new Date());
        agentLevelRateModel.setLevel(2);
        agentLevelRateModel.setRate(2.0d);
        agentLevelRateModel.setUpdateTime(new Date());
        agentLevelRateMapper.create(agentLevelRateModel);
        assertNotNull(agentLevelRateModel.getId());
    }

    @Test
    public void shouldFindAgentLevelRateByLoginNameAndLevel(){
        UserModel userModel = createFakeUser();
        AgentLevelRateModel agentLevelRateModel = new AgentLevelRateModel();
        agentLevelRateModel.setLoginName(userModel.getLoginName());
        agentLevelRateModel.setInputTime(new Date());
        agentLevelRateModel.setLevel(2);
        agentLevelRateModel.setRate(2.0d);
        agentLevelRateModel.setUpdateTime(new Date());
        agentLevelRateMapper.create(agentLevelRateModel);

        AgentLevelRateModel agentLevelRateModel1 = agentLevelRateMapper.findAgentLevelRateByLoginNameAndLevel(userModel.getLoginName(),2);
        assertNotNull(agentLevelRateModel1);
    }
    @Test
    public void shouldDeleteIsSuccess(){
        UserModel userModel = createFakeUser();
        AgentLevelRateModel agentLevelRateModel = new AgentLevelRateModel();
        agentLevelRateModel.setLoginName(userModel.getLoginName());
        agentLevelRateModel.setInputTime(new Date());
        agentLevelRateModel.setLevel(2);
        agentLevelRateModel.setRate(2.0d);
        agentLevelRateModel.setUpdateTime(new Date());
        agentLevelRateMapper.create(agentLevelRateModel);

        agentLevelRateMapper.delete(agentLevelRateModel.getId());

        AgentLevelRateModel agentLevelRateModel1 = agentLevelRateMapper.findAgentLevelRateById(agentLevelRateModel.getId());

        assertNull(agentLevelRateModel1);

    }
    @Test
    public void shouldUpdateIsSuccess(){
        UserModel userModel = createFakeUser();
        AgentLevelRateModel agentLevelRateModel = new AgentLevelRateModel();
        agentLevelRateModel.setLoginName(userModel.getLoginName());
        agentLevelRateModel.setInputTime(new Date());
        agentLevelRateModel.setLevel(2);
        agentLevelRateModel.setRate(2.0d);
        agentLevelRateModel.setUpdateTime(new Date());
        agentLevelRateMapper.create(agentLevelRateModel);

        agentLevelRateModel.setLevel(3);
        agentLevelRateMapper.update(agentLevelRateModel);

        AgentLevelRateModel agentLevelRateModel1 = agentLevelRateMapper.findAgentLevelRateById(agentLevelRateModel.getId());

        assertEquals(3,agentLevelRateModel1.getLevel());
    }

    private UserModel createFakeUser() {
        UserModel model = new UserModel();
        model.setLoginName("loginName");
        model.setPassword("password");
        model.setEmail("loginName@abc.com");
        model.setMobile("12900000000");
        model.setRegisterTime(new Date());
        model.setStatus(UserStatus.ACTIVE);
        model.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(model);
        return model;
    }
}
