package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.repository.model.PointTaskModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointTaskMapper {

    List<PointTaskModel> find(@Param(value = "index") int index,@Param(value = "pageSize") int pageSize);

    int findCount();

    PointTaskModel findByName(PointTask pointTask);

    PointTaskModel findById(long id);
}
