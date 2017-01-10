package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.repository.model.UserPointTaskModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPointTaskMapper {

    void create(UserPointTaskModel userPointTaskModel);

    List<UserPointTaskModel> findByLoginName(String loginName);

    List<UserPointTaskModel> findByLoginNameAndTask(@Param(value = "loginName") String loginName,
                                                    @Param(value = "pointTask") PointTask pointTask);
    long findMaxTaskLevelByLoginName(@Param(value = "loginName") String loginName, @Param(value = "pointTask") PointTask pointTask);

    long findFinishTaskByLoginName(@Param(value = "loginName") String loginName);

}
