package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.UserLuxuryPrizeModel;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLuxuryPrizeMapper {

    void create(UserLuxuryPrizeModel userLuxuryPrizeModel);

    UserLuxuryPrizeModel findById(long id);

}
