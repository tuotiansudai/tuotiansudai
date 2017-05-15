package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.DragonBoatFestivalModel;
import com.tuotiansudai.activity.repository.model.InvestNewmanTyrantModel;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DragonBoatFestivalMapper {

    void create(DragonBoatFestivalModel dragonBoatFestivalModel);

    void addInvestAmount(DragonBoatFestivalModel dragonBoatFestivalModel);

    void addExperienceAmount(DragonBoatFestivalModel dragonBoatFestivalModel);

    void addInviteNewUserCount(@Param(value = "loginName") String loginName,
                               @Param(value = "userName") String userName,
                               @Param(value = "mobile") String mobile);

    void addInviteOldUserCount(@Param(value = "loginName") String loginName,
                               @Param(value = "userName") String userName,
                               @Param(value = "mobile") String mobile);

    List<DragonBoatFestivalModel> getDragonBoatFestivalList(@Param(value = "index") int index,
                                                            @Param(value = "pageSize") int pageSize);
}
