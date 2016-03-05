package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AccountModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMapper {

    void create(AccountModel model);

    AccountModel findByLoginName(String loginName);

    AccountModel findByPayUserId(String payUserId);

    List<String> findAllLoanerLikeLoginName(String loginName);

    List<String> findAccountLikeLoginName(String loginName);

    AccountModel lockByLoginName(String loginName);

    void update(AccountModel model);

    AccountModel findByIdentityNumber(String identityNumber);

    List<String> findLoginNames();

    List<String> findBirthOfAccountInMonth();

    List<AccountModel> findUsersAccountPoint(@Param(value = "loginName") String loginName,
                                          @Param(value = "userName") String userName,
                                          @Param(value = "mobile") String mobile,
                                          @Param(value = "startLimit") int startLimit,
                                          @Param(value = "endLimit") int endLimit);

    int findUsersAccountPointCount(@Param(value = "loginName") String loginName,
                                   @Param(value = "userName") String userName,
                                   @Param(value = "mobile") String mobile);

    int findUsersAccountTotalPoint(@Param(value = "loginName") String loginName);

    int findUsersAccountAvailablePoint(@Param(value = "loginName") String loginName);

    void updateByLoginName(@Param(value = "loginName") String loginName, @Param(value = "exchangePoint" ) long exchangePoint);

}
