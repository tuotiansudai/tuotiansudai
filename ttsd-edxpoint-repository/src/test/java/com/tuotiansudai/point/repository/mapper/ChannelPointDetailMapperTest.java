package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.ChannelPointDetailModel;
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
public class ChannelPointDetailMapperTest {
    @Autowired
    private ChannelPointDetailMapper channelPointDetailMapper;
    @Autowired
    private ChannelPointMapper channelPointMapper;

    @Test
    public void shouldCreateIsSuccess() {
        ChannelPointModel channelPointModel = generateFakeChannelPoint();
        channelPointMapper.create(channelPointModel);
        ChannelPointDetailModel channelPointDetailModel = generateDetail(channelPointModel);
        channelPointDetailMapper.create(channelPointDetailModel);
        ChannelPointDetailModel channelPointDetailModelReturn = channelPointDetailMapper.findById(channelPointDetailModel.getId());

        assertEquals(channelPointDetailModelReturn.getChannel(), channelPointDetailModel.getChannel());
        assertEquals(channelPointDetailModelReturn.getMobile(), channelPointDetailModel.getMobile());
        assertEquals(channelPointDetailModelReturn.getLoginName(), channelPointDetailModel.getLoginName());
        assertEquals(channelPointDetailModelReturn.getChannelPointId(), channelPointModel.getId());
        assertEquals(channelPointDetailModelReturn.getPoint(), channelPointDetailModel.getPoint());
        assertEquals(channelPointDetailModelReturn.getUserName(), channelPointDetailModel.getUserName());
        assertEquals(channelPointDetailModelReturn.isSuccess(), true);


    }

    private ChannelPointDetailModel generateDetail(ChannelPointModel channelPointModel) {
        ChannelPointDetailModel channelPointDetailModel = new ChannelPointDetailModel();
        channelPointDetailModel.setLoginName("loginName");
        channelPointDetailModel.setUserName("userName");
        channelPointDetailModel.setCreatedTime(new Date());
        channelPointDetailModel.setChannel("channel");
        channelPointDetailModel.setChannelPointId(channelPointModel.getId());
        channelPointDetailModel.setMobile("mobile");
        channelPointDetailModel.setSuccess(true);
        return channelPointDetailModel;
    }

    private ChannelPointModel generateFakeChannelPoint() {
        ChannelPointModel channelPointModel = new ChannelPointModel();
        channelPointModel.setSerialNo("Test_1000");
        channelPointModel.setHeadCount(1000);
        channelPointModel.setTotalPoint(600000l);
        channelPointModel.setCreatedBy("loginName");
        channelPointModel.setCreatedTime(new Date());
        return channelPointModel;
    }


}
