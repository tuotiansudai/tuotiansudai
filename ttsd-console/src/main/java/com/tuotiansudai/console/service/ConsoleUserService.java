package com.tuotiansudai.console.service;

import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.dto.UserItemDataDto;
import com.tuotiansudai.console.repository.model.UserOperation;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.EditUserDto;
import com.tuotiansudai.exception.EditUserException;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserView;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface ConsoleUserService {
    void editUser(String operatorLoginName, EditUserDto editUserDto, String ip) throws EditUserException, ReferrerRelationException;

    EditUserDto getEditUser(String loginName);

    List<String> findAllUserChannels();

    UserModel findByLoginName(String loginName);

    BaseDto<BasePaginationDataDto<UserItemDataDto>> findAllUser(String loginName, String email, String mobile,
                                                                Date beginTime, Date endTime, Source source,
                                                                RoleStage roleStage, String referrerMobile,
                                                                String channel, UserOperation userOperation,
                                                                Integer index, Integer pageSize);

    List<UserView> searchAllUsers(String loginName, String referrerMobile, String mobile, String identityNumber);

    List<UserItemDataDto> findUsersAccountBalance(String mobile, String balanceMin, String balanceMax, Integer index, Integer pageSize);

    long findUsersAccountBalanceCount(String mobile, String balanceMin, String balanceMax);

    long findUsersAccountBalanceSum(String mobile, String balanceMin, String balanceMax);

    List<String> findStaffNameFromUserLike(String loginName);

    List<String> findLoginNameLike(String loginName);

    List<String> findMobileLike(String mobile);

    long findUsersCountByChannel(String channel);

    List<String> findAllLoanerLikeLoginName(String loginName);

    List<String> findAccountLikeLoginName(String loginName);
}
