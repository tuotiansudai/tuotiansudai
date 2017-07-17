package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppCurrentInvestService;
import com.tuotiansudai.api.service.v1_0.MobileAppCurrentWithdrawService;

public class MobileAppCurrentWithdrawServiceImpl implements MobileAppCurrentWithdrawService {

    @Override
    public BaseResponseDto<CurrentWithdrawResponseDataDto> withdraw(CurrentWithdrawRequestDto withdrawRequestDto, String loginName) {
        //调用封装好的rest api，金额转换成分
        return null;
    }
}
