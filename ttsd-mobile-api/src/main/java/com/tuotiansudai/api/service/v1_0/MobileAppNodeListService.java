package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NodeListRequestDto;
import com.tuotiansudai.api.dto.v1_0.NodeListResponseDataDto;

public interface MobileAppNodeListService {
    BaseResponseDto<NodeListResponseDataDto> generateNodeList(NodeListRequestDto nodeListRequestDto);
}
