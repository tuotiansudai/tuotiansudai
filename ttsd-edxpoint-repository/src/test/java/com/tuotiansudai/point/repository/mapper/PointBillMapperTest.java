package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class PointBillMapperTest {

    @Autowired
    private PointBillMapper pointBillMapper;

    @Test
    public void shouldCreatePointBillModel() throws Exception {
        String fakeLoginName = "fakeUser";

        PointBillModel pointBillModel = new PointBillModel(fakeLoginName, null, 1, 1, PointBusinessType.EXCHANGE, "note","mobile","userName");

        pointBillMapper.create(pointBillModel);

        List<PointBillModel> pointBillModelList = pointBillMapper.findByLoginName(fakeLoginName);

        assertThat(pointBillModelList.size(), is(1));
    }

    @Test
    public void shouldFindPointBillPaginationIsOk() {
        String fakeLoginName = "fakeUser";
        PointBillModel pointBillModel = new PointBillModel(fakeLoginName, null, 1, 0, PointBusinessType.LOTTERY, "note","mobile","userName");
        pointBillMapper.create(pointBillModel);
        PointBillModel pointBillModel1 = new PointBillModel(fakeLoginName, null, 1, 0, PointBusinessType.EXCHANGE, "note","mobile","userName");
        pointBillMapper.create(pointBillModel1);
        List<PointBillModel> pointBillModelList = pointBillMapper.findPointBillPagination(fakeLoginName, null, 0, 10, null, null, Arrays.asList(PointBusinessType.EXCHANGE, PointBusinessType.LOTTERY));
        assertThat(pointBillModelList.size(), is(2));
    }

    @Test
    public void shouldFindSumPointByLoginNameAndBusinessTypeIsOk() {
        String fakeLoginName = "fakeUser";
        PointBillModel pointBillModel = new PointBillModel(fakeLoginName, null, 10, 0, PointBusinessType.ACTIVITY, "note","mobile","userName");
        pointBillMapper.create(pointBillModel);
        PointBillModel pointBillModel1 = new PointBillModel(fakeLoginName, null, 200, 0, PointBusinessType.ACTIVITY, "note","mobile","userName");
        pointBillMapper.create(pointBillModel1);
        Date startDate = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));
        Date endDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        long sumPoint = pointBillMapper.findSumPointByLoginNameAndBusinessType(fakeLoginName, startDate, endDate, Collections.singletonList(PointBusinessType.ACTIVITY));
        assertEquals(sumPoint, 210);
    }
}
