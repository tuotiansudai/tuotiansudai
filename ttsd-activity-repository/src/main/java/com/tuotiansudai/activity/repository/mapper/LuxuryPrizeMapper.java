package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LuxuryPrizeMapper {

    void create(LuxuryPrizeModel luxuryPrizeModel);

    LuxuryPrizeModel findById(long id);

    LuxuryPrizeModel findByPrizeId(long id);

    List<LuxuryPrizeModel> findAll();


}
