package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.UserPointTaskModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPointTaskMapper {

    void create(UserPointTaskModel userPointTaskModel);

    List<UserPointTaskModel> findByLoginName(String loginName);
}
