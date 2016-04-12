package com.ttsd.api.service;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.NodeDetailRequestDto;

public interface MobileAppNodeDetailService {
    BaseResponseDto generateNodeDetail(NodeDetailRequestDto requestDto);
}
