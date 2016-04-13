package com.tuotiansudai.coupon.service;

import com.tuotiansudai.dto.BaseDataDto;

public interface ExchangeCodeService {

    boolean generateExchangeCode(long couponId, int count);

    BaseDataDto exchange(String loginName, String exchangeCode);

    String toBase31Prefix(long couponId);

    long getValueBase31(String exchangeCode);
}
