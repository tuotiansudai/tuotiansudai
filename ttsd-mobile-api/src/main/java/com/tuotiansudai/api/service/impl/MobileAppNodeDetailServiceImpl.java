package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.NodeDetailRequestDto;
import com.tuotiansudai.api.service.MobileAppNodeDetailService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppNodeDetailServiceImpl implements MobileAppNodeDetailService {
    @Override
    public BaseResponseDto generateNodeDetail(NodeDetailRequestDto requestDto) {
        throw new NotImplementedException(getClass().getName());
    }
}
