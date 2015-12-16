package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.repository.model.WithdrawStatus;
import com.tuotiansudai.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WithdrawServiceImpl implements WithdrawService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Override
    public BaseDto<PayFormDataDto> withdraw(WithdrawDto withdrawDto) {
        return payWrapperClient.withdraw(withdrawDto);
    }

    @Override
    public long sumSuccessWithdrawAmount(String loginName) {
        return withdrawMapper.findSumSuccessWithdrawByLoginName(loginName);
    }

    @Override
    public BaseDto<BasePaginationDataDto> findWithdrawPagination(String withdrawId, String loginName,
                                                                 WithdrawStatus status, Source source,
                                                                 int index, int pageSize, Date startTime, Date endTime) {
        if (index < 1) {
            index = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }

        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        List<WithdrawPaginationItemDataDto> withdrawPaginationItemDataDtos = Lists.newArrayList();

        long count = withdrawMapper.findWithdrawCount(withdrawId, loginName, status, source, startTime, endTime);

        List<WithdrawModel> withdrawModelList = withdrawMapper.findWithdrawPagination(withdrawId, loginName, status, source, (index - 1) * pageSize, pageSize, startTime, endTime);

        for (WithdrawModel model : withdrawModelList) {
            WithdrawPaginationItemDataDto withdrawDto = new WithdrawPaginationItemDataDto(model);
            withdrawPaginationItemDataDtos.add(withdrawDto);
        }

        BasePaginationDataDto<WithdrawPaginationItemDataDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, withdrawPaginationItemDataDtos);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);
        return baseDto;
    }


    @Override
    public long findSumWithdrawAmount(String withdrawId,
                                      String loginName,
                                      WithdrawStatus status,
                                      Source source,
                                      Date startTime,
                                      Date endTime) {

        return withdrawMapper.findSumWithdrawAmount(withdrawId, loginName, status, source, startTime, endTime);
    }

    @Override
    public long findSumWithdrawFee(String withdrawId,
                                   String loginName,
                                   WithdrawStatus status,
                                   Source source,
                                   Date startTime,
                                   Date endTime) {

        return withdrawMapper.findSumWithdrawFee(withdrawId, loginName, status, source, startTime, endTime);
    }

}
