package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.NodeDetailRequestDto;
import com.tuotiansudai.api.dto.NodeDetailResponseDataDto;
import com.tuotiansudai.api.service.impl.MobileAppNodeDetailServiceImpl;
import com.tuotiansudai.repository.mapper.AnnouncementManagementMapper;
import com.tuotiansudai.repository.model.AnnouncementManagementModel;
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
    private AnnouncementManagementMapper announcementManagementMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldGenerateNodeDetailIsOk(){
        ReflectionTestUtils.setField(mobileAppNodeDetailService, "urlPattern", "(\\\\/upload\\\\/).*?(.jpg|.gif|.jpeg|.png)");
        ReflectionTestUtils.setField(mobileAppNodeDetailService, "domainName", "http://localhost:8080");
        AnnouncementManagementModel announcementManagementModel1 = fakeAnnouncementManagementModel();
        when(announcementManagementMapper.findById(anyLong())).thenReturn(announcementManagementModel1);
        NodeDetailRequestDto nodeListRequestDto = new NodeDetailRequestDto();
        nodeListRequestDto.setNodeId("" + announcementManagementModel1.getId());

        BaseResponseDto<NodeDetailResponseDataDto> baseDto = mobileAppNodeDetailService.generateNodeDetail(nodeListRequestDto);
        assertTrue(baseDto.isSuccess());

    }

    private AnnouncementManagementModel fakeAnnouncementManagementModel(){
        AnnouncementManagementModel announcementManagementModel = new AnnouncementManagementModel();
        announcementManagementModel.setId(idGenerator.generate());
        announcementManagementModel.setTitle("tile");
        announcementManagementModel.setContent("content");
        announcementManagementModel.setCreatedTime(new Date());
        announcementManagementModel.setUpdateTime(new Date());
        announcementManagementModel.setShowOnHome(false);
        return announcementManagementModel;
    }


}
