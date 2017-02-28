package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class PointBillMapperTest {

    @Autowired
    private PointBillMapper pointBillMapper;

    @Test
    public void shouldCreatePointBillModel() throws Exception {
        String fakeLoginName = "fakeUser";

        PointBillModel pointBillModel = new PointBillModel(fakeLoginName, null, 1, PointBusinessType.EXCHANGE, "note");

        pointBillMapper.create(pointBillModel);

        List<PointBillModel> pointBillModelList = pointBillMapper.findByLoginName(fakeLoginName);

        assertThat(pointBillModelList.size(), is(1));
    }

    @Test
    public void shouldFindPointBillPaginationIsOk() {
        String fakeLoginName = "fakeUser";
        PointBillModel pointBillModel = new PointBillModel(fakeLoginName, null, 1, PointBusinessType.LOTTERY, "note");
        pointBillMapper.create(pointBillModel);
        PointBillModel pointBillModel1 = new PointBillModel(fakeLoginName, null, 1, PointBusinessType.EXCHANGE, "note");
        pointBillMapper.create(pointBillModel1);
        List<PointBillModel> pointBillModelList = pointBillMapper.findPointBillPagination(fakeLoginName, null, 0, 10, null, null, Arrays.asList(PointBusinessType.EXCHANGE, PointBusinessType.LOTTERY));
        assertThat(pointBillModelList.size(), is(2));
    }

    @Test
    public void shouldFindSumPointByLoginNameAndBusinessTypeIsOk() {
        String fakeLoginName = "fakeUser";
        PointBillModel pointBillModel = new PointBillModel(fakeLoginName, null, 10, PointBusinessType.ACTIVITY, "note");
        pointBillMapper.create(pointBillModel);
        PointBillModel pointBillModel1 = new PointBillModel(fakeLoginName, null, 200, PointBusinessType.ACTIVITY, "note");
        pointBillMapper.create(pointBillModel1);
        Date startDate = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));
        Date endDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        long sumPoint = pointBillMapper.findSumPointByLoginNameAndBusinessType(fakeLoginName, startDate, endDate, Arrays.asList(PointBusinessType.ACTIVITY));
        assertEquals(sumPoint, 210);
    }
}
