package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.UserBankCardModel;
import com.tuotiansudai.repository.model.UserBankCardStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBankCardMapper {

    void createInvestor(UserBankCardModel model);

    void createLoaner(UserBankCardModel model);

    UserBankCardModel findByLoginNameAndRole(@Param(value = "loginName") String loginName,
                                             @Param(value = "roleType") Role roleType);

    void updateStatus(@Param(value = "id") long id, @Param(value = "status") UserBankCardStatus status);

    List<UserBankCardModel> findBankCardNumberByloginName(@Param(value = "loginName") String loginName);
}
