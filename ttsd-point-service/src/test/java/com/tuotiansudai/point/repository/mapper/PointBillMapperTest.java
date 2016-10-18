package com.tuotiansudai.point.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class PointBillMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PointBillMapper pointBillMapper;

    @Test
    public void shouldCreatePointBillModel() throws Exception {
        UserModel fakeUserModel = this.createFakeUserModel();

        PointBillModel pointBillModel = new PointBillModel(fakeUserModel.getLoginName(), null, 1, PointBusinessType.EXCHANGE, "note");

        pointBillMapper.create(pointBillModel);

        List<PointBillModel> pointBillModelList = pointBillMapper.findByLoginName(fakeUserModel.getLoginName());

        assertThat(pointBillModelList.size(), is(1));
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

    @Test
    public void shouldFindPointBillPaginationIsOk() {
        UserModel fakeUserModel = this.createFakeUserModel();
        PointBillModel pointBillModel = new PointBillModel(fakeUserModel.getLoginName(), null, 1, PointBusinessType.LOTTERY, "note");
        pointBillMapper.create(pointBillModel);
        PointBillModel pointBillModel1 = new PointBillModel(fakeUserModel.getLoginName(), null, 1, PointBusinessType.EXCHANGE, "note");
        pointBillMapper.create(pointBillModel1);
        List<PointBillModel> pointBillModelList = pointBillMapper.findPointBillPagination(fakeUserModel.getLoginName(), 0, 10, null, null, Lists.newArrayList(PointBusinessType.EXCHANGE, PointBusinessType.LOTTERY));
        assertThat(pointBillModelList.size(), is(2));
    }

    @Test
    public void shouldFindSumPointByLoginNameAndBusinessTypeIsOk(){
        UserModel fakeUserModel = this.createFakeUserModel();
        PointBillModel pointBillModel = new PointBillModel(fakeUserModel.getLoginName(), null, 10, PointBusinessType.ACTIVITY, "note");
        pointBillMapper.create(pointBillModel);
        PointBillModel pointBillModel1 = new PointBillModel(fakeUserModel.getLoginName(), null, 200, PointBusinessType.ACTIVITY, "note");
        pointBillMapper.create(pointBillModel1);
        Date startDate = DateUtils.addDays(DateTime.now().toDate(),-1);
        Date endDate = DateUtils.addDays(DateTime.now().toDate(),1);
        long sumPoint = pointBillMapper.findSumPointByLoginNameAndBusinessType(fakeUserModel.getLoginName(),startDate,endDate,Lists.newArrayList(PointBusinessType.ACTIVITY));
        assertEquals(sumPoint,210);
    }
}
