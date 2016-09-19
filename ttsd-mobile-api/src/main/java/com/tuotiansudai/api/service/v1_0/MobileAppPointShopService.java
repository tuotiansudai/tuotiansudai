package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ProductDetailRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserAddressRequestDto;

public interface MobileAppPointShopService {

    BaseResponseDto updateUserAddress(UserAddressRequestDto userAddressRequestDto);

    BaseResponseDto findUserAddressResponseDto(BaseParamDto baseParamDto);

    BaseResponseDto findUserPointsOrders(BaseParamDto baseParamDto);

    BaseResponseDto findPointHome(BaseParamDto baseParamDto);

    BaseResponseDto findProductDetail(ProductDetailRequestDto productDetailRequestDto);

    BaseResponseDto productExchange(ProductDetailRequestDto productDetailRequestDto);
}
