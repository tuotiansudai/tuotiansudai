package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NodeDetailRequestDto;
import com.tuotiansudai.api.dto.v1_0.NodeDetailResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppNodeDetailService;
import com.tuotiansudai.repository.mapper.AnnounceMapper;
import com.tuotiansudai.repository.model.AnnounceModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MobileAppNodeDetailServiceImpl implements MobileAppNodeDetailService {
    @Value("${mobile.app.imageUrl.pattern}")
    private String urlPattern;
    @Value("${web.server}")
    private String domainName;
    @Autowired
    private AnnounceMapper announceMapper;
    @Override
    public BaseResponseDto generateNodeDetail(NodeDetailRequestDto requestDto) {
        BaseResponseDto<NodeDetailResponseDataDto> dto = new BaseResponseDto<>();
        String nodeId = requestDto.getNodeId();
        if(StringUtils.isEmpty(nodeId)){
            return new BaseResponseDto(ReturnMessage.NODE_ID_IS_NOT_EXIST.getCode(),ReturnMessage.NODE_ID_IS_NOT_EXIST.getMsg());
        }
        AnnounceModel announceModel = announceMapper.findById(Long.parseLong(nodeId));
        if(announceModel == null){
            return new BaseResponseDto(ReturnMessage.NODE_ID_IS_NOT_EXIST.getCode(),ReturnMessage.NODE_ID_IS_NOT_EXIST.getMsg());
        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        NodeDetailResponseDataDto dataDto = new NodeDetailResponseDataDto(announceModel,true);
        dataDto.addDomainNameToImageUrl(urlPattern, domainName);
        dto.setData(dataDto);
        return dto;
    }
}
