package com.tuotiansudai.console.repository.mapper;

import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository(value = "consoleUserMapper")
public interface UserMapperConsole {

    List<UserView> findAllUser(@Param(value = "loginName") String loginName,
                                @Param(value = "email") String email,
                                @Param(value = "mobile") String mobile,
                                @Param(value = "beginTime") Date beginTime,
                                @Param(value = "endTime") Date endTime,
                                @Param(value = "source") Source source,
                                @Param(value = "roleStage") RoleStage roleStage,
                                @Param(value = "referrerMobile") String referrerMobile,
                                @Param(value = "channel") String channel,
                                @Param(value = "index") Integer index,
                                @Param(value = "pageSize") Integer pageSize);

    int findAllUserCount(@Param(value = "loginName") String loginName,
                         @Param(value = "email") String email,
                         @Param(value = "mobile") String mobile,
                         @Param(value = "beginTime") Date beginTime,
                         @Param(value = "endTime") Date endTime,
                         @Param(value = "source") Source source,
                         @Param(value = "roleStage") RoleStage roleStage,
                         @Param(value = "referrerMobile") String referrerMobile,
                         @Param(value = "channel") String channel);
}
