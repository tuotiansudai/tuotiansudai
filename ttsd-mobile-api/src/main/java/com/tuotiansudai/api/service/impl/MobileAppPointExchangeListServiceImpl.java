package com.tuotiansudai.api.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppPointExchangeListService;
import com.tuotiansudai.coupon.repository.mapper.CouponExchangeMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppPointExchangeListServiceImpl implements MobileAppPointExchangeListService {

    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponExchangeMapper couponExchangeMapper;

    @Override
    public BaseResponseDto generatePointExchangeList(PointExchangeListRequestDto pointExchangeListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        Integer index = pointExchangeListRequestDto.getIndex();
        Integer pageSize = pointExchangeListRequestDto.getPageSize();

        int count = couponMapper.findCouponExchangeableCount();

        if (index == null || index.intValue() <= 0) {
            index = 1;
        }

        if (pageSize == null || pageSize.intValue() <= 0) {
            pageSize = 10;
        }
        List<CouponModel> couponModels = couponMapper.findCouponExchangeableList((index-1)*pageSize, pageSize);

        List<PointExchangeRecordResponseDataDto> pointExchangeRecordResponseDataDto = null;
        if (CollectionUtils.isNotEmpty(couponModels)) {
            pointExchangeRecordResponseDataDto = Lists.transform(couponModels, new Function<CouponModel, PointExchangeRecordResponseDataDto>() {
                @Override
                public PointExchangeRecordResponseDataDto apply(CouponModel input) {
                    return new PointExchangeRecordResponseDataDto(input, couponExchangeMapper.findByCouponId(input.getId()).getExchangePoint());
                }
            });
        }

        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        PointExchangeListResponseDataDto pointExchangeListResponseDataDto = new PointExchangeListResponseDataDto();
        pointExchangeListResponseDataDto.setPointExchange(pointExchangeRecordResponseDataDto);
        pointExchangeListResponseDataDto.setIndex(index);
        pointExchangeListResponseDataDto.setPageSize(pageSize);
        pointExchangeListResponseDataDto.setTotalCount(count);
        dto.setData(pointExchangeListResponseDataDto);
        return dto;
    }


}
