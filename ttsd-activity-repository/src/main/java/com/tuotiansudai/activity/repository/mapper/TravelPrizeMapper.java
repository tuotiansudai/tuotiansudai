package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.TravelPrizeModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelPrizeMapper {

    void create(TravelPrizeModel travelPrizeModel);

    TravelPrizeModel findById(long id);

    List<TravelPrizeModel> findAll();

    void update(TravelPrizeModel model);
}
