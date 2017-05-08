package com.tuotiansudai.repository.mapper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
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

        List<UserBillModel> actualModels = userBillMapper.findByLoginName(fakeUser.getLoginName());

        assertThat(actualModels.size(), is(1));

    }

    @Test
    public void shouldFindUserBillsIsSuccess(){
        UserModel fakeUser = this.getFakeUser();
        userMapper.create(fakeUser);
        getUserBillModel(UserBillOperationType.TI_BALANCE,UserBillBusinessType.ACTIVITY_REWARD,fakeUser.getLoginName());
        getUserBillModel(UserBillOperationType.TO_FREEZE,UserBillBusinessType.ADVANCE_REPAY,fakeUser.getLoginName());

        List<UserBillModel> userBillModelsTi = userBillMapper.findUserBills(Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("loginName", fakeUser.getLoginName())
                .put("userBillOperationTypes",Lists.newArrayList(UserBillOperationType.TI_BALANCE))
                .put("indexPage", 0)
                .put("pageSize", 10).build()));

        List<UserBillModel> userBillModelsTo = userBillMapper.findUserBills(Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("loginName", fakeUser.getLoginName())
                .put("userBillOperationTypes",Lists.newArrayList(UserBillOperationType.TO_FREEZE))
                .put("indexPage", 0)
                .put("pageSize", 10).build()));

        assertEquals(1,userBillModelsTi.size());
        assertEquals(UserBillBusinessType.ACTIVITY_REWARD,userBillModelsTi.get(0).getBusinessType());
        assertEquals(UserBillOperationType.TI_BALANCE,userBillModelsTi.get(0).getOperationType());


        assertEquals(1,userBillModelsTo.size());
        assertEquals(UserBillBusinessType.ADVANCE_REPAY,userBillModelsTo.get(0).getBusinessType());
        assertEquals(UserBillOperationType.TO_FREEZE,userBillModelsTo.get(0).getOperationType());




    }
    private void getUserBillModel(UserBillOperationType userBillOperationType,UserBillBusinessType userBillBusinessType,String loginName){
        UserBillModel userBillModel = new UserBillModel();
        userBillModel.setLoginName(loginName);
        userBillModel.setAmount(1);
        userBillModel.setBalance(1);
        userBillModel.setFreeze(1);
        userBillModel.setOperationType(userBillOperationType);
        userBillModel.setBusinessType(userBillBusinessType);

        userBillMapper.create(userBillModel);
    }
    public UserModel getFakeUser() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("loginName");
        userModelTest.setPassword("password");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile(RandomStringUtils.randomNumeric(11));
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
        params.put("userBillBusinessType", billBusinessTypes);
        params.put("indexPage", 1);
        params.put("startTime", new Date());
        params.put("endTime", new Date());
        params.put("pageSize", 10);
        List<UserBillModel> list = userBillMapper.findUserBills(params);
        assertTrue(list.size() <= 10);
    }

}
