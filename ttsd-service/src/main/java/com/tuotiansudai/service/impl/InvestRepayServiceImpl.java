package com.tuotiansudai.service.impl;


import com.tuotiansudai.dto.InvestRepayInAccountDto;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.service.InvestRepayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InvestRepayServiceImpl implements InvestRepayService{

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Override
    public long findByLoginNameAndTimeAndSuccessInvestRepay(String loginName,Date startTime,Date endTime) {
        return investRepayMapper.findByLoginNameAndTimeAndSuccessInvestRepay(loginName, startTime, endTime);
    }

    @Override
    public long findByLoginNameAndTimeAndNotSuccessInvestRepay(String loginName,Date startTime,Date endTime) {
        return investRepayMapper.findByLoginNameAndTimeAndNotSuccessInvestRepay(loginName, startTime, endTime);
    }

    @Override
    public List<InvestRepayModel> findByLoginNameAndTimeSuccessInvestRepayList(String loginName, Date startTime, Date endTime, int startLimit, int endLimit) {
        return investRepayMapper.findByLoginNameAndTimeSuccessInvestRepayList(loginName, startTime, endTime, startLimit, endLimit);
    }

    @Override
    public List<InvestRepayModel> findByLoginNameAndTimeNotSuccessInvestRepayList(String loginName, Date startTime, Date endTime, int startLimit, int endLimit) {
        return investRepayMapper.findByLoginNameAndTimeNotSuccessInvestRepayList(loginName, startTime, endTime, startLimit, endLimit);
    }

    @Override
    public List<InvestRepayInAccountDto> findRecentlyInvestByLoginNameInAccount(String loginName) {
        return investRepayMapper.findRecentlyInvestByLoginNameInAccount(loginName);
    }

    @Override
    public long findSumSuccessInterestByLoginName(String loginName) {
        return investRepayMapper.findSumSuccessInterestByLoginName(loginName);
    }

    @Override
    public long findSumWillSuccessInterestByLoginName(String loginName) {
        return investRepayMapper.findSumWillSuccessInterestByLoginName(loginName);
    }

    @Override
    public long findSumWillSuccessCorpusByLoginName(String loginName) {
        return investRepayMapper.findSumWillSuccessCorpusByLoginName(loginName);
    }
}
