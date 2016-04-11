package com.tuotiansudai.coupon.service;

import com.tuotiansudai.dto.BaseDataDto;

public interface ExchangeCodeService {

    boolean generateExchangeCode(long couponId, int count);

    BaseDataDto exchange(String loginName, String exchangeCode);

}
