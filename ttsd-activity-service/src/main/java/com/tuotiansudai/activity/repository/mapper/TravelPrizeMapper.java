package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.TravelPrizeModel;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelPrizeMapper {

    void create(TravelPrizeModel travelPrizeModel);

    TravelPrizeModel findById(long id);

}
