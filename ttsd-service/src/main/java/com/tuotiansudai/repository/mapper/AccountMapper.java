package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AccountModel;

import java.util.List;

public interface AccountMapper {

    void create(AccountModel model);

    AccountModel findByLoginName(String loginName);

<<<<<<< HEAD
    List<String> findAllLoginNamesByLike(String loginName);
=======
    AccountModel lockByLoginName(String loginName);

    void update(AccountModel model);

>>>>>>> origin/master
}
