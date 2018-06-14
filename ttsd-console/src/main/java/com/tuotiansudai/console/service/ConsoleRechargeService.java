package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.RechargePaginationItemDataDto;
import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.repository.mapper.BankRechargeMapper;
import com.tuotiansudai.repository.model.BankRechargePaginationView;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsoleRechargeService {

    @Autowired
    private BankRechargeMapper bankRechargeMapper;

    public List<String> findAllChannel() {
        return bankRechargeMapper.findAllChannels();
    }

    public BaseDto<BasePaginationDataDto<RechargePaginationItemDataDto>> findRechargePagination(String rechargeId, String mobile, Source source,
                                                                                                BankRechargeStatus status, String channel, int index, int pageSize, Date startTime, Date endTime, String role) {
        index = index < 1 ? 1 : index;
        int count = bankRechargeMapper.findRechargeCount(rechargeId, mobile, source, status, channel, startTime, endTime, role);

        List<BankRechargePaginationView> views = bankRechargeMapper.findRechargePagination(rechargeId, mobile, source, status, channel, (index - 1) * pageSize, pageSize, startTime, endTime, role);

        return new BaseDto<>(true, new BasePaginationDataDto<RechargePaginationItemDataDto>(
                index,
                10,
                count,
                views.stream().map(view -> new RechargePaginationItemDataDto(view.getId(),
                        view.getStatus().name(),
                        view.getCreatedTime(),
                        view.getLoginName(),
                        view.getMobile(),
                        view.getUserName(),
                        view.getIsStaff(),
                        AmountConverter.convertCentToString(view.getAmount()),
                        view.getPayType(),
                        view.getSource(),
                        view.getChannel())).collect(Collectors.toList())));

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
