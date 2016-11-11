package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.PromotionModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionMapper {

    void create(PromotionModel promotionModel);

    PromotionModel findById(long id);

    List<PromotionModel> findAll();

    void update(PromotionModel promotionModel);

    List<PromotionModel> findPromotionList();

}
