package com.tuotiansudai.point.repository.mapper;


import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper {

    void create(ProductModel productModel);

    void update(ProductModel productModel);

    List<ProductModel> findExchangeableProductsList(@Param(value = "goodsType") GoodsType goodsType,
                                        @Param(value = "index") int index,
                                        @Param(value = "pageSize") int pageSize);

    long findExchangeableProductsCount(@Param(value = "goodsType") GoodsType goodsType);

    List<ProductModel> findAllProducts(@Param(value = "goodsType") GoodsType goodsType,
                                        @Param(value = "index") int index,
                                        @Param(value = "pageSize") int pageSize);

    long findAllProductsCount(@Param(value = "goodsType") GoodsType goodsType);

    ProductModel findById(@Param(value = "id") long id);
}
