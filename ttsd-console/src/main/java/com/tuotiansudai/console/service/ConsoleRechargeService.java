package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.RechargePaginationItemDataDto;
import com.tuotiansudai.enums.AccountType;
import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.repository.mapper.BankRechargeMapper;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.BankRechargePaginationView;
import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.repository.model.RechargePaginationView;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsoleRechargeService {

    @Autowired
    private BankRechargeMapper bankRechargeMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    public List<String> findAllChannel() {
        return bankRechargeMapper.findAllChannels().stream().filter(item -> !StringUtils.isEmpty(item)).distinct().collect(Collectors.toList());
    }

    public BaseDto<BasePaginationDataDto<RechargePaginationItemDataDto>> findRechargePagination(AccountType accountType, String rechargeId, String mobile, Source source,
                                                                                                BankRechargeStatus status, String channel, int index, int pageSize, Date startTime, Date endTime) {

        index = index < 1 ? 1 : index;
        int count = 0;
        List<BankRechargePaginationView> views = null;
        if (accountType != null && accountType == AccountType.UMP) {
            count = rechargeMapper.findRechargeCount(rechargeId, mobile, source, status, channel, startTime, endTime);
            views = rechargeMapper.findRechargePagination(rechargeId, mobile, source, status, channel, (index - 1) * pageSize, pageSize, startTime, endTime);
        } else {
            count = bankRechargeMapper.findRechargeCount(accountType, rechargeId, mobile, source, status, channel, startTime, endTime);
            views = bankRechargeMapper.findRechargePagination(accountType, rechargeId, mobile, source, status, channel, (index - 1) * pageSize, pageSize, startTime, endTime);

        }
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

    public long findSumRechargeAmount(
            AccountType accountType,
            String rechargeId,
            String mobile,
            Source source,
            BankRechargeStatus status,
            String channel,
            Date startTime,
            Date endTime) {
        if (accountType != null && accountType == AccountType.UMP) {
            return rechargeMapper.findSumRechargeAmount(rechargeId, mobile, source, status, channel, startTime, endTime);
        } else {
            return bankRechargeMapper.findSumRechargeAmount(accountType, rechargeId, mobile, source, status, channel, startTime, endTime);
        }

    }
}
