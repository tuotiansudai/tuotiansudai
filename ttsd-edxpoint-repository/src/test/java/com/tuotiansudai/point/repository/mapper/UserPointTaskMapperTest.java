package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.PointTaskModel;
import com.tuotiansudai.point.repository.model.UserPointTaskModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
        String fakeLoginName = "fakeUser";

        List<PointTaskModel> pointTaskModels = pointTaskMapper.findPointTaskPagination(0, 10);
        for (PointTaskModel pointTaskModel : pointTaskModels) {
            userPointTaskMapper.create(new UserPointTaskModel(fakeLoginName, pointTaskModel.getId(), 1000, 1));
        }

        assertThat(userPointTaskMapper.findByLoginName(fakeLoginName).size(), is(pointTaskModels.size()));
    }
}
