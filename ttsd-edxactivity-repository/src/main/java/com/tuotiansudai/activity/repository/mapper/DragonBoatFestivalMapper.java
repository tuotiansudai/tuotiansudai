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

    void setPKGroup(DragonBoatFestivalModel dragonBoatFestivalModel);

    void addTotalInvestAmount(@Param(value = "loginName") String loginName,
                              @Param(value = "userName") String userName,
                              @Param(value = "mobile") String mobile,
                              @Param(value = "investAmount") long investAmount);

    void addPKInvestAmount(@Param(value = "loginName") String loginName,
                           @Param(value = "pkInvestAmount") long pkInvestAmount);

    void addInviteExperienceAmount(DragonBoatFestivalModel dragonBoatFestivalModel);

    void setPKExperienceAmount(@Param(value = "loginName") String loginName,
                               @Param(value = "pkExperienceAmount") long pkExperienceAmount);

    void addInviteNewUserCount(@Param(value = "loginName") String loginName,
                               @Param(value = "userName") String userName,
                               @Param(value = "mobile") String mobile);

    void addInviteOldUserCount(@Param(value = "loginName") String loginName,
                               @Param(value = "userName") String userName,
                               @Param(value = "mobile") String mobile);

    DragonBoatFestivalModel findByLoginName(@Param(value = "loginName") String loginName);

    long getGroupInvestAmount(@Param(value = "group") String group);

    long countDragonBoatFestival();

    List<DragonBoatFestivalModel> getDragonBoatFestivalList(@Param(value = "index") int index,
                                                            @Param(value = "pageSize") int pageSize);

    List<DragonBoatFestivalModel> getDragonBoatFestivalPKUserList();

    List<DragonBoatFestivalModel> getDragonBoatFestivalChampagneList();
}
