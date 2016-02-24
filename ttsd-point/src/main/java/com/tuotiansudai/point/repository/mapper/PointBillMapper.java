package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.PointBillModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointBillMapper {

    void create(PointBillModel pointBillModel);

    List<PointBillModel> findByLoginName(String loginName);
}
