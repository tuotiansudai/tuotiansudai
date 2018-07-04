package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserBankCardModel;
import com.tuotiansudai.repository.model.UserBankCardStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBankCardMapper {

    void createInvestor(UserBankCardModel model);

    void createLoaner(UserBankCardModel model);

    UserBankCardModel findByLoginName(@Param(value = "loginName") String loginName);

    void updateStatus(@Param(value = "id") long id, @Param(value = "status") UserBankCardStatus status);
}
