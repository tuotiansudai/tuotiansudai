package com.tuotiansudai.point.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.repository.dto.ChannelPointPaginationItemDataDto;
import com.tuotiansudai.point.repository.mapper.ChannelPointMapper;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelPointServiceImpl {
    static Logger logger = Logger.getLogger(ChannelPointServiceImpl.class);
    @Autowired
    private ChannelPointMapper channelPointMapper;

    public BaseDto<BasePaginationDataDto> getDetailList(int index, int pageSize) {
        long count = channelPointMapper.findCountByPagination();
        List<ChannelPointPaginationItemDataDto> itemDataDtos = channelPointMapper.findByPagination(PaginationUtil.calculateOffset(index, pageSize, count), pageSize)
                .stream()
                .map(channelPointModel -> new ChannelPointPaginationItemDataDto(channelPointModel))
                .collect(Collectors.toList());

        BasePaginationDataDto paginationDataDto = new BasePaginationDataDto(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, itemDataDtos);
        paginationDataDto.setStatus(true);
        return new BaseDto<>(paginationDataDto);

    }

    public long getSumTotalPoint() {
        return channelPointMapper.findSumTotalCount();
    }

    public long getSumHeadCount() {
        return channelPointMapper.findSumHeadCount();
    }
}
