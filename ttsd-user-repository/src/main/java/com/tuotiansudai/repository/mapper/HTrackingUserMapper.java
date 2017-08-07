package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.HTrackingUserModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HTrackingUserMapper {

    void create(HTrackingUserModel hTrackingUserModel);

    void update(HTrackingUserModel hTrackingUserModel);

    HTrackingUserModel findbyId(long id);

    HTrackingUserModel findByMobileAndDeviceId(@Param(value = "mobile") String mobile,
                                               @Param(value = "deviceId") String deviceId);
}
