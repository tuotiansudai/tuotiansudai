package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserBankCardModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBankCardMapper {

    void create(UserBankCardModel model);

    UserBankCardModel findByLoginName(@Param(value = "loginName") String loginName);

    void update(@Param(value = "status") UserBankCardModel userBankCardModel);
}
