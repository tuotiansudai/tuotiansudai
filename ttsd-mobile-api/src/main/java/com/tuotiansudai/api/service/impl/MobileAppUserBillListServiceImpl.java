package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.UserBillDetailListRequestDto;
import com.tuotiansudai.api.service.MobileAppUserBillListService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class MobileAppUserBillListServiceImpl implements MobileAppUserBillListService {
    @Override
    public BaseResponseDto queryUserBillList(UserBillDetailListRequestDto userBillDetailListRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }
}
