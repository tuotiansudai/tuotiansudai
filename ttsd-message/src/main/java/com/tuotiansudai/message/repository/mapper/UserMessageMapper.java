package com.tuotiansudai.message.repository.mapper;

import com.tuotiansudai.message.repository.model.UserMessageModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMessageMapper {

    List<UserMessageModel> findByLoginName(String loginName);

    void create(UserMessageModel model);

    void update(UserMessageModel model);
}
