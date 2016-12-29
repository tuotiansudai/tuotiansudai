package com.tuotiansudai.service;

import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.model.UserModel;

public interface RegisterUserService {

    boolean register(UserModel userModel) throws ReferrerRelationException;
}
