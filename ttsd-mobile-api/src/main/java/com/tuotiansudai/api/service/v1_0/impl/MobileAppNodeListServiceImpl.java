package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppNodeListService;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.message.repository.mapper.AnnounceMapper;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppNodeListServiceImpl implements MobileAppNodeListService {
    @Autowired
    private AnnounceMapper announceMapper;

    @Autowired
    private PageValidUtils pageValidUtils;

    @Override
    public BaseResponseDto<NodeListResponseDataDto> generateNodeList(NodeListRequestDto nodeListRequestDto) {
        Integer index = nodeListRequestDto.getIndex();
        Integer pageSize = nodeListRequestDto.getPageSize();
        String termId = nodeListRequestDto.getTermId();

        pageSize = pageValidUtils.validPageSizeLimit(pageSize);

        int count = announceMapper.findAnnounceCount(null);

        List<NodeDetailResponseDataDto> nodeDetailResponseDataDtos = Lists.transform(announceMapper.findAnnounce(null, PaginationUtil.calculateOffset(index, pageSize, count), pageSize),
                input -> new NodeDetailResponseDataDto(input,false));

        NodeListResponseDataDto dtoData = new NodeListResponseDataDto();
        dtoData.setTotalCount(count);
        dtoData.setIndex(PaginationUtil.validateIndex(index, pageSize, count));
        dtoData.setPageSize(pageSize);
        dtoData.setNodeList(nodeDetailResponseDataDtos);
        dtoData.setTermId(termId);
        BaseResponseDto<NodeListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);
        return dto;
    }
}
