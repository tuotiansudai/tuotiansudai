package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.MidSummerSharedUsersModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MidSummerSharedUsersMapper {

    void create(MidSummerSharedUsersModel model);

    MidSummerSharedUsersModel findByLoginName(@Param(value = "loginName") String loginName);
}
