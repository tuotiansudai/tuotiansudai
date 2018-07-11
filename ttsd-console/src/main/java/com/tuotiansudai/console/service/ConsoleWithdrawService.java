package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.WithdrawPaginationItemDataDto;
import com.tuotiansudai.enums.AccountType;
import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.repository.mapper.BankWithdrawMapper;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.repository.model.WithdrawPaginationView;
import com.tuotiansudai.repository.model.WithdrawResultPaginationView;
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
        if (accountType != null && accountType == AccountType.UMP) {
            BaseDto<BasePaginationDataDto<WithdrawPaginationItemDataDto>> baseDto = new BaseDto<>();
            long count = withdrawMapper.findWithdrawCount(withdrawId, mobile, status, source, startTime, endTime);
            List<WithdrawModel> withdrawModelList = withdrawMapper.findWithdrawPagination(withdrawId, mobile, status, source, (index - 1) * 10, 10, startTime, endTime);
            List<WithdrawPaginationItemDataDto> withdrawPaginationItemDataDtos = withdrawModelList.stream().map(item -> new WithdrawPaginationItemDataDto(item.getId(),
                    item.getLoginName(),
                    item instanceof WithdrawResultPaginationView ? ((WithdrawResultPaginationView) item).getMobile() : "",
                    item instanceof WithdrawResultPaginationView ? ((WithdrawResultPaginationView) item).getUserName() : "",
                    AmountConverter.convertCentToString(item.getAmount()),
                    AmountConverter.convertCentToString(item.getFee()),
                    item.getSource(),
                    item.getStatus().getDescription(),
                    item.getCreatedTime(),
                    String.valueOf(item instanceof WithdrawResultPaginationView ? ((WithdrawResultPaginationView) item).getUserName() : "")
            )).collect(Collectors.toList());
            return new BaseDto<>(true, new BasePaginationDataDto<>(index, 10, count, withdrawPaginationItemDataDtos));
        } else {
            long count = bankWithdrawMapper.findWithdrawCount(accountType, withdrawId, mobile, status, source, startTime, endTime);
            List<WithdrawPaginationView> views = bankWithdrawMapper.findWithdrawPagination(accountType, withdrawId, mobile, status, source, PaginationUtil.calculateOffset(index, 10, count), 10, startTime, endTime);
            BasePaginationDataDto<WithdrawPaginationItemDataDto> basePaginationDataDto = new BasePaginationDataDto<>(
                    index,
                    10,
                    count,
                    views.stream().map(view -> new WithdrawPaginationItemDataDto(view.getId(),
                            view.getLoginName(),
                            view.getMobile(),
                            view.getUserName(),
                            AmountConverter.convertCentToString(view.getAmount()),
                            AmountConverter.convertCentToString(view.getFee()),
                            view.getSource(),
                            view.getStatus().getDescription(),
                            view.getCreatedTime(),
                            String.valueOf(view.getIsStaff())
                    )).collect(Collectors.toList()));

            return new BaseDto<>(true, basePaginationDataDto);
        }
    }

    public long findSumWithdrawAmount(AccountType accountType, Long withdrawId, String mobile, WithdrawStatus status, Source source, Date startTime, Date endTime) {
        if (accountType != null && accountType == AccountType.UMP) {
            return withdrawMapper.findSumWithdrawAmount(withdrawId, mobile, status, source, startTime, endTime);
        }else{
            return bankWithdrawMapper.sumWithdrawAmount(accountType, withdrawId, mobile, status, source, startTime, endTime);
        }
    }

    public long findSumWithdrawFee(AccountType accountType, Long withdrawId, String mobile, WithdrawStatus status, Source source, Date startTime, Date endTime) {
        if (accountType != null && accountType == AccountType.UMP) {
            return withdrawMapper.findSumWithdrawFee(withdrawId, mobile, status, source, startTime, endTime);
        }else{
            return bankWithdrawMapper.sumWithdrawFee(accountType, withdrawId, mobile, status, source, startTime, endTime);
        }

    }
}
