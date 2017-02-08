package com.tuotiansudai.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.ReferrerRelationView;

import java.util.Date;

public interface ReferrerManageService {

    BasePaginationDataDto<ReferrerRelationView> findReferrerRelationList(String referrerLoginName, String loginName, Date startTime, Date endTime, int index, int pageSize);

    BasePaginationDataDto<ReferrerManageView> findReferInvestList(String referrerLoginName, String loginName, Date startTime, Date endTime, int index, int pageSize);

    String getUserRewardDisplayLevel(String loginName);

    String findReferInvestTotalAmount(String referrerLoginName, String loginName, Date startTime, Date endTime);
}
