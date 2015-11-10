package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.BankCardStatus;
import org.apache.ibatis.annotations.Param;

public interface BankCardMapper {

    void create(BankCardModel model);

    void update(@Param("id") long id, @Param("status") BankCardStatus status);

    BankCardModel findById(long id);

    BankCardModel findByLoginName(String loginName);

    void updateBankCard(BankCardModel bankCardModel);

    BankCardModel findByLoginNameAndIsFastPayOn(String loginName);
}
