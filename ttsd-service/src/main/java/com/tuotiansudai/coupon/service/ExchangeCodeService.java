package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.dto.BaseDataDto;

import java.util.List;

public interface ExchangeCodeService {

    boolean generateExchangeCode(long couponId, int count);

    BaseDataDto exchange(String loginName, String exchangeCode);

    List<String> getExchangeCodes(long couponId);

    String toBase31Prefix(long couponId);

    long getValueBase31(String exchangeCode);

    boolean checkExchangeCodeDailyCount(String loginName);

    boolean checkExchangeCodeUsed(long couponId, String exchangeCode);

    boolean checkExchangeCodeExpire(CouponModel couponModel);

    boolean checkExchangeCodeCorrect(String exchangeCode, long couponId, CouponModel couponModel);
}
