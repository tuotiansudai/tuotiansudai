package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.UserTravelPrizeModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserTravelPrizeMapper {

    void create(UserTravelPrizeModel userTravelPrizeModel);

    UserTravelPrizeModel findById(long id);

    List<UserTravelPrizeModel> findByPagination(@Param("mobile") String mobile,
                                                @Param("startTime") Date startTime,
                                                @Param("endTime") Date endTime,
                                                @Param("index") Integer index,
                                                @Param("pageSize") Integer pageSize);

    long countByPagination(@Param("mobile") String mobile,
                           @Param("startTime") Date startTime,
                           @Param("endTime") Date endTime);

}
