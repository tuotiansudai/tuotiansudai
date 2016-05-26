package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.PointTaskModel;
import com.tuotiansudai.point.repository.model.UserPointTaskModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPointTaskMapper {

    void create(UserPointTaskModel userPointTaskModel);

    List<UserPointTaskModel> findByLoginName(String loginName);

    UserPointTaskModel findByLoginNameAndId(@Param(value = "pointTaskId") long pointTaskId,@Param(value = "loginName") String loginName );

    long findMaxTaskLevelByLoginName(@Param(value = "loginName") String loginName, @Param(value = "pointTaskId") long pointTaskId);
}
