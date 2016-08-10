package com.tuotiansudai.point.service;


import com.tuotiansudai.point.dto.ProductDto;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;

import java.util.List;

public interface ProductService {

    void createProduct(ProductDto productDto);

    List<ProductModel> findGoods(GoodsType goodsType);

    long findGoodsCount(GoodsType goodsType);

}
