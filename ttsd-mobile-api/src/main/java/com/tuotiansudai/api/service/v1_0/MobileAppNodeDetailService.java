package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NodeDetailRequestDto;
import com.tuotiansudai.api.dto.v1_0.NodeDetailResponseDataDto;

public interface MobileAppNodeDetailService {
    BaseResponseDto<NodeDetailResponseDataDto> generateNodeDetail(NodeDetailRequestDto requestDto);
}
