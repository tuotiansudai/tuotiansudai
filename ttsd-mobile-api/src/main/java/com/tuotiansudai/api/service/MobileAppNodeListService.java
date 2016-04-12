package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.NodeListRequestDto;

public interface MobileAppNodeListService {
    BaseResponseDto generateNodeList(NodeListRequestDto nodeListRequestDto);
}
