package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.dto.LuxuryPrizeDto;
import com.tuotiansudai.activity.dto.UserLuxuryPrizeDto;

import java.util.Date;
import java.util.List;

public interface SeptemberActivityService {

    List<UserLuxuryPrizeDto> obtainLuxuryPrizeList(String mobile,Date startTime,Date endTime,Integer index,Integer pageSize);
}
