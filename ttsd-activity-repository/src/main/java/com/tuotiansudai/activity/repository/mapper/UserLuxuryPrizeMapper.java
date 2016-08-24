package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.UserLuxuryPrizeModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserLuxuryPrizeMapper {

    void create(UserLuxuryPrizeModel userLuxuryPrizeModel);

    UserLuxuryPrizeModel findById(long id);

    List<UserLuxuryPrizeModel> getUserLuxuryPrizeList(@Param(value = "mobile") String mobile,
                                              @Param(value = "startTime") Date startTime,
                                              @Param(value = "endTime") Date endTime,
                                              @Param(value = "index") Integer index,
                                              @Param(value = "pageSize") Integer pageSize);

    long getCountUserLuxuryPrize(@Param(value = "mobile") String mobile,
                                                      @Param(value = "startTime") Date startTime,
                                                      @Param(value = "endTime") Date endTime);

}
