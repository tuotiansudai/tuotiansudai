package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NodeListRequestDto;
import com.tuotiansudai.api.dto.v1_0.NodeListResponseDataDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppNodeListServiceImpl;
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
    private AnnounceMapper announceMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldGenerateNodeListIsOk(){
        AnnounceModel announceModel1 = fakeAnnounceModel();
        AnnounceModel announceModel2 = fakeAnnounceModel();
        announceModel1.setContent("content1");
        announceModel2.setContent("content2");
        List<AnnounceModel> announceModels = Lists.newArrayList();
        announceModels.add(announceModel1);
        announceModels.add(announceModel2);
        when(announceMapper.findAnnounce(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(announceModels);
        when(announceMapper.findAnnounceCount(anyLong(), anyString())).thenReturn(2);
        NodeListRequestDto nodeListRequestDto = new NodeListRequestDto();
        nodeListRequestDto.setIndex(1);
        nodeListRequestDto.setPageSize(2);
        nodeListRequestDto.setTermId("termId");
        BaseResponseDto<NodeListResponseDataDto> baseDto = mobileAppNodeListService.generateNodeList(nodeListRequestDto);
        assertTrue(baseDto.isSuccess());
        assertEquals(2, baseDto.getData().getTotalCount().intValue());
        assertEquals("content2",baseDto.getData().getNodeList().get(1).getContent());
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
