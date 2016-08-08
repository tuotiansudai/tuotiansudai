package com.tuotiansudai.point.service;


import com.tuotiansudai.point.dto.GoodsRequestDto;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;

import java.util.List;

public interface GoodsManageService {
    void createGoods(GoodsRequestDto requestDto);

    List<ProductModel> findGoods(GoodsType goodsType);

    long findGoodsCount(GoodsType goodsType);
}
