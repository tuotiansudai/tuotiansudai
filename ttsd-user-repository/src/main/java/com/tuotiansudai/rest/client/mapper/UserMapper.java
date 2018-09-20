package com.tuotiansudai.rest.client.mapper;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRegisterInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface UserMapper {
    UserModel findByLoginNameOrMobile(String loginNameOrMobile);

    UserModel findByIdentityNumber(String identityNumber);

    UserModel findByEmail(String email);

    void updateEmail(String loginName, String email);

    void updateUserNameAndIdentityNumber(String loginName, String userName, String identityNumber);

    void updateStaffReferrerMobile(String loginName, String staffReferrerMobile);

    BasePaginationDataDto<UserRegisterInfo> findUsersByRegisterTimeAndReferrer(Date startTime, Date endTime, String referrer, int page, int pageSize);

    BasePaginationDataDto<UserRegisterInfo> findUsersHasReferrerByRegisterTime(Date startTime, Date endTime, int page, int pageSize);

    long findUserCountByRegisterTimeAndReferrer(Date startTime, Date endTime, String referrer);

    long findUsersCount();

    List<UserModel> findUserModelByMobileLike(String mobile, int page, int pageSize);

    int findCountByMobileLike(String mobile);

    List<UserModel> findEmptyProvinceUsers();

    void updateProvinceAndCity(String loginName, String province, String city);

    // call UserMapperDB
    UserModel lockByLoginName(String loginName);

    default UserModel findByMobile(String mobile) {
        return findByLoginNameOrMobile(mobile);
    }

    default UserModel findByLoginName(String loginName) {
        return findByLoginNameOrMobile(loginName);
    }

    /**
     * @deprecated
     *      仅为兼容性考虑，新业务不允许使用该方法, replaced by
     *      {@link #findUsersByRegisterTimeAndReferrer(Date, Date, String, int, int)}
     */
    @Deprecated
    default List<UserRegisterInfo> findAllUsersByRegisterTimeAndReferrer(Date startTime, Date endTime, String referrer) {
        int page = 1;
        int pageSize = 100;
        BasePaginationDataDto<UserRegisterInfo> registerUsers = findUsersByRegisterTimeAndReferrer(startTime, endTime, referrer, page, pageSize);
        List<UserRegisterInfo> list = new ArrayList<>(registerUsers.getRecords());
        while (registerUsers.isHasNextPage()) {
            page++;
            registerUsers = findUsersByRegisterTimeAndReferrer(startTime, endTime, referrer, page, pageSize);
            list.addAll(registerUsers.getRecords());
        }
        return list;
    }


    /**
     * @deprecated
     *      仅为兼容性考虑，新业务不允许使用该方法, replaced by
     *      {@link #findUsersHasReferrerByRegisterTime(Date, Date, int, int)}
     */
    @Deprecated
    default List<UserRegisterInfo> findAllUserHasReferrerByRegisterTime(Date startTime, Date endTime) {
        int page = 1;
        int pageSize = 100;
        BasePaginationDataDto<UserRegisterInfo> registerUsers = findUsersHasReferrerByRegisterTime(startTime, endTime, page, pageSize);
        List<UserRegisterInfo> list = new ArrayList<>(registerUsers.getRecords());
        while (registerUsers.isHasNextPage()) {
            page++;
            registerUsers = findUsersHasReferrerByRegisterTime(startTime, endTime, page, pageSize);
            list.addAll(registerUsers.getRecords());
        }
        return list;
    }
}

