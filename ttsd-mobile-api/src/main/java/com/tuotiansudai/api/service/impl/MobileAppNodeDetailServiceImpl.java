package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.NodeDetailRequestDto;
import com.tuotiansudai.api.dto.NodeDetailResponseDataDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppNodeDetailService;
import com.tuotiansudai.repository.mapper.AnnouncementManagementMapper;
import com.tuotiansudai.repository.model.AnnouncementManagementModel;
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
    private AnnouncementManagementMapper announcementManagementMapper;
    @Override
    public BaseResponseDto generateNodeDetail(NodeDetailRequestDto requestDto) {
        BaseResponseDto<NodeDetailResponseDataDto> dto = new BaseResponseDto<>();
        String nodeId = requestDto.getNodeId();
        if(StringUtils.isEmpty(nodeId)){
            return new BaseResponseDto(ReturnMessage.NODE_ID_IS_NOT_EXIST.getCode(),ReturnMessage.NODE_ID_IS_NOT_EXIST.getMsg());
        }
        AnnouncementManagementModel announcementManagementModel = announcementManagementMapper.findById(Long.parseLong(nodeId));
        if(announcementManagementModel == null){
            return new BaseResponseDto(ReturnMessage.NODE_ID_IS_NOT_EXIST.getCode(),ReturnMessage.NODE_ID_IS_NOT_EXIST.getMsg());
        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        NodeDetailResponseDataDto dataDto = new NodeDetailResponseDataDto(announcementManagementModel);
        dataDto.addDomainNameToImageUrl(urlPattern, domainName);
        dto.setData(dataDto);
        return dto;
    }
}
