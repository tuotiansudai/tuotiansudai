package com.tuotiansudai.point.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.repository.model.ProductOrderViewDto;

public interface PointExchangeService {

    boolean exchangeCoupon(long couponId, String loginName, long exchangePoint);

    boolean exchangeableCoupon(long couponId, String loginName);

    BasePaginationDataDto<ProductOrderViewDto> findProductOrderListByLoginNamePagination(String loginName, int index, int pageSize);

}
