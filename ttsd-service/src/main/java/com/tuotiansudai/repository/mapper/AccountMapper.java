package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AccountModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMapper {

    void create(AccountModel model);

    AccountModel findByLoginName(String loginName);

    AccountModel findByLoginNameOrderByTime(String loginName);

    AccountModel findByPayUserId(String payUserId);

    List<String> findAllLoginNamesByLike(String loginName);

    AccountModel lockByLoginName(String loginName);

    void update(AccountModel model);

    AccountModel findByIdentityNumber(String identityNumber);

}
