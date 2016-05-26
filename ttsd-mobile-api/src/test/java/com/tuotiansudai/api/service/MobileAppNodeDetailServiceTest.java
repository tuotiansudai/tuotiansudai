package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NodeDetailRequestDto;
import com.tuotiansudai.api.dto.v1_0.NodeDetailResponseDataDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppNodeDetailServiceImpl;
import com.tuotiansudai.repository.mapper.AnnounceMapper;
import com.tuotiansudai.repository.model.AnnounceModel;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MobileAppNodeDetailServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppNodeDetailServiceImpl mobileAppNodeDetailService;
    @Mock
    private AnnounceMapper announceMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldGenerateNodeDetailIsOk(){
        ReflectionTestUtils.setField(mobileAppNodeDetailService, "urlPattern", "(\\\\/upload\\\\/).*?(.jpg|.gif|.jpeg|.png)");
        ReflectionTestUtils.setField(mobileAppNodeDetailService, "domainName", "http://localhost:8080");
        AnnounceModel announceModel1 = fakeAnnounceModel();
        when(announceMapper.findById(anyLong())).thenReturn(announceModel1);
        NodeDetailRequestDto nodeListRequestDto = new NodeDetailRequestDto();
        nodeListRequestDto.setNodeId("" + announceModel1.getId());

        BaseResponseDto<NodeDetailResponseDataDto> baseDto = mobileAppNodeDetailService.generateNodeDetail(nodeListRequestDto);
        assertTrue(baseDto.isSuccess());

    }

    private AnnounceModel fakeAnnounceModel(){
        AnnounceModel announceModel = new AnnounceModel();
        announceModel.setId(idGenerator.generate());
        announceModel.setTitle("tile");
        announceModel.setContent("content");
        announceModel.setCreatedTime(new Date());
        announceModel.setUpdateTime(new Date());
        announceModel.setShowOnHome(false);
        return announceModel;
    }


}
