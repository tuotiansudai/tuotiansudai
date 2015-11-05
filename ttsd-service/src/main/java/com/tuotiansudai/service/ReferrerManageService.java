package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.Role;

import java.util.Date;
import java.util.List;

public interface ReferrerManageService {

    List<ReferrerManageView> findReferrerManage(String referrerLoginName,String investLoginName,Date investStartTime,Date investEndTime,Integer level,Date rewardStartTime,Date rewardEndTime,Role role,int currentPageNo,int pageSize);

    int findReferrerManageCount(String referrerLoginName,String investLoginName,Date investStartTime,Date investEndTime,Integer level,Date rewardStartTime,Date rewardEndTime,Role role);

}
