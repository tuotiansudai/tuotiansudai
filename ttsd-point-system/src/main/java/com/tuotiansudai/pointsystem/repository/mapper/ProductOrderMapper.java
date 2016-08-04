package com.tuotiansudai.pointsystem.repository.mapper;


import com.tuotiansudai.pointsystem.repository.model.ProductOrderModel;
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
