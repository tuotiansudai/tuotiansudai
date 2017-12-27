package com.tuotiansudai.point.service;

import com.tuotiansudai.point.repository.model.UserPointModel;

public interface UserPointService {
    UserPointModel findOrCreate(String loginName);
}
