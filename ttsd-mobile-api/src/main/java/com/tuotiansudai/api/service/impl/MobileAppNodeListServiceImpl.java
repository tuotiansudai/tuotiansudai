package com.tuotiansudai.api.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppNodeListService;
import com.tuotiansudai.repository.mapper.AnnounceMapper;
import com.tuotiansudai.repository.model.AnnounceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppNodeListServiceImpl implements MobileAppNodeListService {
    @Autowired
    private AnnounceMapper announceMapper;

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
        int count = announceMapper.findAnnounceCount(null,null);

        List<AnnounceModel> announceDtos = announceMapper.findAnnounce(null, null, (index - 1) * pageSize, pageSize);

        List<NodeDetailResponseDataDto> nodeDetailResponseDataDtos = Lists.transform(announceDtos, new Function<AnnounceModel, NodeDetailResponseDataDto>() {
            @Override
            public NodeDetailResponseDataDto apply(AnnounceModel input) {
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
