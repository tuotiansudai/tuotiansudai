package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.RechargePaginationItemDataDto;
import com.tuotiansudai.enums.RechargeStatus;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.BankRechargeMapper;
import com.tuotiansudai.repository.mapper.RechargeMapper;
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

    @Autowired
    private RechargeMapper rechargeMapper;


    public BaseDto<BasePaginationDataDto<RechargePaginationItemDataDto>> findRechargePagination(Role role, String rechargeId, String mobile, Source source,
                                                                                                RechargeStatus status, String channel, int index, int pageSize, Date startTime, Date endTime) {

        index = index < 1 ? 1 : index;
        int count = 0;
        List<BankRechargePaginationView> views = null;
        if (role == Role.UMP_INVESTOR) {
            count = rechargeMapper.findRechargeCount(rechargeId, mobile, source, status, channel, startTime, endTime);
            views = rechargeMapper.findRechargePagination(rechargeId, mobile, source, status, channel, (index - 1) * pageSize, pageSize, startTime, endTime);
        } else {
            count = bankRechargeMapper.findRechargeCount(role, rechargeId, mobile, source, status, channel, startTime, endTime);
            views = bankRechargeMapper.findRechargePagination(role, rechargeId, mobile, source, status, channel, (index - 1) * pageSize, pageSize, startTime, endTime);

        }
        return new BaseDto<>(true, new BasePaginationDataDto<RechargePaginationItemDataDto>(
                index,
                10,
                count,
                views.stream().map(view -> new RechargePaginationItemDataDto(view.getId(),
                        view.getStatus().name(),
                        view.getCreatedTime(),
                        view.getLoginName(),
                        view.getUserName(),
                        view.getUmpUserName(),
                        view.getMobile(),
                        view.getIsStaff(),
                        AmountConverter.convertCentToString(view.getAmount()),
                        view.getPayType(),
                        view.getSource(),
                        view.getChannel())).collect(Collectors.toList())));
    }

    public long findSumRechargeAmount(
            Role role,
            String rechargeId,
            String mobile,
            Source source,
            RechargeStatus status,
            String channel,
            Date startTime,
            Date endTime) {
        if (role == Role.UMP_INVESTOR) {
            return rechargeMapper.findSumRechargeAmount(rechargeId, mobile, source, status, channel, startTime, endTime);
        } else {
            return bankRechargeMapper.findSumRechargeAmount(role, rechargeId, mobile, source, status, channel, startTime, endTime);
        }

    }
}
