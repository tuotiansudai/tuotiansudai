package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ExperienceBillModel;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ExperienceBillMapper {

    void create(ExperienceBillModel experienceBillModel);

    ExperienceBillModel findById(long id);

    void update(ExperienceBillModel experienceBillModel);

    Date findLastExchangeTimeByLoginName(String loginName);
}
