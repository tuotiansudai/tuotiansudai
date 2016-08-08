package com.tuotiansudai.point.repository.mapper;


import com.tuotiansudai.point.repository.model.ProductOrderModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderMapper {

    void create(ProductOrderModel productOrderModel);

    void update(ProductOrderModel productModel);

    List<ProductOrderModel> findProductOrderList();

    long findProductOrderCount();

    ProductOrderModel findById(long id);

}
