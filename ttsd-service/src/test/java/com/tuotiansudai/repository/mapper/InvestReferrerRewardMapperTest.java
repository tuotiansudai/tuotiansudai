package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class InvestReferrerRewardMapperTest {

    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private InvestReferrerRewardMapper investReferrerRewardMapper;

    @Test
    public void shouldCreateInvestReferrerReward(){
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("helloworld");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);

        InvestModel model = new InvestModel();
        model.setAmount(1000000);
        // 舍弃毫秒数
        Date currentDate = new Date((new Date().getTime()/1000)*1000);
        model.setCreatedTime(currentDate);
        model.setId(idGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(userModelTest.getLoginName());
        model.setLoanId(1);
        model.setSource(InvestSource.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);

        InvestReferrerRewardModel investReferrerRewardModel = new InvestReferrerRewardModel();
        long id = idGenerator.generate();
        investReferrerRewardModel.setId(id);
        investReferrerRewardModel.setStatus(ReferrerRewardStatus.SUCCESS);
        investReferrerRewardModel.setReferrerLoginName(userModelTest.getLoginName());
        investReferrerRewardModel.setTime(new Date());
        investReferrerRewardModel.setBonus(100);
        investReferrerRewardModel.setInvestId(model.getId());
        investReferrerRewardModel.setRoleName(Role.INVESTOR);
        investReferrerRewardMapper.create(investReferrerRewardModel);

        InvestReferrerRewardModel investReferrerRewardModel1 = investReferrerRewardMapper.findById(id);
        assertNotNull(investReferrerRewardModel1);
    }



}
