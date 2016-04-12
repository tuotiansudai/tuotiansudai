package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.NodeDetailRequestDto;

public interface MobileAppNodeDetailService {
    BaseResponseDto generateNodeDetail(NodeDetailRequestDto requestDto);
}
