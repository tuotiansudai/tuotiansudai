package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.WithdrawPaginationItemDataDto;
import com.tuotiansudai.enums.AccountType;
import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.repository.mapper.BankWithdrawMapper;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WithdrawPaginationView;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsoleWithdrawService {

    private final BankWithdrawMapper bankWithdrawMapper;

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Autowired
    public ConsoleWithdrawService(BankWithdrawMapper bankWithdrawMapper) {
        this.bankWithdrawMapper = bankWithdrawMapper;
    }

    public BaseDto<BasePaginationDataDto<WithdrawPaginationItemDataDto>> findWithdrawPagination(AccountType accountType, Long withdrawId, String mobile, WithdrawStatus status, Source source, int index, Date startTime, Date endTime) {
        index = index < 1 ? 1 : index;
        List<WithdrawPaginationView> views = null;
        long count = 0;
        if (accountType != null && accountType == AccountType.UMP) {
            count = withdrawMapper.findWithdrawCount(withdrawId, mobile, status, source, startTime, endTime);
            views = withdrawMapper.findWithdrawPagination(withdrawId, mobile, status, source, (index - 1) * 10, 10, startTime, endTime);

        } else {
            count = bankWithdrawMapper.findWithdrawCount(accountType, withdrawId, mobile, status, source, startTime, endTime);
            views = bankWithdrawMapper.findWithdrawPagination(accountType, withdrawId, mobile, status, source, PaginationUtil.calculateOffset(index, 10, count), 10, startTime, endTime);
        }
        List<WithdrawPaginationItemDataDto> withdrawPaginationItemDataDtos = views.stream().map(item -> new WithdrawPaginationItemDataDto(item.getId(),
                item.getLoginName(),
                item.getMobile(),
                item.getUserName(),
                AmountConverter.convertCentToString(item.getAmount()),
                AmountConverter.convertCentToString(item.getFee()),
                item.getSource(),
                item.getStatus().getDescription(),
                item.getCreatedTime(),
                String.valueOf(item.getIsStaff())
        )).collect(Collectors.toList());
        return new BaseDto<>(true, new BasePaginationDataDto<>(index, 10, count, withdrawPaginationItemDataDtos));
    }

    public long findSumWithdrawAmount(AccountType accountType, Long withdrawId, String mobile, WithdrawStatus status, Source source, Date startTime, Date endTime) {
        if (accountType != null && accountType == AccountType.UMP) {
            return withdrawMapper.findSumWithdrawAmount(withdrawId, mobile, status, source, startTime, endTime);
        } else {
            return bankWithdrawMapper.sumWithdrawAmount(accountType, withdrawId, mobile, status, source, startTime, endTime);
        }
    }

    public long findSumWithdrawFee(AccountType accountType, Long withdrawId, String mobile, WithdrawStatus status, Source source, Date startTime, Date endTime) {
        if (accountType != null && accountType == AccountType.UMP) {
            return withdrawMapper.findSumWithdrawFee(withdrawId, mobile, status, source, startTime, endTime);
        } else {
            return bankWithdrawMapper.sumWithdrawFee(accountType, withdrawId, mobile, status, source, startTime, endTime);
        }

    }
}
