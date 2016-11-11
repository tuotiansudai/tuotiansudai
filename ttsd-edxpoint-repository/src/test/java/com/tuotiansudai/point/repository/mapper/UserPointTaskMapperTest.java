package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.repository.model.PointTaskModel;
import com.tuotiansudai.point.repository.model.UserPointTaskModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserPointTaskMapperTest {

    @Autowired
    private PointTaskMapper pointTaskMapper;

    @Autowired
    private UserPointTaskMapper userPointTaskMapper;

    @Test
    public void shouldCreatePointBillModel() throws Exception {
        PointTaskModel pointTaskModel = new PointTaskModel();
        pointTaskModel.setName(PointTask.REGISTER);
        pointTaskModel.setPoint(1);
        pointTaskModel.setActive(true);
        pointTaskModel.setMaxLevel(1);
        pointTaskModel.setMultiple(false);
        pointTaskModel.setCreatedTime(new Date());
        pointTaskMapper.create(pointTaskModel);

        String fakeLoginName = "fakeUser";

        userPointTaskMapper.create(new UserPointTaskModel(fakeLoginName, pointTaskModel.getId(), 1000, 1));


        assertThat(userPointTaskMapper.findByLoginName(fakeLoginName).size(), is(1));
    }


}
