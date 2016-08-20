package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import org.springframework.stereotype.Repository;

@Repository
public interface LuxuryPrizeMapper {

    void create(LuxuryPrizeModel luxuryPrizeModel);

    LuxuryPrizeModel findById(long id);

}
