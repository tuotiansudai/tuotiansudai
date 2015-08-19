package com.ttsd.api.service.impl;

import com.esoft.archer.node.model.Node;
import com.ttsd.api.dao.MobileAppNodeListDao;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppNodeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppNodeListServiceImpl implements MobileAppNodeListService {

    @Autowired
    private MobileAppNodeListDao mobileAppNodeListDao;

    @Override
    public BaseResponseDto generateNodeList(NodeListRequestDto nodeListRequestDto, String baseUrl) {
        Integer index = nodeListRequestDto.getIndex();
        Integer pageSize = nodeListRequestDto.getPageSize();
        String termId = nodeListRequestDto.getTermId();

        Integer nodeTotalCount = mobileAppNodeListDao.getTotalCount(termId);
        List<Node> nodeList = mobileAppNodeListDao.getNodeList(index, pageSize, termId);

        NodeListResponseDataDto dtoData = new NodeListResponseDataDto();
        List<NodeDetailResponseDataDto> nodeDtoList = new ArrayList<>();
        if (nodeDtoList != null) {
            for (Node node : nodeList) {
                nodeDtoList.add(new NodeDetailResponseDataDto(node, baseUrl, false));
            }
        }
        dtoData.setIndex(index);
        dtoData.setPageSize(pageSize);
        dtoData.setTermId(termId);
        dtoData.setTotalCount(nodeTotalCount);
        dtoData.setNodeList(nodeDtoList);

        BaseResponseDto dto = new BaseResponseDto();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);
        return dto;
    }
}
