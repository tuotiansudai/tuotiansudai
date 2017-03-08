package com.tuotiansudai.point.repository.mapper;


import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductMapper {

    void create(ProductModel productModel);

    void update(ProductModel productModel);

    List<ProductModel> findExchangeableProductsList(@Param(value="currentDate") Date currentDate,
                                                    @Param(value = "type") GoodsType type,
                                                    @Param(value = "index") int index,
                                                    @Param(value = "pageSize") int pageSize);

    long findExchangeableProductsCount(@Param(value = "type") GoodsType type);

    List<ProductModel> findAllProducts(@Param(value = "type") GoodsType type,
                                        @Param(value = "index") int index,
                                        @Param(value = "pageSize") int pageSize);

    long findAllProductsCount(@Param(value = "type") GoodsType type);

    ProductModel findById(@Param(value = "id") long id);

    ProductModel findByCouponId(@Param(value = "couponId") long couponId);

    List<ProductModel> findAllProductsByGoodsType(@Param(value = "goodsTypes") List<GoodsType> goodsTypes);

    ProductModel lockById(@Param(value = "id") long id);

}
