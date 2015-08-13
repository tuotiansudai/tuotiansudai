package com.ttsd.api.dao;

import com.esoft.archer.user.model.UserBill;

import java.util.List;

public interface MobileAppUserBillListDao {

    Integer getTotalCount(String userId);

    List<UserBill> getUserBillList(Integer index, Integer pageSize,String userId);
}
