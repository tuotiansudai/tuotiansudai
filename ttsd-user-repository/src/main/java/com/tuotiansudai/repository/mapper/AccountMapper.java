package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AccountModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMapper {

    AccountModel findByLoginName(String loginName);

    List<AccountModel> findByLoginNames(List<String> loginNames);

    AccountModel lockByLoginName(String loginName);

    void updateBalanceAndFreeze(@Param(value = "loginName") String loginName,
                                @Param(value = "balance") long balance,
                                @Param(value = "freeze") long freeze);
}
