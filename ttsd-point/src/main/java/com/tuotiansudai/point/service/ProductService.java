package com.tuotiansudai.point.service;


import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.point.dto.GoodsActiveDto;
import com.tuotiansudai.point.dto.GoodsConsignmentDto;
import com.tuotiansudai.point.dto.ProductDto;
import com.tuotiansudai.point.dto.ProductOrderDto;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;

import java.util.List;

public interface ProductService {

    void createProduct(ProductDto productDto);

    List<ProductModel> findProductsList(GoodsType goodsType, int index, int pageSize);

    long findProductsCount(GoodsType goodsType);

    List<ProductOrderDto> findProductOrderList(long productId, String loginName, int index, int pageSize);

    long findProductOrderCount(long productId);

    BaseDataDto active(long productId, String loginName);

    BaseDataDto consignment(long orderId);

    ProductModel findById(long id);

    BaseDataDto updateProduct(ProductDto productDto);
}
