package com.tuotiansudai.api.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppNodeListService;
import com.tuotiansudai.dto.AnnouncementManagementDto;
import com.tuotiansudai.repository.mapper.AnnouncementManagementMapper;
import com.tuotiansudai.repository.model.AnnouncementManagementModel;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppNodeListServiceImpl implements MobileAppNodeListService {
    @Autowired
    private AnnouncementManagementMapper announcementManagementMapper;

    @Override
    public BaseResponseDto generateNodeList(NodeListRequestDto nodeListRequestDto) {
        Integer index = nodeListRequestDto.getIndex();
        Integer pageSize = nodeListRequestDto.getPageSize();
        String termId = nodeListRequestDto.getTermId();
        NodeListResponseDataDto dtoData = new NodeListResponseDataDto();

        if(index == null || index <= 0){
            index = 1;
        }
        if(pageSize == null || pageSize <= 0){
            pageSize = 10;
        }
        int count = announcementManagementMapper.findAnnouncementManagementCount(null,null);

        List<AnnouncementManagementModel> announcementManagementDtos = announcementManagementMapper.findAnnouncementManagement(null, null, (index - 1) * pageSize, pageSize);

        List<NodeDetailResponseDataDto> nodeDetailResponseDataDtos = Lists.transform(announcementManagementDtos, new Function<AnnouncementManagementModel, NodeDetailResponseDataDto>() {
            @Override
            public NodeDetailResponseDataDto apply(AnnouncementManagementModel input) {
                return new NodeDetailResponseDataDto(input);
            }
        });

        dtoData.setTotalCount(count);
        dtoData.setIndex(index);
        dtoData.setPageSize(pageSize);
        dtoData.setNodeList(nodeDetailResponseDataDtos);
        dtoData.setTermId(termId);
        BaseResponseDto dto = new BaseResponseDto();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);
        return dto;
    }
}
