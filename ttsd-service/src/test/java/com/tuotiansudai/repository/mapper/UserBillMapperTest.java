package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserBillMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserBillMapper userBillMapper;

    @Test
    public void shouldCreateUserBill() throws Exception {
        UserModel fakeUser = this.getFakeUser();
        userMapper.create(fakeUser);

        UserBillModel userBillModel = new UserBillModel();
        userBillModel.setLoginName(fakeUser.getLoginName());
        userBillModel.setAmount(1);
        userBillModel.setBalance(1);
        userBillModel.setFreeze(1);
        userBillModel.setOperationType(UserBillOperationType.FREEZE);
        userBillModel.setBusinessType(UserBillBusinessType.RECHARGE_SUCCESS);

        userBillMapper.create(userBillModel);

        UserBillModel actualModel = userBillMapper.findByLoginName(fakeUser.getLoginName());

        assertTrue(actualModel.getId() > 0);

    }

    public UserModel getFakeUser() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("loginName");
        userModelTest.setPassword("password");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    @Test
    public void userBillListTest(){
        Map<String, Object> params = new HashMap<>();
        List<UserBillBusinessType> billBusinessTypes = Lists.newArrayList();
        billBusinessTypes.add(UserBillBusinessType.ACTIVITY_REWARD);
        billBusinessTypes.add(UserBillBusinessType.ADVANCE_REPAY);
        params.put("userBillBusinessType",billBusinessTypes);
        params.put("currentPage",1);
        params.put("startTime",new Date());
        params.put("endTime",new Date());
        params.put("pageSize",10);
        List<UserBillModel> list = userBillMapper.findUserBills(params);
        int count = userBillMapper.findUserBillsCount(params);
        assertTrue(list.size() == count);
    }

}
