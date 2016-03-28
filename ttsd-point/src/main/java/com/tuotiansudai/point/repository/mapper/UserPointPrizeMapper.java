package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.UserPointPrizeModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPointPrizeMapper {

    void create(UserPointPrizeModel userPointPrizeModel);


    List<UserPointPrizeModel> findByLoginNameAndCreateTime(@Param("loginName") String loginName, @Param("date") String date);

}
