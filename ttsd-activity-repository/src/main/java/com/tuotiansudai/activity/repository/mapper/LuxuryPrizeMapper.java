package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.model.LuxuryPrizeModel;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LuxuryPrizeMapper {

    void create(LuxuryPrizeModel luxuryPrizeModel);

    LuxuryPrizeModel findById(long id);

    LuxuryPrizeModel findByPrizeId(long id);

    List<LuxuryPrizeModel> findAll();

    void update(LuxuryPrizeModel luxuryPrizeModel);


}
