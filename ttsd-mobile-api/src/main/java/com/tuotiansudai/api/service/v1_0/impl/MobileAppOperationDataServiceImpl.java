package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppNodeDetailService;
import com.tuotiansudai.api.service.v1_0.MobileAppOperationDataService;
import com.tuotiansudai.repository.mapper.AnnounceMapper;
import com.tuotiansudai.repository.model.AnnounceModel;
import com.tuotiansudai.service.OperationDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MobileAppOperationDataServiceImpl implements MobileAppOperationDataService {

    @Autowired
    private OperationDataService operationDataService;

    @Override
    public BaseResponseDto generatorOperationData(BaseParamDto requestDto) {
        BaseResponseDto<NodeDetailResponseDataDto> dto = new BaseResponseDto<>();


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
