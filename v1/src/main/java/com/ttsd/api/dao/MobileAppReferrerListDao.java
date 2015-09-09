package com.ttsd.api.dao;

import com.ttsd.api.dto.ReferrerResponseDataDto;

import java.util.List;

public interface MobileAppReferrerListDao {

    Integer getTotalCount(String ReferrerId);

    List<ReferrerResponseDataDto> getReferrerRelationList(Integer index, Integer pageSize, String referrerId);

    String getUserRoleByUserId(String userId);

}
