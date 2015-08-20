package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AccountModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountMapper {

    void create(AccountModel model);

    AccountModel findByLoginName(String loginName);

    List<String> findAllLoginNamesByLike(@Param("loginName")String loginName);

    AccountModel lockByLoginName(String loginName);

    void update(AccountModel model);

}
