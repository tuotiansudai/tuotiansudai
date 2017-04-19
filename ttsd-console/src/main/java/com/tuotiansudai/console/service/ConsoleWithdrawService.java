package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.WithdrawPaginationItemDataDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WithdrawModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConsoleWithdrawService {

    @Autowired
    private WithdrawMapper withdrawMapper;

    public BaseDto<BasePaginationDataDto<WithdrawPaginationItemDataDto>> findWithdrawPagination(String withdrawId, String mobile,
                                                                                                WithdrawStatus status, Source source,
                                                                                                int index, int pageSize, Date startTime, Date endTime, String role) {
        if (index < 1) {
            index = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }

        BaseDto<BasePaginationDataDto<WithdrawPaginationItemDataDto>> baseDto = new BaseDto<>();
        List<WithdrawPaginationItemDataDto> withdrawPaginationItemDataDtos = Lists.newArrayList();

        long count = withdrawMapper.findWithdrawCount(withdrawId, mobile, status, source, startTime, endTime, role);

        List<WithdrawModel> withdrawModelList = withdrawMapper.findWithdrawPagination(withdrawId, mobile, status, source, (index - 1) * pageSize, pageSize, startTime, endTime, role);

        for (WithdrawModel model : withdrawModelList) {
            WithdrawPaginationItemDataDto withdrawDto = new WithdrawPaginationItemDataDto(model);
            withdrawPaginationItemDataDtos.add(withdrawDto);
        }

        BasePaginationDataDto<WithdrawPaginationItemDataDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, withdrawPaginationItemDataDtos);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);
        return baseDto;
    }

    public long findSumWithdrawAmount(String withdrawId,
                                      String mobile,
                                      WithdrawStatus status,
                                      Source source,
                                      Date startTime,
                                      Date endTime,
                                      String role) {

        return withdrawMapper.findSumWithdrawAmount(withdrawId, mobile, status, source, role, startTime, endTime);
    }

    public long findSumWithdrawFee(String withdrawId,
                                   String mobile,
                                   WithdrawStatus status,
                                   Source source,
                                   Date startTime,
                                   Date endTime,
                                   String role) {

        return withdrawMapper.findSumWithdrawFee(withdrawId, mobile, status, source, startTime, endTime, role);
    }
}
