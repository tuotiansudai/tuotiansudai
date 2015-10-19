package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserInfoLogModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoLogMapper {

    void create(UserInfoLogModel userInfoLogModel);

}
