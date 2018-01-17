package com.tuotiansudai.point.service;


import com.tuotiansudai.dto.ExchangeCouponDto;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.point.repository.dto.ProductDto;
import com.tuotiansudai.point.repository.dto.ProductOrderDto;
import com.tuotiansudai.point.repository.dto.ProductShowItemDto;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.point.repository.model.UserAddressModel;
import com.tuotiansudai.repository.model.ProductType;

import java.util.List;

public interface ProductService {

    void createProduct(ProductDto productDto);

    List<ProductModel> findAllProducts(GoodsType goodsType, int index, int pageSize);

    long findAllProductsCount(GoodsType goodsType);

    List<ProductShowItemDto> findAllProductsByGoodsTypes(List<GoodsType> goodsTypes);

    ProductShowItemDto findProductShowItemDto(long id, GoodsType goodsType);

    BaseDto<BaseDataDto> buyProduct(String loginName, long id, GoodsType goodsType, int amount, Long addressId, String comment);

    List<UserAddressModel> getUserAddressesByLoginName(String loginName);

    BaseDto<BaseDataDto> addAddress(String loginName, String realName, String mobile, String address);

    BaseDto<BaseDataDto> updateAddress(long id, String loginName, String realName, String mobile, String address);

    List<ProductOrderDto> findProductOrderList(long goodsId, String loginName, int index, int pageSize);

    long findProductOrderCount(long goodsId);

    BaseDataDto active(long productId, String loginName);

    BaseDataDto inactive(long productId, String loginName);

    BaseDataDto consignment(long orderId);

    BaseDataDto batchConsignment(long goodsId);

    ProductModel findById(long id);

    BaseDataDto updateProduct(ProductDto productDto);

    ProductModel findProductByCouponId(long couponId);

    List<ExchangeCouponDto> findCouponExchanges(int index, int pageSize);

    int findCouponExchangeCount();

    List<String> getProductDescription(long investLowerLimit,List<ProductType> productTypes,Integer deadline);

    String discountShowInfo(String loginName);

    double discountRate(String loginName);

    long discountTotalPrice(long originalPoints, double discountRate, int count);

    int getUserBuyCountInMonth(long productId, String loginName);
}
