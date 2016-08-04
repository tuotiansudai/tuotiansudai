package com.tuotiansudai.pointsystem.repository.mapper;


import com.tuotiansudai.pointsystem.repository.model.ProductModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper {

    void create(ProductModel productModel);

    void update(ProductModel productModel);

    List<ProductModel> findProductList();

    long findProductCount();

    ProductModel findById(long id);
}
