package com.tuotiansudai.service;


import com.tuotiansudai.dto.InvestRepayDataItemDto;
import com.tuotiansudai.dto.InvestorInvestPaginationItemDataDto;
import com.tuotiansudai.repository.model.LatestInvestView;

import java.util.Date;
import java.util.List;

public interface InvestRepayService {

    long findByLoginNameAndTimeAndSuccessInvestRepay(String loginName,Date startTime,Date endTime);

    long findByLoginNameAndTimeAndNotSuccessInvestRepay(String loginName,Date startTime,Date endTime);

    List<InvestRepayDataItemDto> findByLoginNameAndTimeSuccessInvestRepayList(String loginName, Date startTime, Date endTime, int startLimit, int endLimit);

    List<InvestRepayDataItemDto> findByLoginNameAndTimeNotSuccessInvestRepayList(String loginName, Date startTime, Date endTime, int startLimit, int endLimit);

    List<InvestorInvestPaginationItemDataDto> findLatestInvestByLoginName(String loginName, int startLimit, int endLimit);
}
