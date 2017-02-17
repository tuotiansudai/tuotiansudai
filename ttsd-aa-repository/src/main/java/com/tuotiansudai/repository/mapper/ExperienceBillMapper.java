package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ExperienceBillModel;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceBillMapper {

    void create(ExperienceBillModel experienceBillModel);

    ExperienceBillModel findById(long id);

    void update(ExperienceBillModel experienceBillModel);
}
