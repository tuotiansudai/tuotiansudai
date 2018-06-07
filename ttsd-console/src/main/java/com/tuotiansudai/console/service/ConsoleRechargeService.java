package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.RechargePaginationItemDataDto;
import com.tuotiansudai.repository.mapper.BankRechargeMapper;
import com.tuotiansudai.repository.model.BankRechargePaginationView;
import com.tuotiansudai.repository.model.BankRechargeStatus;
import com.tuotiansudai.repository.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConsoleRechargeService {

    @Autowired
    private BankRechargeMapper bankRechargeMapper;

    public List<String> findAllChannel() {
        return bankRechargeMapper.findAllChannels();
    }

    public BasePaginationDataDto<BankRechargePaginationView> findRechargePagination(String rechargeId, String mobile, Source source,
                                                                                             BankRechargeStatus status, String channel, int index, int pageSize, Date startTime, Date endTime, String role) {
        if (index < 1) {
            index = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }

        BaseDto<BasePaginationDataDto<RechargePaginationItemDataDto>> baseDto = new BaseDto<>();

        int count = bankRechargeMapper.findRechargeCount(rechargeId, mobile, source, status, channel, startTime, endTime, role);

        List<BankRechargePaginationView> bankRechargePaginationViews = bankRechargeMapper.findRechargePagination(rechargeId, mobile, source, status, channel, (index - 1) * pageSize, pageSize, startTime, endTime, role);

        return new BasePaginationDataDto<>(index, pageSize, count, bankRechargePaginationViews);
    }

    public long findSumRechargeAmount(String rechargeId,
                                      String mobile,
                                      Source source,
                                      BankRechargeStatus status,
                                      String channel,
                                      Date startTime,
                                      Date endTime,
                                      String role) {
        return bankRechargeMapper.findSumRechargeAmount(rechargeId, mobile, source, status, channel, role, startTime, endTime);
    }
}
