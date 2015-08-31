package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AccountModel;

public interface AccountMapper {

    void create(AccountModel model);

    AccountModel findByLoginName(String loginName);

    AccountModel lockByLoginName(String loginName);

    void update(AccountModel model);

}
