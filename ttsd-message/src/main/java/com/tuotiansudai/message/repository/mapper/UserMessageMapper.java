package com.tuotiansudai.message.repository.mapper;

import com.tuotiansudai.message.repository.model.UserMessageModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMessageMapper {

    List<UserMessageModel> findMessagesByLoginName(@Param(value = "loginName") String loginName,
                                                   @Param(value = "index") Integer index,
                                                   @Param(value = "pageSize") Integer pageSize);

    long countMessagesByLoginName(String loginName);

    void create(UserMessageModel model);

    void update(UserMessageModel model);
}
