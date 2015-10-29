package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.NodeListRequestDto;
import com.tuotiansudai.api.service.MobileAppNodeListService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppNodeListServiceImpl implements MobileAppNodeListService {
    @Override
    public BaseResponseDto generateNodeList(NodeListRequestDto nodeListRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }
}
