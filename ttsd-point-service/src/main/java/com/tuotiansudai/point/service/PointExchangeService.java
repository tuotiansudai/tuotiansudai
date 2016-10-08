package com.tuotiansudai.point.service;

import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.ProductOrderViewDto;

import java.util.Date;
import java.util.List;

public interface PointExchangeService {

    List<ExchangeCouponDto> findExchangeableCouponList();

    boolean exchangeCoupon(long couponId, String loginName, long exchangePoint);

    boolean exchangeableCoupon(long couponId, String loginName);

    BasePaginationDataDto<ProductOrderViewDto> findProductOrderListByLoginNamePagination(String loginName, int index, int pageSize);

    long findProductOrderListByLoginNameCount(String loginName);

}
