package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReferrerListRequestDto;
import com.tuotiansudai.api.service.MobileAppReferrerListService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppReferrerListServiceImpl implements MobileAppReferrerListService {
    @Override
    public BaseResponseDto generateReferrerList(ReferrerListRequestDto referrerListRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }
}
