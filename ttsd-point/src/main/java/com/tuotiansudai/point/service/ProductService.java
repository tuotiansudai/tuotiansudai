package com.tuotiansudai.point.service;


import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.point.dto.ProductDto;
import com.tuotiansudai.point.dto.ProductShowItemDto;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ItemType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.point.repository.model.UserAddressModel;

import java.util.List;

public interface ProductService {

    void createProduct(ProductDto productDto);

    List<ProductModel> findGoods(GoodsType goodsType);

    long findGoodsCount(GoodsType goodsType);

    List<ProductShowItemDto> findAllProductsByGoodsTypes(List<GoodsType> goodsTypes);

    ProductShowItemDto findProductShowItemDto(long id, ItemType itemType);

    BaseDto<BaseDataDto> buyProduct(String loginName, long id, ItemType itemType, int amount, UserAddressModel userAddressModel);

    List<UserAddressModel> getUserAddressesByLoginName(String loginName);
}
