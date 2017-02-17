package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ExperienceGoldBillModel;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceGoldBillMapper {

    void create(ExperienceGoldBillModel experienceGoldBillModel);

    ExperienceGoldBillModel findById(long id);

    void update(ExperienceGoldBillModel experienceGoldBillModel);
}
