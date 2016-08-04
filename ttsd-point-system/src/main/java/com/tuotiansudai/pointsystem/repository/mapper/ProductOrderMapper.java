package com.tuotiansudai.pointsystem.repository.mapper;


import com.tuotiansudai.pointsystem.repository.model.ProductOrderModel;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderMapper {

    void create(ProductOrderModel productOrderModel);


}
