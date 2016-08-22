package com.tuotiansudai.console.service;

import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.UserItemDataDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserView;

import java.util.Date;
import java.util.List;

public interface UserServiceConsole {

    BaseDto<BasePaginationDataDto> findAllUser(String loginName, String email, String mobile, Date beginTime, Date endTime,
                                               Source source, RoleStage roleStage, String referrerMobile, String channel,
                                               Integer pageIndex, Integer pageSize);

    List<UserView> searchAllUsers(String loginName, String referrerMobile, String mobile, String identityNumber);

    List<UserItemDataDto> findUsersAccountBalance(String mobile, String balanceMin, String balanceMax, Integer currentPageNo, Integer pageSize);

    long findUsersAccountBalanceCount(String mobile, String balanceMin, String balanceMax);

    long findUsersAccountBalanceSum(String mobile, String balanceMin, String balanceMax);

    List<String> findStaffNameFromUserLike(String loginName);

    List<String> findLoginNameLike(String loginName);

    List<String> findMobileLike(String mobile);

    long findUsersCountByChannel(String channel);

    List<String> findAllLoanerLikeLoginName(String loginName);

    List<String> findAccountLikeLoginName(String loginName);
}
