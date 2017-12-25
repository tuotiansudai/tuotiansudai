package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.ChannelPointModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ChannelPointMapperTest {
    @Autowired
    private ChannelPointMapper channelPointMapper;

    @Test
    public void shouldCreateIsSuccess() {
        ChannelPointModel channelPointModel = generateFakeChannelPoint();
        channelPointMapper.create(channelPointModel);

        ChannelPointModel channelPointModelReturn = channelPointMapper.findById(channelPointModel.getId());
        assertEquals(channelPointModelReturn.getSerialNo(), channelPointModel.getSerialNo());
        assertEquals(channelPointModelReturn.getCreatedBy(), channelPointModel.getCreatedBy());
        assertEquals(channelPointModelReturn.getHeadCount(), channelPointModel.getHeadCount());
        assertEquals(channelPointModelReturn.getTotalPoint(), channelPointModel.getTotalPoint());
    }

    private ChannelPointModel generateFakeChannelPoint() {
        ChannelPointModel channelPointModel = new ChannelPointModel();
        channelPointModel.setSerialNo("Test_1000");
        channelPointModel.setHeadCount(1000);
        channelPointModel.setTotalPoint(500000l);
        channelPointModel.setCreatedBy("loginName");
        channelPointModel.setCreatedTime(new Date());
        return channelPointModel;
    }
}
