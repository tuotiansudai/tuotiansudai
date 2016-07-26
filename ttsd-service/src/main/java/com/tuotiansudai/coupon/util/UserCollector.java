package com.tuotiansudai.coupon.util;

import com.tuotiansudai.coupon.repository.model.UserGroup;

import java.util.List;

public interface UserCollector {

    List<String> collect(long couponId);

    boolean contains(long couponId, String loginName);

    boolean contains(long loanId, String loginName, UserGroup userGroup);
}
