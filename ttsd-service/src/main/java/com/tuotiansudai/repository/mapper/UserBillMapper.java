package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserBillModel;

public interface UserBillMapper {

    void create(UserBillModel userBillModel);

    UserBillModel findByLoginName(String loginName);
}
