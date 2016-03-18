package com.tuotiansudai.service;

import com.tuotiansudai.dto.AccountItemDataDto;
import com.tuotiansudai.repository.model.AccountModel;
import java.util.List;

public interface AccountService {

    AccountModel findByLoginName(String loginName);

    long getBalance(String loginName);

    boolean isIdentityNumberExist(String identityNumber);

    long getFreeze(String loginName);

    List<AccountItemDataDto> findUsersAccountPoint(String loginName, String userName, String mobile, int currentPageNo, int pageSize);

    int findUsersAccountPointCount(String loginName, String userName, String mobile);

    int findUsersAccountTotalPoint(String loginName);

    int findUsersAccountAvailablePoint(String loginName);

    void updateByLoginName(String loginName, long exchangePoint);
}
