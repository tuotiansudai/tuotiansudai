package com.tuotiansudai.point.repository.mapper;


import com.tuotiansudai.point.repository.model.ProductOrderModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderMapper {

    void create(ProductOrderModel productOrderModel);

    void update(ProductOrderModel productModel);

    List<ProductOrderModel> findProductOrderList(
            @Param("productId") long productId,
            @Param("createdBy") String createdBy
    );

    long findProductOrderCount(
            @Param("productId") long productId,
            @Param("createdBy") String createdBy
    );

    ProductOrderModel findById(long id);

}
