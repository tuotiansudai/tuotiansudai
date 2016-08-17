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

    List<ProductModel> findProductsList(@Param(value = "goodsType") GoodsType goodsType,
                                        @Param(value = "index") int index,
                                        @Param(value = "pageSize") int pageSize);

    long findProductsCount(@Param(value = "goodsType") GoodsType goodsType);

    ProductModel findById(@Param(value = "id") long id);
}
