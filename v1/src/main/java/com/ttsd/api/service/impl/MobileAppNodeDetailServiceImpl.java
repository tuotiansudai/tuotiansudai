package com.ttsd.api.service.impl;

import com.esoft.archer.node.model.Node;
import com.ttsd.api.dao.MobileAppNodeDetailDao;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.NodeDetailRequestDto;
import com.ttsd.api.dto.NodeDetailResponseDataDto;
import com.ttsd.api.dto.ReturnMessage;
import com.ttsd.api.service.MobileAppNodeDetailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MobileAppNodeDetailServiceImpl implements MobileAppNodeDetailService {
    @Resource
    private MobileAppNodeDetailDao mobileAppNodeDetailDao;

    @Value("${mobile.app.imageUrl.pattern}")
    private String urlPattern;
    @Value("${announcement.image.domain}")
    private String domainName;

    @Override
    public BaseResponseDto generateNodeDetail(NodeDetailRequestDto requestDto) {
        String nodeId = requestDto.getNodeId();
        Node node = mobileAppNodeDetailDao.getNodeById(nodeId);
        BaseResponseDto<NodeDetailResponseDataDto> dto = new BaseResponseDto<>();
        if(node == null) {
            dto.setCode(ReturnMessage.NODE_ID_IS_NOT_EXIST.getCode());
            dto.setMessage(ReturnMessage.NODE_ID_IS_NOT_EXIST.getMsg());
        }else {
            dto.setCode(ReturnMessage.SUCCESS.getCode());
            dto.setMessage(ReturnMessage.SUCCESS.getMsg());
            NodeDetailResponseDataDto dataDto = new NodeDetailResponseDataDto(node,true);
            dataDto.addDomainNameToImageUrl(urlPattern, domainName);
            dto.setData(dataDto);
        }
        return dto;
    }
}
