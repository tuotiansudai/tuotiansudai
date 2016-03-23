package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.UserPointPrizeModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointPrizeMapper {

    void create(UserPointPrizeModel userPointPrizeModel);


    UserPointPrizeModel findByLoginNameAndCreateTime(@Param("loginName") String loginName, @Param("date") String date);

}
