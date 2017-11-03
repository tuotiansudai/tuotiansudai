package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserModel;
import org.springframework.stereotype.Repository;

@Repository
public interface FakeUserHelper {

    int create(UserModel userModel);

    int updateUser(UserModel userModel);
}
