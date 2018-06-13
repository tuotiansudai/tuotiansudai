package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.WithdrawPaginationItemDataDto;
import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.repository.mapper.BankWithdrawMapper;
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
    public ConsoleWithdrawService(BankWithdrawMapper bankWithdrawMapper) {
        this.bankWithdrawMapper = bankWithdrawMapper;
    }

    public BaseDto<BasePaginationDataDto<WithdrawPaginationItemDataDto>> findWithdrawPagination(Long withdrawId, String mobile, WithdrawStatus status, Source source, int index, Date startTime, Date endTime) {
        index = index < 1 ? 1 : index;
        long count = bankWithdrawMapper.findWithdrawCount(withdrawId, mobile, status, source, startTime, endTime);
        List<WithdrawPaginationView> views = bankWithdrawMapper.findWithdrawPagination(withdrawId, mobile, status, source, PaginationUtil.calculateOffset(index, 10, count), 10, startTime, endTime);
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

    public long findSumWithdrawAmount(Long withdrawId, String mobile, WithdrawStatus status, Source source, Date startTime, Date endTime) {
        return bankWithdrawMapper.sumWithdrawAmount(withdrawId, mobile, status, source, startTime, endTime);
    }

    public long findSumWithdrawFee(Long withdrawId, String mobile, WithdrawStatus status, Source source, Date startTime, Date endTime) {
        return bankWithdrawMapper.sumWithdrawFee(withdrawId, mobile, status, source, startTime, endTime);
    }
}
