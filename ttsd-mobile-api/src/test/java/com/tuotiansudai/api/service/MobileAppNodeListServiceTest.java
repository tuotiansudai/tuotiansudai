package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.NodeListRequestDto;
import com.tuotiansudai.api.dto.NodeListResponseDataDto;
import com.tuotiansudai.api.service.impl.MobileAppNodeListServiceImpl;
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

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MobileAppNodeListServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppNodeListServiceImpl mobileAppNodeListService;
    @Mock
    private AnnouncementManagementMapper announcementManagementMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldGenerateNodeListIsOk(){
        AnnouncementManagementModel announcementManagementModel1 = fakeAnnouncementManagementModel();
        AnnouncementManagementModel announcementManagementModel2 = fakeAnnouncementManagementModel();
        announcementManagementModel1.setContent("content1");
        announcementManagementModel2.setContent("content2");
        List<AnnouncementManagementModel> announcementManagementModels = Lists.newArrayList();
        announcementManagementModels.add(announcementManagementModel1);
        announcementManagementModels.add(announcementManagementModel2);
        when(announcementManagementMapper.findAnnouncementManagement(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(announcementManagementModels);
        when(announcementManagementMapper.findAnnouncementManagementCount(anyLong(), anyString())).thenReturn(2);
        NodeListRequestDto nodeListRequestDto = new NodeListRequestDto();
        nodeListRequestDto.setIndex(1);
        nodeListRequestDto.setPageSize(2);
        nodeListRequestDto.setTermId("termId");
        BaseResponseDto<NodeListResponseDataDto> baseDto = mobileAppNodeListService.generateNodeList(nodeListRequestDto);
        assertTrue(baseDto.isSuccess());
        assertEquals(2, baseDto.getData().getTotalCount().intValue());
        assertEquals("content2",baseDto.getData().getNodeList().get(1).getContent());
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
