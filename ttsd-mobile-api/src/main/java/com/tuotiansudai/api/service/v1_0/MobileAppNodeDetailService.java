package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NodeDetailRequestDto;

public interface MobileAppNodeDetailService {
    BaseResponseDto generateNodeDetail(NodeDetailRequestDto requestDto);
}
