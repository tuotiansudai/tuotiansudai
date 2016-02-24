package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.repository.model.PointTaskModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointTaskMapper {

    List<PointTaskModel> find();

    PointTaskModel findByName(PointTask pointTask);
}
