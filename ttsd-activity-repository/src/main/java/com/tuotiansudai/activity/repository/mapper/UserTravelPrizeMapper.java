package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.UserTravelPrizeModel;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTravelPrizeMapper {

    void create(UserTravelPrizeModel userTravelPrizeModel);

    UserTravelPrizeModel findById(long id);

}
