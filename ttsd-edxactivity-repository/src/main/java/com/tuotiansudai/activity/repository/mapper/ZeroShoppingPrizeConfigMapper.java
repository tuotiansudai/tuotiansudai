package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.ZeroShoppingPrize;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrizeConfigModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZeroShoppingPrizeConfigMapper {

    List<ZeroShoppingPrizeConfigModel> findAll();

    void update(ZeroShoppingPrizeConfigModel zeroShoppingPrizeModel);
}
