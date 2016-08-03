package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.PrepareChannel;
import com.tuotiansudai.repository.model.PrepareModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PrepareMapper {

    void create(PrepareModel prepareModel);

    List<PrepareModel> findPrepares(
            @Param(value = "referrerMobile") String referrerMobile,
            @Param(value = "mobile") String mobile,
            @Param(value = "channel") PrepareChannel channel,
            @Param(value = "beginTime") Date beginTime,
            @Param(value = "endTime") Date endTime,
            @Param(value = "index") int index,
            @Param(value = "pageSize") int pageSize
    );

    long findPrepareCount(
            @Param(value = "referrerMobile") String referrerMobile,
            @Param(value = "mobile") String mobile,
            @Param(value = "channel") PrepareChannel channel,
            @Param(value = "beginTime") Date beginTime,
            @Param(value = "endTime") Date endTime
    );
}
