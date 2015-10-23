package com.tuotiansudai.service;


import com.tuotiansudai.repository.model.InvestRepayInAccountDto;
import com.tuotiansudai.repository.model.InvestRepayModel;

import java.util.Date;
import java.util.List;

public interface InvestRepayService {

    long findByLoginNameAndTimeAndSuccessInvestRepay(String loginName,Date startTime,Date endTime);

    long findByLoginNameAndTimeAndNotSuccessInvestRepay(String loginName,Date startTime,Date endTime);

    List<InvestRepayModel> findByLoginNameAndTimeSuccessInvestRepayList(String loginName, Date startTime, Date endTime, int startLimit, int endLimit);

    List<InvestRepayModel> findByLoginNameAndTimeNotSuccessInvestRepayList(String loginName, Date startTime, Date endTime, int startLimit, int endLimit);

    List<InvestRepayInAccountDto> findRecentlyInvestByLoginNameInAccount(String loginName);

    long findSumSuccessInterestByLoginName(String loginName);

    long findSumWillSuccessInterestByLoginName(String loginName);

    long findSumWillSuccessCorpusByLoginName(String loginName);
}
