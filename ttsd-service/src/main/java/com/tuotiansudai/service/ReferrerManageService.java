package com.tuotiansudai.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.*;

import java.util.Date;
import java.util.List;

public interface ReferrerManageService {

    BasePaginationDataDto<ReferrerRelationView> findReferrerRelationList(String referrerLoginName, String loginName, Date startTime, Date endTime, int index, int pageSize);

    BasePaginationDataDto<ReferrerManageView> findReferInvestList(String referrerLoginName, String loginName, Date startTime, Date endTime, int index, int pageSize);

    String getUserRewardDisplayLevel(String loginName);

    String findReferInvestTotalAmount(String referrerLoginName, String loginName, Date startTime, Date endTime);
}
