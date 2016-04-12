package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BankCardModel;

public interface BankCardMapper {

    void create(BankCardModel model);

    BankCardModel findById(long id);

    BankCardModel findPassedBankCardByLoginName(String loginName);

    BankCardModel findByLoginNameAndIsFastPayOn(String loginName);

    void update(BankCardModel bankCardModel);

    BankCardModel findPassedBankCardByBankCode(String bankCode);
}
