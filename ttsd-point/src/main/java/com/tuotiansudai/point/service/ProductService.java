package com.tuotiansudai.point.service;


import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.point.dto.ProductDto;
import com.tuotiansudai.point.dto.ProductOrderDto;
import com.tuotiansudai.point.dto.ProductShowItemDto;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ItemType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.point.repository.model.UserAddressModel;

import java.util.List;

public interface ProductService {

    void createProduct(ProductDto productDto);

    List<ProductModel> findProductsList(GoodsType goodsType, int index, int pageSize);

    long findProductsCount(GoodsType goodsType);

    List<ProductShowItemDto> findAllProductsByGoodsTypes(List<GoodsType> goodsTypes);

    ProductShowItemDto findProductShowItemDto(long id, ItemType itemType);

    BaseDto<BaseDataDto> buyProduct(String loginName, long id, ItemType itemType, int amount, Long addressId);

    List<UserAddressModel> getUserAddressesByLoginName(String loginName);

    BaseDto<BaseDataDto> addAddress(String loginName, String realName, String mobile, String address);

    BaseDto<BaseDataDto> updateAddress(long id, String loginName, String realName, String mobile, String address);

    List<ProductOrderDto> findProductOrderList(long goodsId, String loginName, int index, int pageSize);

    long findProductOrderCount(long goodsId);

    BaseDataDto active(long productId, String loginName);

    BaseDataDto consignment(long orderId);

    ProductModel findById(long id);

    BaseDataDto updateProduct(ProductDto productDto);
}
