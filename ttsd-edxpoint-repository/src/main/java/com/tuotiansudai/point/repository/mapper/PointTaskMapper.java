package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.repository.model.PointTaskModel;
import org.springframework.stereotype.Repository;

@Repository
public interface PointTaskMapper {

    int create(PointTaskModel pointTaskModel);

    PointTaskModel findByName(PointTask pointTask);

    PointTaskModel findById(long id);

}
