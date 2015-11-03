package com.tuotiansudai.service.impl;


import com.tuotiansudai.repository.model.LatestInvestView;
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
    public List<LatestInvestView> findLatestInvestByLoginName(String loginName, int startLimit, int endLimit) {
        return investRepayMapper.findLatestInvestByLoginName(loginName, startLimit, endLimit);
    }

    @Override
    public long findSumRepaidInterestByLoginName(String loginName) {
        return investRepayMapper.findSumRepaidInterestByLoginName(loginName);
    }

    @Override
    public long findSumRepayingInterestByLoginName(String loginName) {
        return investRepayMapper.findSumRepayingInterestByLoginName(loginName);
    }

    @Override
    public long findSumRepayingCorpusByLoginName(String loginName) {
        return investRepayMapper.findSumRepayingCorpusByLoginName(loginName);
    }
}
