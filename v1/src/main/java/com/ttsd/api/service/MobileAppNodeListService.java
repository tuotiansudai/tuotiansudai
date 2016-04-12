package com.ttsd.api.service;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.NodeListRequestDto;

public interface MobileAppNodeListService {
    BaseResponseDto generateNodeList(NodeListRequestDto nodeListRequestDto);
}
