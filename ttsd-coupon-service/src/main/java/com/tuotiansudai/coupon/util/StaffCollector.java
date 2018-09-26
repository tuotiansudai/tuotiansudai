package com.tuotiansudai.coupon.util;

import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffCollector implements UserCollector {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        if (userModel == null) {
            return false;
        }
        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(userModel.getLoginName());
        return userRoleModels.stream()
                .anyMatch(ur -> (ur.getRole() == Role.ZC_STAFF || ur.getRole() == Role.SD_STAFF));
    }

}
