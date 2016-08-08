package com.tuotiansudai.pointsystem.service;


import com.tuotiansudai.pointsystem.dto.GoodsRequestDto;
import com.tuotiansudai.pointsystem.repository.model.GoodsType;
import com.tuotiansudai.pointsystem.repository.model.ProductModel;

import java.util.List;

public interface GoodsManageService {
    void createGoods(GoodsRequestDto requestDto);

    List<ProductModel> findGoods(GoodsType goodsType);

    long findGoodsCount(GoodsType goodsType);
}
