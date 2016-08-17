package com.tuotiansudai.point.repository.mapper;


import com.tuotiansudai.point.repository.model.ProductOrderModel;
import com.tuotiansudai.point.repository.model.ProductOrderViewDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderMapper {

    void create(ProductOrderModel productOrderModel);

    void update(ProductOrderModel productModel);

    List<ProductOrderModel> findProductOrderList(
            @Param(value = "goodsId") long goodsId,
            @Param(value = "createdBy") String createdBy,
            @Param(value = "index") int index,
            @Param(value = "pageSize") int pageSize);

    long findProductOrderCount(
            @Param(value = "goodsId") long goodsId,
            @Param(value = "createdBy") String createdBy);

    ProductOrderModel findById(long id);

    List<ProductOrderViewDto> findProductOrderListByLoginName(String loginName);

}
