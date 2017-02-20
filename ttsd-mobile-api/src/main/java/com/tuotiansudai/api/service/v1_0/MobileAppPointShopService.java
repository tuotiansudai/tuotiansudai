package com.tuotiansudai.api.service.v1_0;


import com.tuotiansudai.api.dto.v1_0.*;

public interface MobileAppPointShopService {

    BaseResponseDto updateUserAddress(UserAddressRequestDto userAddressRequestDto);

    BaseResponseDto<UserAddressResponseDto> findUserAddressResponseDto(BaseParamDto baseParamDto);

    BaseResponseDto<ProductListOrderResponseDto> findUserPointsOrders(BaseParamDto baseParamDto);

    BaseResponseDto<ProductListResponseDto> findPointHome(ProductListRequestDto productListRequestDto);

    BaseResponseDto<ProductDetailResponseDto> findProductDetail(ProductDetailRequestDto productDetailRequestDto);

    BaseResponseDto productExchange(ProductDetailRequestDto productDetailRequestDto);

    BaseResponseDto<PointDrawResultResponseDto> lotteryDrawByPoint(BaseParamDto baseParamDto);

    BaseResponseDto<MyPrizeListResponseDto> findPrizeListByLoginName(BaseParamDto baseParamDto);

    BaseResponseDto<PrizeListResponseDto> findPrizeList(BaseParamDto baseParamDto);

}
