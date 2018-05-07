package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.BankAccountModel;

public interface BankAccountMapper {

    void create(BankAccountModel bankAccountModel);

    BankAccountModel findByLoginName(String loginName);
}
