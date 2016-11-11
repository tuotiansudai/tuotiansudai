package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.PointPrizeModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointPrizeMapper {

    List<PointPrizeModel> findAllPossibleWin();

    List<PointPrizeModel> findAllUnPossibleWin();

    PointPrizeModel findById(long pointPrizeId);

}
